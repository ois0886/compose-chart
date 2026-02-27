package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * Individual data point for the bubble chart.
 *
 * NaN and Infinity values are automatically clamped to 0f.
 *
 * @param x X-axis value
 * @param y Y-axis value
 * @param size Bubble size value (mapped to radius proportionally)
 * @param label Label for this point (shown in tooltip, optional)
 * @param color Bubble color. Auto-assigned from default palette when [Color.Unspecified].
 */
data class BubblePoint(
    val x: Float,
    val y: Float,
    val size: Float,
    val label: String = "",
    val color: Color = Color.Unspecified,
) {
    internal val safeX: Float get() = if (x.isFinite()) x else 0f
    internal val safeY: Float get() = if (y.isFinite()) y else 0f
    internal val safeSize: Float get() = if (size.isFinite()) size.coerceAtLeast(0f) else 0f
}

/**
 * Complete data passed to the bubble chart.
 *
 * Displays an empty screen if points is empty or has no valid values.
 *
 * @param points List of bubble points
 * @param xLabels Labels to display below the X-axis (optional)
 */
data class BubbleChartData(
    val points: List<BubblePoint>,
    val xLabels: List<String> = emptyList(),
) {
    companion object {
        /**
         * Convenience factory to create bubble chart data from value lists.
         *
         * ```kotlin
         * BubbleChartData.fromValues(
         *     xValues = listOf(1f, 2f, 3f),
         *     yValues = listOf(10f, 25f, 18f),
         *     sizes = listOf(5f, 15f, 10f),
         * )
         * ```
         */
        fun fromValues(
            xValues: List<Float>,
            yValues: List<Float>,
            sizes: List<Float>,
            labels: List<String> = emptyList(),
            xLabels: List<String> = emptyList(),
        ): BubbleChartData = BubbleChartData(
            points = xValues.zip(yValues).zip(sizes).mapIndexed { index, (xy, size) ->
                BubblePoint(
                    x = xy.first,
                    y = xy.second,
                    size = size,
                    label = labels.getOrElse(index) { "" },
                )
            },
            xLabels = xLabels,
        )
    }
}
