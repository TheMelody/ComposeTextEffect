package com.melody.text.effect.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.melody.text.effect.utils.rippleClickable
import com.melody.text.effect.R

/**
 * MainContent
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/2 21:30
 */
@Composable
internal fun MainContent(onItemClick: (String) -> Unit) {
    val funItemList = stringArrayResource(id = R.array.fun_item_list).toList()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(items = funItemList) { index, item ->
            CardItem(text = item) {
                onItemClick.invoke(item)
            }
            if (index < funItemList.lastIndex) {
                Divider()
            }
        }
    }
}

@Composable
private fun CardItem(text: String, onItemClick: () -> Unit) {
    val currentOnItemClick by rememberUpdatedState(newValue = onItemClick)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .rippleClickable(currentOnItemClick)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .align(Alignment.CenterStart),
            style = MaterialTheme.typography.body1
                    .copy(color = Color.Black.copy(alpha = 0.66F))
        )
    }
}