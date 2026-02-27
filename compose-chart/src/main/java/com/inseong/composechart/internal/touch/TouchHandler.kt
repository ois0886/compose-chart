package com.inseong.composechart.internal.touch

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

/**
 * Modifier extension for handling chart touch interactions.
 *
 * Detects tap and horizontal drag (scrubbing) gestures and passes the touch position via callback.
 * Vertical drags are not consumed, allowing parent scroll containers to work normally.
 * Passes null when the touch ends.
 *
 * @param onTouch Touch position [Offset] or null on touch end
 */
internal fun Modifier.chartTouchHandler(
    onTouch: (Offset?) -> Unit,
): Modifier = this
    .pointerInput(Unit) {
        detectTapGestures(
            onPress = { offset ->
                onTouch(offset)
                tryAwaitRelease()
                onTouch(null)
            },
        )
    }
    .pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragStart = { offset -> onTouch(Offset(offset.x, offset.y)) },
            onHorizontalDrag = { change, _ ->
                change.consume()
                onTouch(change.position)
            },
            onDragEnd = { onTouch(null) },
            onDragCancel = { onTouch(null) },
        )
    }

/**
 * Finds the index of the nearest data point to the touch X coordinate.
 *
 * @param touchX Touch X coordinate (pixels)
 * @param pointXPositions X coordinate list of each data point (pixels)
 * @return Index of the nearest point. Returns -1 if the list is empty.
 */
internal fun findNearestPointIndex(touchX: Float, pointXPositions: List<Float>): Int {
    if (pointXPositions.isEmpty()) return -1
    var minDistance = Float.MAX_VALUE
    var nearestIndex = 0
    pointXPositions.forEachIndexed { index, x ->
        val distance = abs(touchX - x)
        if (distance < minDistance) {
            minDistance = distance
            nearestIndex = index
        }
    }
    return nearestIndex
}
