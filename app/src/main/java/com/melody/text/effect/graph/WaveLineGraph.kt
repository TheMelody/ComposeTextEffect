package com.melody.text.effect.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.melody.text.effect.content.WaveLineAnimContent

/**
 * 波浪线动画：指定某段内容下方绘制波浪线
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/3 14:22
 */
fun NavGraphBuilder.addWaveLineAnimGraph() {
    composable("/text/wave_line_animation") {
        WaveLineAnimContent()
    }
}