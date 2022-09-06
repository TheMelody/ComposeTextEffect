package com.melody.text.effect.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.melody.text.effect.content.ExpandableTextContent

/**
 * 文字展开/收起
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/6 20:33
 */
fun NavGraphBuilder.addExpandableTextGraph() {
    composable("/text/expandable") {
        ExpandableTextContent()
    }
}