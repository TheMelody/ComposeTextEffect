package com.melody.text

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import com.melody.text.ext.detectTouchGestures
import kotlin.math.roundToInt

/**
 * 展开/收起，文本内容组件
 * @author 被风吹过的夏天
 * email developer_melody@163.com
 * created 2022/9/6 14:51
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    minimizedMaxLines: Int = 3,
    expandStateText: String,
    collapseStateText: String,
    textStyle: TextStyle
) {
    // 仅用于测量：展开/收起，文字大小等数据
    val readOnlyMeasureText = buildAnnotatedString { append(expandStateText) }
    // 用于：追加在正文的后面，【展开和收起，和省略号不是一体，它们是分开的】
    val ellipsisText = buildAnnotatedString { append("... ") }
    // 查看更多的TextMeasure
    val seeMoreTextMeasure = rememberTextMeasurer()
    // 正文的TextMeasure
    val contentTextMeasure = rememberTextMeasurer()
    // 存储正文的宽度和高度大小值
    var contentMeasureSize by rememberSaveable(stateSaver = SizeValueSaver) {
        mutableStateOf(Size.Zero)
    }
    // 展开，收起，执行的动画 Animatable
    val animaTable = remember { Animatable(0F, Float.VectorConverter) }
    // 当前是展开还是收起
    var expanded by rememberSaveable { mutableStateOf(false) }
    // 是不是首次初始化渲染
    var isInitRender by rememberSaveable { mutableStateOf(true) }
    // 默认收起状态下，最下高度
    var collapseMinHeight by rememberSaveable { mutableStateOf(0F) }
    // 展开状态下，最大高度
    var expandMaxHeight by rememberSaveable { mutableStateOf(0F) }
    // 是不是来自：查看更多按钮的点击
    var fromSeeMoreClick by rememberSaveable { mutableStateOf(false) }
    // 更新查看更多的文本内容背景色，用于触摸点击的时候能看到，按钮被点击
    var seeMoreTextBackground by remember { mutableStateOf(Color.Transparent) }
    // 展开/收起，按钮的位置
    var seeMoreOffset by rememberSaveable(stateSaver = OffsetValueSaver) {
        mutableStateOf(Offset.Zero)
    }
    // 首行内容的顶部位置
    var firstLineTop by rememberSaveable { mutableStateOf(0F) }
    // 省略号，上一行内容的底部位置
    var noEllipsisLineBtm by rememberSaveable { mutableStateOf(0F) }
    // 省略号的左侧位置
    var ellipsisLineLeftX by rememberSaveable { mutableStateOf(0F) }
    // 省略号【这一行内容】的顶部位置
    var ellipsisLineTop by rememberSaveable { mutableStateOf(0F) }
    // 省略号【这一行内容】的右侧位置
    var ellipsisLineRight by rememberSaveable { mutableStateOf(0F) }

    var textLayoutResultState by remember { mutableStateOf<TextLayoutResult?>(null) }

    LaunchedEffect(expanded, isInitRender) {
        if (!isInitRender) {
            // 执行，展开、收起，动画
            animaTable.animateTo(
                targetValue = if (expanded) expandMaxHeight else collapseMinHeight,
                animationSpec = tween()
            )
        }
    }

    LaunchedEffect(collapseMinHeight) {
        if (isInitRender) {
            // 下次动画开始的初始值
            animaTable.snapTo(collapseMinHeight)
        }
    }

    LaunchedEffect(animaTable.isRunning) {
        if (!animaTable.isRunning) {
            // 恢复默认状态
            fromSeeMoreClick = false
        }
    }


    // 测量查看更多，即：展开/收起，按钮。返回TextLayoutResult
    val seeMoreTextLayoutResult = seeMoreTextMeasure.measure(
        text = readOnlyMeasureText,
        style = textStyle.copy(color = Color.Blue, fontWeight = FontWeight.Medium)
    )

    // 测量省略号,返回TextLayoutResult
    val ellipsisTextLayoutResult = seeMoreTextMeasure.measure(
        text = ellipsisText,
        style = textStyle
    )

    // 计算展开的高度
    val expandHeight by remember {
        derivedStateOf {
            if (!isInitRender && animaTable.value > 0) {
                animaTable.value + if (expanded) seeMoreTextLayoutResult.size.height else 0
            } else {
                collapseMinHeight
            }
        }
    }

    // 计算：... 展开，内容总宽度
    val seeMoreTextTotalWidth by remember {
        derivedStateOf {
            seeMoreTextLayoutResult.size.width + ellipsisTextLayoutResult.size.width
        }
    }

    // 省略号，右侧裁剪的位置
    val ellipsisClipRight by remember {
        derivedStateOf {
            contentMeasureSize.width - if (!fromSeeMoreClick && !expanded) seeMoreTextTotalWidth else 0
        }
    }

    LaunchedEffect(contentMeasureSize) {
        if(contentMeasureSize == Size.Zero) return@LaunchedEffect
        // 测量正文内容
        val textLayoutResult = contentTextMeasure.measure(
            text = text,
            style = textStyle,
            constraints = Constraints(
                maxWidth = contentMeasureSize.width.roundToInt(),
                maxHeight = contentMeasureSize.height.roundToInt()
            )
        )
        textLayoutResultState = textLayoutResult
        // 计算文字第一行的顶部位置
        firstLineTop = textLayoutResult.getLineTop(0)
        val currentEndLine = if (minimizedMaxLines >= textLayoutResult.lineCount - 1) {
            textLayoutResult.lineCount - 1
        } else {
            minimizedMaxLines
        }
        // 计算出，默认状态收起下，最小高度，展开状态下，最大高度
        collapseMinHeight = textLayoutResult.getLineBottom(currentEndLine)
        expandMaxHeight = textLayoutResult.getLineBottom(textLayoutResult.lineCount - 1)

        // 显示省略号的上一行的底部位置
        noEllipsisLineBtm =
            textLayoutResult.getLineBottom(if (!expanded) currentEndLine - 1 else textLayoutResult.lineCount - 2)

        // 显示省略号的一行顶部位置
        ellipsisLineTop =
            textLayoutResult.getLineTop(if (!expanded) currentEndLine else textLayoutResult.lineCount - 1)

        // 显示省略号的一行右侧位置，处理，这一行文本有换行符的情况
        ellipsisLineRight = textLayoutResult.getLineRight(if (!expanded) currentEndLine else textLayoutResult.lineCount - 1)

        // 处理换行符的那一行，右侧位置
        val collapseEllipsisRightX =  (contentMeasureSize.width - seeMoreTextTotalWidth).coerceAtMost(
            ellipsisLineRight - seeMoreTextTotalWidth
        )
        ellipsisLineLeftX = if(collapseEllipsisRightX >= contentMeasureSize.width- seeMoreTextTotalWidth){
            contentMeasureSize.width - seeMoreTextTotalWidth
        } else{
            if(ellipsisLineRight <= contentMeasureSize.width- seeMoreTextTotalWidth) {
                ellipsisLineRight
            } else  {
                contentMeasureSize.width- seeMoreTextTotalWidth
            }
        }
        seeMoreOffset = seeMoreOffset.copy(
            x = if (expanded) 0F else ellipsisLineLeftX + ellipsisTextLayoutResult.size.width,
            y = (expandHeight - seeMoreTextLayoutResult.size.height)
        )
    }

    Box(modifier = modifier
        .height(LocalDensity.current.run { expandHeight.toDp() })
        .pointerInput(Unit) {
            detectTransformGestures(
                onPress = {
                    seeMoreTextBackground = refreshSeeMoreBtnBg(
                        touchOffset = it,
                        textLayoutResult = seeMoreTextLayoutResult,
                        btnOffset = seeMoreOffset
                    )
                },
                onClick = {
                    seeMoreTextBackground = Color.Transparent
                    if (isMoveSeeMore(
                            touchOffset = it,
                            textLayoutSize = seeMoreTextLayoutResult.size,
                            btnOffset = seeMoreOffset
                        )
                    ) {
                        fromSeeMoreClick = true
                        expanded = !expanded
                        isInitRender = false
                    }
                }
            )
        }
        .drawWithCache {
            contentMeasureSize = size
            onDrawBehind {
                withTransform({
                    clipRect(
                        left = 0F,
                        top = firstLineTop,
                        right = size.width,
                        bottom = noEllipsisLineBtm
                    )
                }) {
                    // 绘制正文，不包含最后一行文本内容
                    textLayoutResultState?.let {
                        drawText(it)
                    }
                }
                withTransform({
                    clipRect(
                        left = 0F,
                        top = ellipsisLineTop,
                        right = ellipsisClipRight,
                        bottom = expandHeight
                    )
                }) {
                    // 绘制正文，最后一行内容，不包含：省略号
                    textLayoutResultState?.let {
                        drawText(it)
                    }
                }
                if (!fromSeeMoreClick && collapseMinHeight < expandMaxHeight) { // 必须满足：最大行 > 默认的最小行
                    if (!expanded) {
                        // 绘制省略号
                        // 默认收起状态，才显示... 省略号，省略号和正文一个颜色，一个字体大小，和：展开、收起样式不同
                        drawText(
                            textLayoutResult = ellipsisTextLayoutResult,
                            topLeft = Offset(
                                // 需要计算最后一行内容的右侧距离,处理：换行符的情况
                                x = ellipsisLineLeftX,
                                y = expandHeight - ellipsisTextLayoutResult.size.height
                            )
                        )
                    }
                    // 绘制：”展开“ 或者 ”收起“
                    drawText(
                        textMeasurer = seeMoreTextMeasure,
                        text = if (expanded) collapseStateText else expandStateText,
                        style = textStyle.copy(
                            color = Color.Blue,
                            background = seeMoreTextBackground,
                            fontWeight = FontWeight.Medium
                        ),
                        topLeft = seeMoreOffset
                    )
                }
            }
        }
    )
}

/**
 * 刷新：查看更多按钮的背景
 */
