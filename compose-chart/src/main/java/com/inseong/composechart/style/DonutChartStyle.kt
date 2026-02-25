package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Style configuration for the donut chart.
 *
 * Use [holeRadius] to control the center hole size (0 = filled, 0.6 = donut).
 *
 * @param holeRadius Ratio of the center hole (0.0 = filled, 0.6 = donut). Range: 0.0 to 1.0.
 * @param sliceSpacing Spacing between slices
 * @param selectedScale Scale factor for the selected slice on touch (1.0 = no scale)
 * @param showLabels Whether to show slice labels
 * @param animationDurationMs Duration of the sweep animation in milliseconds
 * @param startAngle Start angle of the first slice (-90 = 12 o'clock)
 * @param chart Common chart style
 */
data class DonutChartStyle(
    val holeRadius: Float = 0f,
    val sliceSpacing: Dp = 2.dp,
    val selectedScale: Float = 1.05f,
    val showLabels: Boolean = true,
    val animationDurationMs: Int = 800,
    val startAngle: Float = -90f,
    val chart: ChartStyle = ChartStyle(),
)
