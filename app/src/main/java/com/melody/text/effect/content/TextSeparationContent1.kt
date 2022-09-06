package com.melody.text.effect.content

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * TextSeparationContent1
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/4 08:55
 */
@OptIn(ExperimentalTextApi::class)
@Composable
internal fun TextSeparationContent1() {
    val textMeasure = rememberTextMeasurer()
    var onDraw: DrawScope.() -> Unit by remember { mutableStateOf({}) }
    var textLayoutResultState by remember { mutableStateOf<TextLayoutResult?>(null) }
    // 每次进来都随机
    val animProgress = rememberInfiniteTransition().animateFloat(
        initialValue = (-35..-5).random().toFloat(),
        targetValue = (5..45).random().toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val brush = Brush.horizontalGradient(
        listOf(
            Color(0xFF22B6FF),
            Color(0xFFB732FF),
            Color(0xFFFF1D37)
        )
    )
    val textStyle = MaterialTheme.typography.body1.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        brush = brush
    )
    val text = buildAnnotatedString { append("人生如果没有梦想，那和咸鱼有什么区别。") }

    LaunchedEffect(textLayoutResultState) {
        textLayoutResultState?.let {
            onDraw = {
                for (index in text.indices){
                    val boundsRect = it.getBoundingBox(index)
                    withTransform({
                            rotate(
                                degrees = if (index % 2 == 0) {
                                    animProgress.value
                                } else {
                                    -animProgress.value
                                },
                                pivot = boundsRect.center
                            )
                        }
                    ) {
                        drawText(
                            textMeasurer = textMeasure,
                            style = textStyle,
                            text = text[index].toString(),
                            topLeft = boundsRect.topLeft,
                        )
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        onDraw()
                        drawRect(brush, blendMode = BlendMode.SrcAtop)
                    }
                },
            onTextLayout = {
                textLayoutResultState = it
            }
        )
    }
}
