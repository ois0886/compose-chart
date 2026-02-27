package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * Individual series data for the radar chart.
 *
 * Each entry represents one polygon on the radar chart.
 * NaN and Infinity values are automatically clamped to 0f.
 *
 * @param values List of values for each axis (must match axisLabels count)
 * @param label Series name (used in legends)
 * @param color Series color. Auto-assigned from default palette when [Color.Unspecified].
 */
data class RadarEntry(
    val values: List<Float>,
    val label: String = "",
    val color: Color = Color.Unspecified,
) {
    internal val safeValues: List<Float>
        get() = values.map { if (it.isFinite()) it.coerceAtLeast(0f) else 0f }
}

/**
 * Complete data passed to the radar chart.
 *
 * Displays an empty screen if entries is empty or axisLabels has fewer than 3 items.
 *
 * @param entries List of [RadarEntry] (each entry is one polygon)
 * @param axisLabels Labels for each axis vertex
 * @param maxValue Maximum value for the chart scale. Auto-calculated if 0 or negative.
 */
data class RadarChartData(
    val entries: List<RadarEntry>,
    val axisLabels: List<String>,
    val maxValue: Float = 0f,
) {
    companion object {
        /**
         * Convenience factory for a single-series radar chart.
         *
         * ```kotlin
         * RadarChartData.single(
         *     values = listOf(80f, 65f, 90f, 70f, 85f),
         *     axisLabels = listOf("STR", "DEX", "INT", "WIS", "CHA"),
         * )
         * ```
         */
        fun single(
            values: List<Float>,
            axisLabels: List<String>,
            label: String = "",
            maxValue: Float = 0f,
        ): RadarChartData = RadarChartData(
            entries = listOf(RadarEntry(values = values, label = label)),
            axisLabels = axisLabels,
            maxValue = maxValue,
        )
    }
}
