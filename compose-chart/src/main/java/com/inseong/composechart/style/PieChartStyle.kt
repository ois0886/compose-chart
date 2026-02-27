package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Style configuration for the pie chart.
 *
 * Pie chart is a filled circle chart (no center hole).
 * For a donut chart with a center hole, use [DonutChartStyle] directly.
 *
 * @param sliceSpacing Spacing between slices
 * @param selectedScale Scale factor for the selected slice on touch (1.0 = no scale)
 * @param showLabels Whether to show slice labels
 * @param animationDurationMs Duration of the sweep animation in milliseconds
 * @param startAngle Start angle of the first slice (-90 = 12 o'clock)
 * @param chart Common chart style
 */
data class PieChartStyle(
    val sliceSpacing: Dp = 2.dp,
    val selectedScale: Float = 1.05f,
    val showLabels: Boolean = true,
    val animationDurationMs: Int = 800,
    val startAngle: Float = -90f,
    val chart: ChartStyle = ChartStyle(),
)
