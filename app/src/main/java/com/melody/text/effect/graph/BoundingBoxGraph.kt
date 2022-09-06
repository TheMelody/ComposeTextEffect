package com.melody.text.effect.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.melody.text.effect.content.WhileGetBoundingBoxContent

/**
 * 循环实现：普通的文本跨度背景绘制
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/2 21:19
 */
fun NavGraphBuilder.addWhileBoundingBoxGraph(){
    composable("/text/while_boundingBox") {
        WhileGetBoundingBoxContent()
    }
}
