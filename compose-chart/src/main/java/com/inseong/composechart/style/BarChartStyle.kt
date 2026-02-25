package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Style configuration for the bar chart.
 *
 * Defaults: rounded top corners, vertical orientation,
 * touch highlight, smooth growth animation.
 *
 * @param cornerRadius Corner radius for bar tops (vertical) or right edges (horizontal)
 * @param barSpacing Spacing between bars within the same group
 * @param groupSpacing Spacing between groups
 * @param horizontal Whether to render as horizontal bar chart (true = left-to-right growth)
 * @param animationDurationMs Duration of the growth animation in milliseconds
 * @param highlightOnTouch Whether to highlight the selected bar on touch
 * @param highlightAlpha Opacity of unselected bars when highlighting
 * @param chart Common chart style
 * @param axis Axis label style
 * @param grid Grid line style
 * @param tooltip Tooltip style
 */
data class BarChartStyle(
    val cornerRadius: Dp = 6.dp,
    val barSpacing: Dp = 4.dp,
    val groupSpacing: Dp = 12.dp,
    val horizontal: Boolean = false,
    val animationDurationMs: Int = 600,
    val highlightOnTouch: Boolean = true,
    val highlightAlpha: Float = 0.3f,
    val chart: ChartStyle = ChartStyle(),
    val axis: AxisStyle = AxisStyle(),
    val grid: GridStyle = GridStyle(),
    val tooltip: TooltipStyle = TooltipStyle(),
)
