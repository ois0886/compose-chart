package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Style configuration for the gauge/progress chart.
 *
 * Use [sweepAngle] to switch between circular progress (360) and semicircular gauge (240).
 *
 * @param trackColor Color of the background track (unfilled area). Uses theme-aware default when [Color.Unspecified].
 * @param progressColor Color of the filled area.
 * @param strokeWidth Thickness of the gauge arc.
 * @param roundCap Whether to round the arc endpoints.
 * @param sweepAngle Total angle of the arc. 360 for circular, 240 for open-bottom gauge.
 * @param showCenterText Whether to show value/label text at the center.
 * @param centerTextSize Size of the center value text.
 * @param centerTextColor Color of the center value text. Uses theme-aware default when [Color.Unspecified].
 * @param animationDurationMs Duration of the fill animation in milliseconds.
 * @param chart Common chart style.
 */
data class GaugeChartStyle(
    val trackColor: Color = Color.Unspecified,
    val progressColor: Color = Color(0xFF3182F6),
    val strokeWidth: Dp = 12.dp,
    val roundCap: Boolean = true,
    val sweepAngle: Float = 240f,
    val showCenterText: Boolean = true,
    val centerTextSize: TextUnit = 24.sp,
    val centerTextColor: Color = Color.Unspecified,
    val animationDurationMs: Int = 1000,
    val chart: ChartStyle = ChartStyle(),
)
