package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * Individual bar data for the bar chart.
 *
 * A single value in [values] creates a simple bar; multiple values create a stacked bar.
 * Negative and NaN values are automatically clamped to 0.
 *
 * @param values List of bar values. For stacked bars, values are stacked from bottom to top.
 * @param label Bar label (shown in tooltip)
 * @param colors Colors for each stack segment. Auto-assigned from default palette when empty.
 */
data class BarEntry(
    val values: List<Float>,
    val label: String = "",
    val colors: List<Color> = emptyList(),
) {
    /** Safe values with negative/NaN/Infinity clamped to 0 */
    internal val safeValues: List<Float>
        get() = values.map { if (it.isFinite()) it.coerceAtLeast(0f) else 0f }
}

/**
 * Group data for the bar chart.
 *
 * A single entry creates a simple bar; multiple entries create grouped (side-by-side) bars.
 *
 * @param entries List of bars in the group
 * @param label Group label (shown on X-axis)
 */
data class BarGroup(
    val entries: List<BarEntry>,
    val label: String = "",
)

/**
 * Complete data passed to the bar chart.
 *
 * Displays an empty screen if groups is empty or has no valid values.
 *
 * @param groups List of bar groups
 */
data class BarChartData(
    val groups: List<BarGroup>,
) {
    companion object {
        /**
         * Convenience factory to create a simple bar chart from values and labels.
         *
         * ```kotlin
         * BarChartData.simple(
         *     values = listOf(30f, 45f, 28f),
         *     labels = listOf("Jan", "Feb", "Mar"),
         * )
         * ```
         */
        fun simple(
            values: List<Float>,
            labels: List<String> = emptyList(),
        ): BarChartData = BarChartData(
            groups = values.mapIndexed { index, value ->
                BarGroup(
                    entries = listOf(BarEntry(values = listOf(value))),
                    label = labels.getOrElse(index) { "" },
                )
            },
        )

        /**
         * Creates a grouped bar chart where each series is shown side-by-side.
         *
         * ```kotlin
         * BarChartData.grouped(
         *     seriesValues = listOf(
         *         listOf(10f, 20f, 30f), // Series A across 3 groups
         *         listOf(15f, 25f, 35f), // Series B across 3 groups
         *     ),
         *     labels = listOf("Jan", "Feb", "Mar"),
         * )
         * ```
         *
         * @param seriesValues Each inner list is one series' values across groups
         * @param labels Group labels for the X-axis
         * @param seriesColors Per-series color lists
         */
        fun grouped(
            seriesValues: List<List<Float>>,
            labels: List<String> = emptyList(),
            seriesColors: List<List<Color>> = emptyList(),
        ): BarChartData {
            val groupCount = seriesValues.firstOrNull()?.size ?: 0
            return BarChartData(
                groups = (0 until groupCount).map { g ->
                    BarGroup(
                        entries = seriesValues.mapIndexed { s, series ->
                            BarEntry(
                                values = listOf(series.getOrElse(g) { 0f }),
                                colors = seriesColors.getOrElse(s) { emptyList() },
                            )
                        },
                        label = labels.getOrElse(g) { "" },
                    )
                },
            )
        }
    }
}
