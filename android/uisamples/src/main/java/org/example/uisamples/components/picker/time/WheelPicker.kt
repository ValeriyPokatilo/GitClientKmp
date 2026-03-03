package org.example.uisamples.components.picker.time

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.example.uisamples.themes.GgcTheme
import kotlin.math.absoluteValue

private const val MAX_TEXT_ALPHA = 1.2f
private const val MIN_TEXT_ALPHA = 0.2f
private const val ITEM_ROTATION_X = -20f

@Composable
fun WheelPicker(
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    count: Int,
    rowCount: Int,
    size: DpSize = DpSize(128.dp, 128.dp),
    onScrollFinished: (snappedIndex: Int) -> Int? = { null },
    content: @Composable LazyItemScope.(index: Int) -> Unit,
) {
    val lazyListState: LazyListState = rememberLazyListState(startIndex)
    val isScrollInProgress: Boolean = lazyListState.isScrollInProgress

    LaunchedEffect(isScrollInProgress, count) {
        if (!isScrollInProgress) {
            onScrollFinished(calculateSnappedItemIndex(lazyListState))?.let {
                lazyListState.scrollToItem(it)
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .height(size.height)
                .width(size.width)
                .nestedScroll(object : NestedScrollConnection {}),
            state = lazyListState,
            contentPadding = PaddingValues(vertical = size.height / rowCount * ((rowCount - 1) / 2)),
            flingBehavior = rememberSnapFlingBehavior(
                lazyListState = lazyListState
            )
        ) {
            items(count) { index ->
                val rotationX = calculateAnimatedRotationX(
                    lazyListState = lazyListState,
                    index = index,
                    rowCount = rowCount
                )
                Box(
                    modifier = Modifier
                        .height(size.height / rowCount)
                        .width(size.width)
                        .alpha(
                            calculateAnimatedAlpha(
                                lazyListState = lazyListState,
                                index = index,
                                rowCount = rowCount
                            )
                        )
                        .graphicsLayer {
                            this.rotationX = rotationX
                        },
                    contentAlignment = Alignment.Center
                ) {
                    content(index)
                }
            }
        }
    }
}

private fun calculateSnappedItemIndex(lazyListState: LazyListState): Int {
    val firstVisibleIndex: Int = lazyListState.firstVisibleItemIndex
    val firstVisibleOffset: Int = lazyListState.firstVisibleItemScrollOffset

    // Если первый элемент не полностью виден, проскроллить к следующему
    return if (firstVisibleOffset > 0) firstVisibleIndex + 1 else firstVisibleIndex
}

@Composable
private fun calculateAnimatedAlpha(
    lazyListState: LazyListState,
    index: Int,
    rowCount: Int
): Float {
    val layoutInfo: LazyListLayoutInfo by remember {
        derivedStateOf { lazyListState.layoutInfo }
    }
    val firstVisibleIndex: Int by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }
    val firstVisibleOffset: Int by remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }
    val viewportHeight: Float = layoutInfo.viewportSize.height.toFloat()
    val singleViewportHeight: Float = viewportHeight / rowCount

    val itemHeight = if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
        layoutInfo.visibleItemsInfo.first().size.toFloat()
    } else {
        singleViewportHeight
    }

    val distanceToIndexSnap: Float =
        ((index - firstVisibleIndex) * itemHeight - firstVisibleOffset).absoluteValue

    return if (distanceToIndexSnap in 0f..singleViewportHeight) {
        MAX_TEXT_ALPHA - (distanceToIndexSnap / singleViewportHeight)
    } else {
        MIN_TEXT_ALPHA
    }
}

@Composable
private fun calculateAnimatedRotationX(
    lazyListState: LazyListState,
    index: Int,
    rowCount: Int
): Float {
    val layoutInfo: LazyListLayoutInfo by remember {
        derivedStateOf { lazyListState.layoutInfo }
    }
    val firstVisibleIndex: Int by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }
    val firstVisibleOffset: Int by remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }
    val viewportHeight: Float = layoutInfo.viewportSize.height.toFloat()
    val singleViewportHeight: Float = viewportHeight / rowCount

    val itemHeight: Float =
        layoutInfo.visibleItemsInfo.firstOrNull()?.size?.toFloat() ?: singleViewportHeight

    val distanceToIndexSnap: Float = ((index - firstVisibleIndex) * itemHeight - firstVisibleOffset)

    val animatedRotationX: Float = ITEM_ROTATION_X * (distanceToIndexSnap / singleViewportHeight)

    return animatedRotationX.takeUnless { it.isNaN() } ?: 0f
}

@Composable
@PreviewLightDark
private fun WheelPickerPreview() {
    GgcTheme {
        WheelPicker(
            modifier = Modifier,
            startIndex = 1,
            count = 12,
            rowCount = 1,
        ) {}
    }
}
