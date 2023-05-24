package com.melody.text.effect.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.melody.expandable.text_compose.ExpandableText
import com.melody.text.effect.R

/**
 * ExpandableTextContent
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/6 20:35
 */
@Composable
internal fun ExpandableTextContent() {
    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()) {
        ExpandableText(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxWidth(),
            // 读者可自行设置文本，查看效果
            //text = buildAnnotatedString { append(stringResource(id = R.string.test_expand_content)) },
            text = buildAnnotatedString { append(stringResource(id = R.string.test_content_san_guo)) },
            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
            expandStateText = "展开",
            collapseStateText = "收起"
        )
    }
}
