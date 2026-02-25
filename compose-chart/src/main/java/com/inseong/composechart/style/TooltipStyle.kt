package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Style configuration for the tooltip bubble shown on touch.
 *
 * @param backgroundColor Tooltip background color (dark shades recommended)
 * @param textColor Tooltip text color
 * @param textSize Tooltip text size
 * @param cornerRadius Tooltip corner radius
 * @param paddingHorizontal Horizontal padding inside the tooltip
 * @param paddingVertical Vertical padding inside the tooltip
 * @param indicatorRadius Radius of the data point indicator (circle dot)
 * @param indicatorColor Fill color of the indicator
 * @param indicatorBorderColor Border color of the indicator. Uses line color when [Color.Unspecified].
 * @param indicatorBorderWidth Border width of the indicator
 */
data class TooltipStyle(
    val backgroundColor: Color = Color(0xFF333333),
    val textColor: Color = Color.White,
    val textSize: TextUnit = 12.sp,
    val cornerRadius: Dp = 8.dp,
    val paddingHorizontal: Dp = 12.dp,
    val paddingVertical: Dp = 6.dp,
    val indicatorRadius: Dp = 5.dp,
    val indicatorColor: Color = Color.White,
    val indicatorBorderColor: Color = Color.Unspecified,
    val indicatorBorderWidth: Dp = 2.dp,
)
