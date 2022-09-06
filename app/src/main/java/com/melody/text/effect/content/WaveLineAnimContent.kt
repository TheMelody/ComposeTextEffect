package com.melody.text.effect.content

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.melody.text.effect.components.getBoundingBoxRectList
import kotlin.math.ceil
import kotlin.math.sin


/**
 * 指定某段内容下方绘制波浪线动画
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/3 14:23
 */
@OptIn(ExperimentalTextApi::class)
@Composable
internal fun WaveLineAnimContent() {
    var onDraw: DrawScope.() -> Unit by remember { mutableStateOf({}) }
    var textLayoutResultState by remember { mutableStateOf<TextLayoutResult?>(null) }
    val path by remember { mutableStateOf(Path()) }
    val annotatedText = buildAnnotatedString {
        append("我们不会期待")
        withAnnotation("miFans", annotation = "根据tag可获取") {
            withStyle(SpanStyle(color = Color(0xFFFA3939))) {
                append("米粉的期待")
            }
        }
        append("，兄弟们支持了吗?\uD83D\uDE02")
    }

    val animationProgress by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val density = LocalDensity.current
    LaunchedEffect(textLayoutResultState) {
        textLayoutResultState?.let {
            val annotation = annotatedText.getStringAnnotations("miFans",0, annotatedText.length).first()
            val rectList = it.getBoundingBoxRectList(annotation.start,annotation.end)
            onDraw = {
                rectList.forEach { rect->
                    /*val underline = rect.copy(top = rect.bottom - 2.sp.toPx())
                    drawRect(
                        color = Color.Blue,
                        topLeft = underline.topLeft,
                        size = underline.size,
                    )*/
                    // 一开始写的波浪线
                    path.buildWaveLinePath(
                        bound = rect,
                        waveLength = 20.sp.toPx(),
                        animProgress = animationProgress
                    )
                    // 波浪线后来的写法
                    /*path.buildWavePath(
                        rect = rect,
                        density = density,
                        animProgress = animationProgress
                    )*/
                    val pathStyle = Stroke(
                        width = 2.sp.toPx(),
                        join = StrokeJoin.Round,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.cornerPathEffect(radius = 9.dp.toPx())
                    )
                    drawPath(
                        path = path,
                        color = Color(0xFFFA3939),
                        style = pathStyle
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = annotatedText,
            modifier = Modifier
                .align(Alignment.Center)
                .drawBehind { onDraw() },
            style = TextStyle(fontSize = 30.sp),
            onTextLayout = {
                textLayoutResultState = it
            }
        )
    }
}
private fun Path.buildWaveLinePath(bound: Rect, waveLength:Float,animProgress: Float): Path {
    asAndroidPath().rewind()
    val TWO_PI = 2 * Math.PI.toFloat()

    var x = bound.left
    while (x < bound.right) {
        val offsetY = bound.bottom + sin(((x - bound.left) / waveLength) * TWO_PI + (TWO_PI * animProgress))* 5
        if(x == bound.left) {
            moveTo(bound.left, offsetY)
        }
        lineTo(x, offsetY)
        x += 1F
    }
    return this
}

private fun Path.buildWavePath(rect: Rect, density: Density, animProgress:Float) = density.run {
    val width: TextUnit = 4.sp
    val wavelength: TextUnit = 20.sp
    val amplitude: TextUnit = 2.sp
    val bottomOffset: TextUnit = 2.sp
    val TWO_PI = 2 * Math.PI.toFloat()

    val lineStart = rect.left + (width.toPx() / 2)
    val lineEnd = rect.right - (width.toPx() / 2)
    val lineBottom = rect.bottom + bottomOffset.toPx()

    val segmentWidth = wavelength.toPx() / 10
    val points = ceil((lineEnd - lineStart) / segmentWidth).toInt()

    var pointX = lineStart

    asAndroidPath().rewind()
    for (index in 0..points) {
        val proportionOfWavelength = (pointX - lineStart) / wavelength.toPx()
        val radiansX = proportionOfWavelength * TWO_PI + (TWO_PI * animProgress)
        val offsetY = lineBottom + (sin(radiansX) * amplitude.toPx())
        when (index) {
            0 -> moveTo(pointX, offsetY)
            else -> lineTo(pointX, offsetY)
        }
        pointX = (pointX + segmentWidth).coerceAtMost(lineEnd)
    }
}
