package com.inseong.composechart.internal.touch

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import com.inseong.composechart.ChartZoomState
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
 * Modifier extension for chart touch with zoom/pan support.
 *
 * Handles tap (tooltip), pinch-to-zoom, drag-to-pan, and double-tap to reset.
 * Vertical drags are consumed when zoomed (for panning).
 *
 * @param zoomState State holder managing zoom scale and pan offsets
 * @param onTouch Touch position [Offset] or null on touch end
 */
internal fun Modifier.chartTouchHandlerWithZoom(
    zoomState: ChartZoomState,
    onTouch: (Offset?) -> Unit,
): Modifier = this
    .pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = { zoomState.reset() },
            onPress = { offset ->
                onTouch(offset)
                tryAwaitRelease()
                onTouch(null)
            },
        )
    }
    .pointerInput(Unit) {
        detectTransformGestures { centroid, pan, zoom, _ ->
            zoomState.applyZoom(zoom, centroid.x, centroid.y)
            if (zoomState.isZoomed) {
                zoomState.applyPan(pan.x, pan.y)
            }
        }
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

/**
 * Finds the index of the nearest data point using primitive X positions.
 *
 * Uses binary search when [sortedAscending] is true, and falls back to the
 * same forward linear scan semantics as [findNearestPointIndex] otherwise.
 */
internal fun findNearestPointIndex(
    touchX: Float,
    pointXPositions: FloatArray,
    sortedAscending: Boolean,
): Int {
    if (pointXPositions.isEmpty()) return -1
    if (!sortedAscending) return findNearestPointIndexLinear(touchX, pointXPositions)

    if (touchX <= pointXPositions.first()) return 0
    val lastIndex = pointXPositions.lastIndex
    if (touchX >= pointXPositions[lastIndex]) return lastIndex

    var low = 0
    var high = pointXPositions.size
    while (low < high) {
        val mid = (low + high) ushr 1
        if (pointXPositions[mid] < touchX) {
            low = mid + 1
        } else {
            high = mid
        }
    }

    val rightIndex = low
    val leftIndex = rightIndex - 1
    val leftDistance = abs(touchX - pointXPositions[leftIndex])
    val rightDistance = abs(pointXPositions[rightIndex] - touchX)
    return if (leftDistance <= rightDistance) leftIndex else rightIndex
}

private fun findNearestPointIndexLinear(
    touchX: Float,
    pointXPositions: FloatArray,
): Int {
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
