package com.inseong.composechart.internal.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.inseong.composechart.style.GridStyle

/**
 * Draws grid lines within the chart area.
 *
 * By default, only thin gray horizontal grid lines are shown.
 *
 * @param style Grid style configuration
 * @param chartArea Chart data area (excluding padding)
 * @param horizontalCount Number of horizontal grid line intervals
 */
internal fun DrawScope.drawGrid(
    style: GridStyle,
    chartArea: Rect,
    horizontalCount: Int,
) {
    val strokeWidthPx = style.strokeWidth.toPx()
    val pathEffect = style.dashPattern?.let {
        PathEffect.dashPathEffect(it, 0f)
    }

    // Horizontal grid lines
    if (style.showHorizontalLines && horizontalCount > 0) {
        val step = chartArea.height / horizontalCount
        for (i in 0..horizontalCount) {
            val y = chartArea.top + step * i
            drawLine(
                color = style.lineColor,
                start = Offset(chartArea.left, y),
                end = Offset(chartArea.right, y),
                strokeWidth = strokeWidthPx,
                pathEffect = pathEffect,
            )
        }
    }

    // Vertical grid lines
    if (style.showVerticalLines && horizontalCount > 0) {
        val step = chartArea.width / horizontalCount
        for (i in 0..horizontalCount) {
            val x = chartArea.left + step * i
            drawLine(
                color = style.lineColor,
                start = Offset(x, chartArea.top),
                end = Offset(x, chartArea.bottom),
                strokeWidth = strokeWidthPx,
                pathEffect = pathEffect,
            )
        }
    }
}
