package com.inseong.composechart.data

/**
 * Data for the gauge/progress chart.
 *
 * Invalid values (negative, NaN, etc.) are safely clamped inside the chart.
 *
 * @param value Current value.
 * @param maxValue Maximum value.
 * @param label Label text displayed at the center (e.g., "Score", "Progress").
 */
data class GaugeChartData(
    val value: Float,
    val maxValue: Float,
    val label: String = "",
)
