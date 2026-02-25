package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * Individual slice data for the donut chart.
 *
 * Negative and NaN values are automatically filtered out and not displayed.
 *
 * @param value Slice value (ratio is auto-calculated relative to total sum)
 * @param label Slice label (shown in legend, tooltip)
 * @param color Slice color. Auto-assigned from default palette when [Color.Unspecified].
 */
data class DonutSlice(
    val value: Float,
    val label: String = "",
    val color: Color = Color.Unspecified,
)

/**
 * Complete data passed to the donut chart.
 *
 * Displays an empty screen if slices is empty or has no valid values.
 *
 * @param slices List of slices
 */
data class DonutChartData(
    val slices: List<DonutSlice>,
) {
    companion object {
        /**
         * Convenience factory to create donut chart data from a label-value map.
         *
         * ```kotlin
         * DonutChartData.fromValues(
         *     values = mapOf("Food" to 40f, "Transport" to 25f, "Shopping" to 20f),
         * )
         * ```
         */
        fun fromValues(values: Map<String, Float>): DonutChartData = DonutChartData(
            slices = values.map { (label, value) -> DonutSlice(value = value, label = label) },
        )
    }
}
