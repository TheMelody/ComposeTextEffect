package com.melody.text.effect.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.melody.text.effect.content.TextSeparationContent1
import com.melody.text.effect.content.TextSeparationContent2

/**
 * 文字分离
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/3 20:20
 */
fun NavGraphBuilder.addTextSeparationGraph() {
    composable("/text/TextSeparationGraph"){
        TextSeparationContent1()
    }
}