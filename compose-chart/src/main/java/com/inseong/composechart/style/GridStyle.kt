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
 * @param dashPattern Dash pattern (null = solid line). e.g. floatArrayOf(10f, 5f)
 */
data class GridStyle(
    val showHorizontalLines: Boolean = true,
    val showVerticalLines: Boolean = false,
    val lineColor: Color = Color.Unspecified,
    val strokeWidth: Dp = 0.5.dp,
    val dashPattern: FloatArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GridStyle) return false
        return showHorizontalLines == other.showHorizontalLines &&
            showVerticalLines == other.showVerticalLines &&
            lineColor == other.lineColor &&
            strokeWidth == other.strokeWidth &&
            dashPattern.contentEquals(other.dashPattern)
    }

    override fun hashCode(): Int {
        var result = showHorizontalLines.hashCode()
        result = 31 * result + showVerticalLines.hashCode()
        result = 31 * result + lineColor.hashCode()
        result = 31 * result + strokeWidth.hashCode()
        result = 31 * result + (dashPattern?.contentHashCode() ?: 0)
        return result
    }
}