private fun refreshSeeMoreBtnBg(
    touchOffset: Offset,
    textLayoutResult: TextLayoutResult,
    btnOffset: Offset
):Color {
    return if (isMoveSeeMore(
            touchOffset = touchOffset,
            textLayoutSize = textLayoutResult.size,
            btnOffset = btnOffset
        )
    ) Color.LightGray else Color.Transparent
}

/**
 * 是否在:查看更多按钮上面移动
 */
private fun isMoveSeeMore(touchOffset: Offset, textLayoutSize: IntSize, btnOffset: Offset): Boolean {
    val touchSizeWidth = textLayoutSize.width
    val touchSizeHeight = textLayoutSize.height
    return touchOffset.x >= btnOffset.x - touchSizeWidth
            && touchOffset.x <= btnOffset.x + touchSizeWidth
            && touchOffset.y >= btnOffset.y - touchSizeHeight
            && touchOffset.y <= btnOffset.y + touchSizeHeight
}

private  val SizeValueSaver = listSaver(
    save = { listOf(it.width, it.height) },
    restore = { Size(it[0], it[1]) }
)

private val OffsetValueSaver = listSaver(
    save = { listOf(it.x, it.y) },
    restore = { Offset(it[0], it[1]) }
)

private suspend fun PointerInputScope.detectTransformGestures(
    onPress: ((Offset) -> Unit),
    onClick: ((Offset) -> Unit)
) {
    detectTouchGestures(
        onDown = onPress,
        onMove = onPress,
        onUp = onClick
    )
}