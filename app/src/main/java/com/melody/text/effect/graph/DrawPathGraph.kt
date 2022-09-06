package com.melody.text.effect.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.melody.text.effect.content.DrawPathContent

/**
 * drawPath实现的默认：普通文本跨行背景绘制
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/2 21:21
 */
fun NavGraphBuilder.addDrawPathGraph(){
    composable("/text/drawPath"){
        DrawPathContent()
    }
}