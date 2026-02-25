package com.inseong.composechart.internal.canvas

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.inseong.composechart.style.TooltipStyle

/**
 * Draws a tooltip bubble above the touched data point.
 *
 * The tooltip is displayed as a rounded rectangle above the data point,
 * with automatic position adjustment to stay within canvas bounds.
 *
 * @param position Canvas coordinates of the data point
 * @param text Text to display in the tooltip
 * @param style Tooltip style configuration
 * @param lineColor Series color (used for indicator border)
 * @param canvasSize Total canvas size (for bounds clamping)
 */
internal fun DrawScope.drawTooltip(
    position: Offset,
    text: String,
    style: TooltipStyle,
    lineColor: Color,
    canvasSize: Size,
) {
    val paddingH = style.paddingHorizontal.toPx()
    val paddingV = style.paddingVertical.toPx()
    val cornerRadius = style.cornerRadius.toPx()
    val textSizePx = style.textSize.toPx()

    // Measure text size
    val textPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = textSizePx
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    val textWidth = textPaint.measureText(text)
    val textHeight = textSizePx

    // Clamp bubble size to not exceed canvas
    val bubbleWidth = (textWidth + paddingH * 2).coerceAtMost(canvasSize.width)
    val bubbleHeight = (textHeight + paddingV * 2).coerceAtMost(canvasSize.height)
    val arrowHeight = 6f

    // Skip tooltip if canvas is too small
    if (canvasSize.width < textSizePx || canvasSize.height < textSizePx) return

    // Default position: above the point
    var bubbleLeft = position.x - bubbleWidth / 2
    var bubbleTop = position.y - bubbleHeight - arrowHeight - style.indicatorRadius.toPx() - 4f

    // Canvas bounds adjustment
    if (bubbleLeft < 0f) bubbleLeft = 0f
    if (bubbleLeft + bubbleWidth > canvasSize.width) {
        bubbleLeft = (canvasSize.width - bubbleWidth).coerceAtLeast(0f)
    }
    if (bubbleTop < 0f) {
        // Show below when no space above
        bubbleTop = position.y + style.indicatorRadius.toPx() + arrowHeight + 4f
    }
    // Bottom bounds adjustment
    if (bubbleTop + bubbleHeight > canvasSize.height) {
        bubbleTop = (canvasSize.height - bubbleHeight).coerceAtLeast(0f)
    }

    // Draw tooltip background
    val bubblePath = Path().apply {
        addRoundRect(
            RoundRect(
                left = bubbleLeft,
                top = bubbleTop,
                right = bubbleLeft + bubbleWidth,
                bottom = bubbleTop + bubbleHeight,
                cornerRadius = CornerRadius(cornerRadius),
            )
        )
    }
    drawPath(path = bubblePath, color = style.backgroundColor, style = Fill)

    // Draw tooltip text
    drawContext.canvas.nativeCanvas.drawText(
        text,
        bubbleLeft + paddingH,
        bubbleTop + paddingV + textHeight * 0.85f, // Baseline adjustment
        textPaint,
    )

    // Draw data point indicator (dot)
    val indicatorBorderColor = if (style.indicatorBorderColor == Color.Unspecified) {
        lineColor
    } else {
        style.indicatorBorderColor
    }

    // Outer circle (border)
    drawCircle(
        color = indicatorBorderColor,
        radius = style.indicatorRadius.toPx() + style.indicatorBorderWidth.toPx(),
        center = position,
    )
    // Inner circle (fill)
    drawCircle(
        color = style.indicatorColor,
        radius = style.indicatorRadius.toPx(),
        center = position,
    )
}

/**
 * Draws a vertical indicator line spanning the full chart area at the touch point.
 *
 * @param x X coordinate of the vertical line
 * @param topY Top Y coordinate of the vertical line
 * @param bottomY Bottom Y coordinate of the vertical line
 * @param color Vertical line color
 */
internal fun DrawScope.drawVerticalIndicatorLine(
    x: Float,
    topY: Float,
    bottomY: Float,
    color: Color = Color(0xFFDDDDDD),
) {
    drawLine(
        color = color,
        start = Offset(x, topY),
        end = Offset(x, bottomY),
        strokeWidth = 1f,
    )
}
