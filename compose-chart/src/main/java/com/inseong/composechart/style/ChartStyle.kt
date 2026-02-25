package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Base style applied to all charts.
 *
 * @param backgroundColor Background color of the chart area
 * @param chartPadding Internal padding of the chart content area
 */
data class ChartStyle(
    val backgroundColor: Color = Color.Transparent,
    val chartPadding: Dp = 16.dp,
)
