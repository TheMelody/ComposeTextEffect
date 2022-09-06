package com.melody.text.effect.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.ResolvedTextDirection

/**
 * TextLayoutExtension
 * @author TheMelody
 * email developer_melody@163.com
 * created 2022/9/2 10:54
 */
fun TextLayoutResult.getBoundingBoxRectList(startOffset:Int, endOffset:Int): List<Rect>{
    // TextLayoutResult.getLineForOffset返回指定文本偏移位置处的行号
    val startLine = getLineForOffset(startOffset)
    val endLine = getLineForOffset(endOffset)
    val boundingBoxRectList = mutableListOf<Rect>()
    if(startLine == endLine) { // 处理单行文本的问题
        var rect = Rect.Zero
        for (index in startOffset until endOffset) {
            val bound = getBoundingBox(index)
            if(index == startOffset) {
                rect = rect.copy(left = bound.left, top = bound.top, bottom = bound.bottom)
            }
            if(index == endOffset - 1) {
                rect = rect.copy(right = bound.right)
            }
        }
        boundingBoxRectList.add(rect)
    } else { // 多行文本
        val isLtr = multiParagraph.getParagraphDirection(offset = layoutInput.text.lastIndex) == ResolvedTextDirection.Ltr
        for (lineIndex in startLine..endLine){
            val rect = Rect(
                // 返回指定行的顶部坐标
                top = getLineTop(lineIndex),
                // 返回指定行的底部坐标
                bottom = getLineBottom(lineIndex),
                left = if (lineIndex == startLine) {
                    // 获取指定文本偏移的水平位置。返回与文本起始偏移量的相对距离
                    getHorizontalPosition(offset = startOffset, usePrimaryDirection = isLtr)
                } else {
                    // 返回指定行的左边水平x坐标
                    getLineLeft(lineIndex)
                },
                right = if (lineIndex == endLine) {
                    // 获取指定文本偏移的水平位置。返回与文本起始偏移量的相对距离
                    getHorizontalPosition(offset = endOffset, usePrimaryDirection = isLtr)
                } else {
                    // 返回指定行的右边水平x坐标
                    getLineRight(lineIndex)
                }
            )
            boundingBoxRectList.add(rect)
        }
    }
    return boundingBoxRectList
}
