package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Style configuration for chart grid lines.
 *
 * Defaults: horizontal grid only, thin gray solid lines.
 *
 * @param showHorizontalLines Whether to show horizontal grid lines
 * @param showVerticalLines Whether to show vertical grid lines
 * @param lineColor Grid line color. Uses theme-aware default when [Color.Unspecified].
 * @param strokeWidth Grid line thickness
 * @param dashPattern Dash pattern (null = solid line). e.g. listOf(10f, 5f)
 */
data class GridStyle(
    val showHorizontalLines: Boolean = true,
    val showVerticalLines: Boolean = false,
    val lineColor: Color = Color.Unspecified,
    val strokeWidth: Dp = 0.5.dp,
    val dashPattern: List<Float>? = null,
)
