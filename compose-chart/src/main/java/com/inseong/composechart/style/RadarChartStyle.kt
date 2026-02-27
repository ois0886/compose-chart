package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Style configuration for the radar chart.
 *
 * @param webLineColor Color of the web grid lines. Theme-aware when [Color.Unspecified].
 * @param webLineWidth Width of the web grid lines
 * @param fillAlpha Opacity of the filled polygon area (0.0 to 1.0)
 * @param showDots Whether to show dots at data vertices
 * @param dotRadius Dot radius at vertices
 * @param webLevels Number of concentric web levels
 * @param labelSize Text size for axis labels
 * @param labelColor Color of axis labels. Theme-aware when [Color.Unspecified].
 * @param animationDurationMs Duration of the entry animation in milliseconds
 * @param chart Common chart style
 */
data class RadarChartStyle(
    val webLineColor: Color = Color.Unspecified,
    val webLineWidth: Dp = 1.dp,
    val fillAlpha: Float = 0.25f,
    val showDots: Boolean = true,
    val dotRadius: Dp = 4.dp,
    val webLevels: Int = 5,
    val labelSize: TextUnit = 11.sp,
    val labelColor: Color = Color.Unspecified,
    val animationDurationMs: Int = 800,
    val chart: ChartStyle = ChartStyle(),
)
