package com.melody.text.effect.graph

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.melody.text.effect.R
import com.melody.text.effect.content.MainContent

/**
 * addMainGraph
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/2 21:28
 */
fun NavGraphBuilder.addMainGraph(navController: NavController) {
    composable("/home/screen") {
        val context = LocalContext.current
        MainContent {
            when(it) {
               context.getString(R.string.item_while_get_bounding_box) -> {
                   navController.navigate("/text/while_boundingBox")
               }
               context.getString(R.string.item_draw_path) -> {
                   navController.navigate("/text/drawPath")
               }
               context.getString(R.string.item_ext_get_bounding_box) -> {
                   navController.navigate("/text/CustomTextSpansGraph")
               }
                context.getString(R.string.item_wave_line_animation) -> {
                    navController.navigate("/text/wave_line_animation")
                }
                context.getString(R.string.item_text_separation_anim) -> {
                    navController.navigate("/text/TextSeparationGraph")
                }
                context.getString(R.string.item_text_expand) -> {
                    navController.navigate("/text/expandable")
                }
            }
        }
    }
}