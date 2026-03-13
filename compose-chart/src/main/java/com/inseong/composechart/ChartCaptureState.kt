package com.inseong.composechart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer

/**
 * State holder for capturing a chart as an [ImageBitmap].
 *
 * Usage:
 * ```kotlin
 * val captureState = rememberChartCaptureState()
 *
 * LineChart(
 *     data = data,
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .height(200.dp)
 *         .chartCaptureModifier(captureState),
 * )
 *
 * // Capture the chart as a bitmap (suspend function)
 * val bitmap: ImageBitmap = captureState.capture()
 * ```
 *
 * @param graphicsLayer The [GraphicsLayer] used for offscreen rendering
 */
class ChartCaptureState(
    internal val graphicsLayer: GraphicsLayer,
) {
    /**
     * Captures the current chart content as an [ImageBitmap].
     *
     * Must be called after the chart has been composed and rendered at least once.
     *
     * @return The chart rendered as an [ImageBitmap]
     */
    suspend fun capture(): ImageBitmap = graphicsLayer.toImageBitmap()
}

/**
 * Creates and remembers a [ChartCaptureState] for chart image capture.
 *
 * The returned state can be used with [chartCaptureModifier] on any chart composable.
 */
@Composable
fun rememberChartCaptureState(): ChartCaptureState {
    val graphicsLayer = rememberGraphicsLayer()
    return remember(graphicsLayer) { ChartCaptureState(graphicsLayer) }
}

/**
 * Modifier that records the chart's draw commands into a [GraphicsLayer]
 * so that [ChartCaptureState.capture] can export it as an [ImageBitmap].
 *
 * Apply this modifier to any chart composable:
 * ```kotlin
 * LineChart(
 *     modifier = Modifier.chartCaptureModifier(captureState),
 * )
 * ```
 *
 * @param captureState The [ChartCaptureState] to record into
 */
fun Modifier.chartCaptureModifier(captureState: ChartCaptureState): Modifier =
    this.drawWithContent {
        captureState.graphicsLayer.record {
            this@drawWithContent.drawContent()
        }
        drawLayer(captureState.graphicsLayer)
    }
