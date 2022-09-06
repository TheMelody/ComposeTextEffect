package com.melody.text.effect.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.melody.text.effect.content.CustomTextSpansContent

/**
 * 扩展getBoundingBox实现：文本跨行背景绘制
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/2 21:23
 */
fun NavGraphBuilder.addCustomTextSpansGraph() {
    composable("/text/CustomTextSpansGraph"){
        CustomTextSpansContent()
    }
}