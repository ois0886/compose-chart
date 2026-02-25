package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Style configuration for the line chart.
 *
 * Defaults: bezier curves, gradient fill,
 * tooltip on touch, smooth draw-in animation.
 *
 * @param lineWidth Line thickness
 * @param curved Whether to apply bezier curve smoothing (false = straight lines)
 * @param showDots Whether to show dots at data points
 * @param dotRadius Dot radius
 * @param gradientFill Whether to apply gradient fill below the line
 * @param gradientAlpha Maximum opacity of the gradient fill (0.0 to 1.0)
 * @param showTooltipOnTouch Whether to show tooltip on touch
 * @param animationDurationMs Duration of the draw-in animation in milliseconds
 * @param chart Common chart style
 * @param axis Axis label style
 * @param grid Grid line style
 * @param tooltip Tooltip style
 */
data class LineChartStyle(
    val lineWidth: Dp = 2.dp,
    val curved: Boolean = true,
    val showDots: Boolean = false,
    val dotRadius: Dp = 4.dp,
    val gradientFill: Boolean = true,
    val gradientAlpha: Float = 0.15f,
    val showTooltipOnTouch: Boolean = true,
    val animationDurationMs: Int = 800,
    val chart: ChartStyle = ChartStyle(),
    val axis: AxisStyle = AxisStyle(),
    val grid: GridStyle = GridStyle(),
    val tooltip: TooltipStyle = TooltipStyle(),
)
