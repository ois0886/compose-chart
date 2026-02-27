package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * Individual data point for the scatter chart.
 *
 * NaN and Infinity values are automatically clamped to 0f.
 *
 * @param x X-axis value
 * @param y Y-axis value
 * @param label Label for this point (shown in tooltip, optional)
 * @param color Point color. Auto-assigned from default palette when [Color.Unspecified].
 */
data class ScatterPoint(
    val x: Float,
    val y: Float,
    val label: String = "",
    val color: Color = Color.Unspecified,
) {
    internal val safeX: Float get() = if (x.isFinite()) x else 0f
    internal val safeY: Float get() = if (y.isFinite()) y else 0f
}

/**
 * Complete data passed to the scatter chart.
 *
 * Displays an empty screen if points is empty or has no valid values.
 *
 * @param points List of scatter points
 * @param xLabels Labels to display below the X-axis (optional)
 */
data class ScatterChartData(
    val points: List<ScatterPoint>,
    val xLabels: List<String> = emptyList(),
) {
    companion object {
        /**
         * Convenience factory to create scatter chart data from X and Y value lists.
         *
         * ```kotlin
         * ScatterChartData.fromValues(
         *     xValues = listOf(1f, 2f, 3f),
         *     yValues = listOf(10f, 25f, 18f),
         * )
         * ```
         */
        fun fromValues(
            xValues: List<Float>,
            yValues: List<Float>,
            labels: List<String> = emptyList(),
            xLabels: List<String> = emptyList(),
        ): ScatterChartData = ScatterChartData(
            points = xValues.zip(yValues).mapIndexed { index, (x, y) ->
                ScatterPoint(x = x, y = y, label = labels.getOrElse(index) { "" })
            },
            xLabels = xLabels,
        )
    }
}
