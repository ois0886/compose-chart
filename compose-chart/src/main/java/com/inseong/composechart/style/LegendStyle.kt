package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Style configuration for [com.inseong.composechart.legend.ChartLegend].
 *
 * @param iconSize Size of the color indicator square
 * @param iconCornerRadius Corner radius of the color indicator
 * @param textSize Label text size
 * @param textColor Label text color. Uses theme-aware default when [Color.Unspecified].
 * @param spacing Spacing between legend items
 * @param iconTextSpacing Spacing between the color indicator and the label text
 * @param orientation Layout direction of legend items
 */
data class LegendStyle(
    val iconSize: Dp = 12.dp,
    val iconCornerRadius: Dp = 3.dp,
    val textSize: TextUnit = 12.sp,
    val textColor: Color = Color.Unspecified,
    val spacing: Dp = 16.dp,
    val iconTextSpacing: Dp = 6.dp,
    val orientation: LegendOrientation = LegendOrientation.HORIZONTAL,
)

/**
 * Layout orientation for legend items.
 */
enum class LegendOrientation {
    HORIZONTAL,
    VERTICAL,
}

/**
 * A single legend item representing a data series or category.
 *
 * @param color Color indicator for this item
 * @param label Display label
 */
data class LegendItem(
    val color: Color,
    val label: String,
)
