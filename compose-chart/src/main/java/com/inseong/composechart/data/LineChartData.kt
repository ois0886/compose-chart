package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * Individual series (line) data for the line chart.
 *
 * Adding multiple series to a single line chart
 * creates a multi-line chart.
 *
 * @param points List of data points composing the series. Ignored if empty.
 * @param label Series name (used in legends)
 * @param color Series color. Auto-assigned from default palette when [Color.Unspecified].
 */
data class LineSeries(
    val points: List<ChartPoint>,
    val label: String = "",
    val color: Color = Color.Unspecified,
)

/**
 * Complete data passed to the line chart.
 *
 * Displays an empty screen if series is empty or has no valid points.
 *
 * @param series List of [LineSeries]
 * @param xLabels Labels to display below the X-axis (optional)
 */
data class LineChartData(
    val series: List<LineSeries>,
    val xLabels: List<String> = emptyList(),
) {
    companion object {
        /**
         * Convenience factory for a single-series line chart.
         *
         * ```kotlin
         * LineChartData.single(
         *     points = listOf(ChartPoint(0f, 10f), ChartPoint(1f, 25f)),
         *     xLabels = listOf("Jan", "Feb"),
         * )
         * ```
         */
        fun single(
            points: List<ChartPoint>,
            xLabels: List<String> = emptyList(),
            label: String = "",
            color: Color = Color.Unspecified,
        ): LineChartData = LineChartData(
            series = listOf(LineSeries(points = points, label = label, color = color)),
            xLabels = xLabels,
        )

        /**
         * Creates a line chart from a list of Y values.
         * X values are auto-assigned as indices starting from 0.
         *
         * ```kotlin
         * LineChartData.fromValues(
         *     values = listOf(10f, 25f, 18f, 32f),
         *     xLabels = listOf("Jan", "Feb", "Mar", "Apr"),
         * )
         * ```
         */
        fun fromValues(
            values: List<Float>,
            xLabels: List<String> = emptyList(),
            label: String = "",
            color: Color = Color.Unspecified,
        ): LineChartData {
            val points = values.mapIndexed { index, y ->
                ChartPoint(x = index.toFloat(), y = y)
            }
            return single(points = points, xLabels = xLabels, label = label, color = color)
        }
    }
}
