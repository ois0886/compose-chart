package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
 * @param fontWeight Font weight for legend labels
 * @param spacing Spacing between legend items
 * @param iconTextSpacing Spacing between the color indicator and the label text
 * @param orientation Layout direction of legend items
 * @param disabledAlpha Alpha value applied to disabled legend items
 */
data class LegendStyle(
    val iconSize: Dp = 12.dp,
    val iconCornerRadius: Dp = 3.dp,
    val textSize: TextUnit = 12.sp,
    val textColor: Color = Color.Unspecified,
    val fontWeight: FontWeight = FontWeight.Normal,
    val spacing: Dp = 16.dp,
    val iconTextSpacing: Dp = 6.dp,
    val orientation: LegendOrientation = LegendOrientation.HORIZONTAL,
    val disabledAlpha: Float = 0.3f,
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
 * @param enabled Whether this item is active. Disabled items are shown with reduced alpha.
 */
data class LegendItem(
    val color: Color,
    val label: String,
    val enabled: Boolean = true,
)
