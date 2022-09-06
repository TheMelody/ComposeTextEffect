package com.melody.text.effect.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.sp
import com.melody.text.effect.R

/**
 * DrawPath：普通的文本跨度背景绘制
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/2 21:50
 */
@Composable
internal fun DrawPathContent() {
    val testContent = stringResource(id = R.string.test_content_san_guo)
    val textSpansContent = stringResource(id = R.string.test_content_san_guo_text_spans)
    var onDraw: DrawScope.() -> Unit by remember { mutableStateOf({}) }
    var textLayoutResultState by remember { mutableStateOf<TextLayoutResult?>(null) }

    LaunchedEffect(textLayoutResultState) {
        textLayoutResultState?.let {
            val textStartIndex = testContent.indexOf(textSpansContent)
            val path = it.getPathForRange(textStartIndex,textSpansContent.length)
            onDraw = {
                drawPath(
                    path = path,
                    brush = SolidColor(Color(0xFF899BBE)),
                    //style = Stroke(width = 1.sp.toPx())
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = testContent,
            style = MaterialTheme.typography.body1.copy(lineHeight = 20.sp),
            modifier = Modifier
                .align(Alignment.Center)
                .drawBehind { onDraw() },
            onTextLayout = {
                textLayoutResultState = it
            }
        )
    }
}
