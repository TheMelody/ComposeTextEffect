package com.melody.text.effect.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.melody.text.effect.graph.*

/**
 * MainScreen
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/2 21:16
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        navController = navController,
        startDestination = "/home/screen"
    ) {
        addMainGraph(navController)
        addDrawPathGraph()
        addWhileBoundingBoxGraph()
        addCustomTextSpansGraph()
        addWaveLineAnimGraph()
        addTextSeparationGraph()
        addExpandableTextGraph()
    }
}