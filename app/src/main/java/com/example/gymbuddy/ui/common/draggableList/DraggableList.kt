package com.example.gymbuddy.ui.common.draggableList

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun <T> DraggableLazyColumn(
    items: SnapshotStateList<T>,
    onMove: (List<T>) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (item: T) -> Unit

) {
    val tempItems = remember { mutableStateListOf<T>() }

    LaunchedEffect(items) {
        tempItems.clear()
        tempItems.addAll(items)
    }

    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.testTag("draggableLazyColumn")
    ) {
        itemsIndexed(tempItems) { index, item ->
            val isBeingDragged = index == draggingIndex

            val animatedOffset by animateDpAsState(
                if (isBeingDragged) with(density) { offsetY.toDp() } else 0.dp,
                label = "softMoveOfItems"
            )
            val shadowElevation by animateDpAsState(
                if (isBeingDragged) 8.dp else 0.dp,
                label = "showShadow"
            )

            // ToDO: evtl hier noch das Item anders aussehen lassen. Passt aber erstmal

            val hapticFeedback = LocalHapticFeedback.current

            var itemHeightPx by remember { mutableFloatStateOf(0f) }

            Box(
                modifier = Modifier
                    // get actual height of the item
                    .onGloballyPositioned { coordinates ->
                        itemHeightPx = coordinates.size.height.toFloat()
                    }
                    // move the item -> only y axis
                    .offset { IntOffset(0, animatedOffset.roundToPx()) }
                    // put the dragged item on top
                    .zIndex(if (isBeingDragged) 1f else 0f)
                    // show shadow if dragged
                    .shadow(elevation = shadowElevation, shape = RoundedCornerShape(8.dp))
                    // detect the drag gesture
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                draggingIndex = index
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onDrag = { _, dragAmount ->
                                // move the item by the drag amount
                                offsetY += dragAmount.y

                                // calculate the target index
                                draggingIndex?.let { dragging ->
                                    val targetIndex = calculateTargetIndex(
                                        draggingIndex = dragging,
                                        dragOffset = offsetY,
                                        listSize = tempItems.size,
                                        itemHeightPx = itemHeightPx
                                    )
                                    // swap the items if dragged over another item
                                    if (targetIndex != dragging) {
                                        tempItems.swap(dragging, targetIndex)
                                        draggingIndex = targetIndex
                                        offsetY = 0f
                                    }
                                }
                            },
                            onDragEnd = {
                                draggingIndex = null
                                offsetY = 0f
                                onMove(tempItems)
                            },
                            onDragCancel = {
                                draggingIndex = null
                                offsetY = 0f
                            }
                        )
                    }
            ) {
                itemContent(
                    item
                )
            }
        }
    }
}

private fun calculateTargetIndex(
    draggingIndex: Int,
    dragOffset: Float,
    listSize: Int,
    itemHeightPx: Float
): Int {
    // calculate the number of dragged items
    val draggedItems = (dragOffset / itemHeightPx).toInt()
    // calculate the target index
    val targetIndex = draggingIndex + draggedItems
    // coerce the target index to the list bounds -> coerce means to limit the value to the given range
    return targetIndex.coerceIn(0, listSize - 1)
}

private fun <T> MutableList<T>.swap(from: Int, to: Int) {
    if (from != to) {
        val temp = this[from]
        this[from] = this[to]
        this[to] = temp
    }
}
