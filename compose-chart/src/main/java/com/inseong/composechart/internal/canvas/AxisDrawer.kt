package com.inseong.composechart.internal.canvas

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.inseong.composechart.style.AxisStyle

/**
 * Draws X-axis labels below the chart area.
 *
 * When [groupWidth] and [groupSpacing] are provided (> 0), each label is centered
 * under its corresponding bar group. Otherwise, labels are evenly distributed
 * across the chart width (edge-to-edge), suitable for line charts.
 *
 * @param labels List of label texts to display
 * @param style Axis style configuration
 * @param chartArea Chart data area (labels are drawn below this area)
 * @param groupWidth Width of each bar group (0 for edge-to-edge distribution)
 * @param groupSpacing Spacing between bar groups (0 for edge-to-edge distribution)
 */
internal fun DrawScope.drawXAxisLabels(
    labels: List<String>,
    style: AxisStyle,
    chartArea: Rect,
    groupWidth: Float = 0f,
    groupSpacing: Float = 0f,
) {
    if (labels.isEmpty()) return
    // Hide labels when chart area is too narrow
    if (chartArea.width < 80f * density) return

    val paint = Paint().apply {
        color = style.labelColor.hashCode()
        textSize = style.labelSize.toPx()
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }

    val y = chartArea.bottom + style.labelSize.toPx() + 8f

    if (labels.size == 1) {
        drawContext.canvas.nativeCanvas.drawText(
            labels[0],
            chartArea.center.x,
            y,
            paint,
        )
        return
    }

    if (groupWidth > 0f) {
        // Center each label under its bar group
        labels.forEachIndexed { index, label ->
            val x = chartArea.left + index * (groupWidth + groupSpacing) + groupWidth / 2
            drawContext.canvas.nativeCanvas.drawText(label, x, y, paint)
        }
    } else {
        // Edge-to-edge distribution (for line charts)
        val step = chartArea.width / (labels.size - 1)
        labels.forEachIndexed { index, label ->
            val x = chartArea.left + step * index
            drawContext.canvas.nativeCanvas.drawText(label, x, y, paint)
        }
    }
}

/**
 * Draws Y-axis reference labels to the left of the chart area.
 *
 * Divides the range between min and max values into equal intervals.
 *
 * @param minValue Y-axis minimum value
 * @param maxValue Y-axis maximum value
 * @param style Axis style configuration
 * @param chartArea Chart data area (labels are drawn to the left of this area)
 */
internal fun DrawScope.drawYAxisLabels(
    minValue: Float,
    maxValue: Float,
    style: AxisStyle,
    chartArea: Rect,
) {
    if (style.yLabelCount <= 0) return
    // Hide labels when chart area is too short
    if (chartArea.height < 80f * density) return

    val paint = Paint().apply {
        color = style.labelColor.hashCode()
        textSize = style.labelSize.toPx()
        textAlign = Paint.Align.RIGHT
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }

    val range = maxValue - minValue
    val step = chartArea.height / style.yLabelCount
    val valueStep = range / style.yLabelCount

    for (i in 0..style.yLabelCount) {
        val yPos = chartArea.bottom - step * i
        val value = minValue + valueStep * i
        // Display as integer if possible, otherwise one decimal place
        val text = if (value == value.toLong().toFloat()) {
            value.toLong().toString()
        } else {
            String.format("%.1f", value)
        }
        drawContext.canvas.nativeCanvas.drawText(
            text,
            chartArea.left - 8f,
            yPos + style.labelSize.toPx() / 3, // Vertical centering adjustment
            paint,
        )
    }
}
