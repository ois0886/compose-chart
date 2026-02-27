package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Style configuration for the scatter chart.
 *
 * @param dotRadius Dot radius for each data point
 * @param showTooltipOnTouch Whether to show tooltip on touch
 * @param animationDurationMs Duration of the entry animation in milliseconds
 * @param chart Common chart style
 * @param axis Axis label style
 * @param grid Grid line style
 * @param tooltip Tooltip style
 */
data class ScatterChartStyle(
    val dotRadius: Dp = 5.dp,
    val showTooltipOnTouch: Boolean = true,
    val animationDurationMs: Int = 600,
    val chart: ChartStyle = ChartStyle(),
    val axis: AxisStyle = AxisStyle(),
    val grid: GridStyle = GridStyle(),
    val tooltip: TooltipStyle = TooltipStyle(),
)
