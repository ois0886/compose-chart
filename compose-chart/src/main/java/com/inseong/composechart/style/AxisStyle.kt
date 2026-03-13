package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Style configuration for chart axis labels.
 *
 * Defaults: X-axis labels shown, no Y-axis labels.
 *
 * @param showXAxis Whether to show X-axis labels
 * @param showYAxis Whether to show Y-axis labels
 * @param labelColor Label text color. Uses theme-aware default when [Color.Unspecified].
 * @param labelSize Label text size
 * @param fontWeight Font weight for axis labels
 * @param yLabelCount Number of Y-axis reference labels (evenly divided)
 * @param yAxisFormatter Custom formatter for Y-axis labels. When null, default formatting is used.
 * @param yAxisMin Manual Y-axis minimum value override. null for auto-calculation.
 * @param yAxisMax Manual Y-axis maximum value override. null for auto-calculation.
 */
data class AxisStyle(
    val showXAxis: Boolean = true,
    val showYAxis: Boolean = false,
    val labelColor: Color = Color.Unspecified,
    val labelSize: TextUnit = 11.sp,
    val fontWeight: FontWeight = FontWeight.Normal,
    val yLabelCount: Int = 5,
    val yAxisFormatter: ((Float) -> String)? = null,
    val yAxisMin: Float? = null,
    val yAxisMax: Float? = null,
)
