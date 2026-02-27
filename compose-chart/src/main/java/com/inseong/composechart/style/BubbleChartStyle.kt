package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Style configuration for the bubble chart.
 *
 * @param minBubbleRadius Minimum bubble radius
 * @param maxBubbleRadius Maximum bubble radius
 * @param bubbleAlpha Bubble fill opacity (0.0 to 1.0)
 * @param showTooltipOnTouch Whether to show tooltip on touch
 * @param animationDurationMs Duration of the entry animation in milliseconds
 * @param chart Common chart style
 * @param axis Axis label style
 * @param grid Grid line style
 * @param tooltip Tooltip style
 */
data class BubbleChartStyle(
    val minBubbleRadius: Dp = 4.dp,
    val maxBubbleRadius: Dp = 30.dp,
    val bubbleAlpha: Float = 0.7f,
    val showTooltipOnTouch: Boolean = true,
    val animationDurationMs: Int = 600,
    val chart: ChartStyle = ChartStyle(),
    val axis: AxisStyle = AxisStyle(),
    val grid: GridStyle = GridStyle(),
    val tooltip: TooltipStyle = TooltipStyle(),
)
