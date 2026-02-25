package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
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
 * @param yLabelCount Number of Y-axis reference labels (evenly divided)
 */
data class AxisStyle(
    val showXAxis: Boolean = true,
    val showYAxis: Boolean = false,
    val labelColor: Color = Color.Unspecified,
    val labelSize: TextUnit = 11.sp,
    val yLabelCount: Int = 5,
)
