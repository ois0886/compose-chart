package com.inseong.composechart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * State holder for chart zoom and pan interactions.
 *
 * Maintains the current scale and translation offsets.
 * Use [rememberChartZoomState] to create an instance.
 *
 * Usage:
 * ```kotlin
 * val zoomState = rememberChartZoomState()
 * LineChart(
 *     data = data,
 *     zoomState = zoomState,
 * )
 * ```
 *
 * @param initialScale Initial zoom scale
 * @param initialOffsetX Initial horizontal pan offset
 * @param initialOffsetY Initial vertical pan offset
 * @param minScale Minimum allowed zoom scale (default: 1x)
 * @param maxScale Maximum allowed zoom scale (default: 5x)
 */
@Stable
class ChartZoomState(
    initialScale: Float = 1f,
    initialOffsetX: Float = 0f,
    initialOffsetY: Float = 0f,
    val minScale: Float = 1f,
    val maxScale: Float = 5f,
) {
    var scale by mutableFloatStateOf(initialScale)
        internal set
    var offsetX by mutableFloatStateOf(initialOffsetX)
        internal set
    var offsetY by mutableFloatStateOf(initialOffsetY)
        internal set

    /** Whether the chart is currently zoomed in. */
    val isZoomed: Boolean get() = scale > minScale + 0.01f

    /** Reset zoom and pan to default. */
    fun reset() {
        scale = minScale
        offsetX = 0f
        offsetY = 0f
    }

    /**
     * Apply a scale change centered at the given point.
     */
    internal fun applyZoom(zoomChange: Float, centroidX: Float, centroidY: Float) {
        val oldScale = scale
        scale = (scale * zoomChange).coerceIn(minScale, maxScale)
        val scaleChange = scale / oldScale
        offsetX = (offsetX - centroidX) * scaleChange + centroidX
        offsetY = (offsetY - centroidY) * scaleChange + centroidY
    }

    /**
     * Apply a pan (translation) offset.
     */
    internal fun applyPan(panX: Float, panY: Float) {
        offsetX += panX
        offsetY += panY
    }

    /**
     * Clamp offsets to prevent panning beyond content bounds.
     */
    internal fun clampOffset(chartWidth: Float, chartHeight: Float) {
        val maxOffsetX = ((chartWidth * scale - chartWidth) / 2).coerceAtLeast(0f)
        val maxOffsetY = ((chartHeight * scale - chartHeight) / 2).coerceAtLeast(0f)
        offsetX = offsetX.coerceIn(-maxOffsetX, maxOffsetX)
        offsetY = offsetY.coerceIn(-maxOffsetY, maxOffsetY)
    }

    companion object {
        val Saver = Saver<ChartZoomState, List<Float>>(
            save = { listOf(it.scale, it.offsetX, it.offsetY, it.minScale, it.maxScale) },
            restore = { ChartZoomState(it[0], it[1], it[2], it[3], it[4]) },
        )
    }
}

/**
 * Creates and remembers a [ChartZoomState] that survives configuration changes.
 *
 * @param minScale Minimum zoom scale (default: 1x, no zoom out)
 * @param maxScale Maximum zoom scale (default: 5x)
 */
@Composable
fun rememberChartZoomState(
    minScale: Float = 1f,
    maxScale: Float = 5f,
): ChartZoomState = rememberSaveable(saver = ChartZoomState.Saver) {
    ChartZoomState(minScale = minScale, maxScale = maxScale)
}
