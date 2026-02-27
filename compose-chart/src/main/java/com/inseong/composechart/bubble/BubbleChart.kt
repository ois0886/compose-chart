package com.inseong.composechart.bubble

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.BubbleChartData
import com.inseong.composechart.data.BubblePoint
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.canvas.drawGrid
import com.inseong.composechart.internal.canvas.drawTooltip
import com.inseong.composechart.internal.canvas.drawVerticalIndicatorLine
import com.inseong.composechart.internal.canvas.drawXAxisLabels
import com.inseong.composechart.internal.canvas.drawYAxisLabels
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.style.BubbleChartStyle
import kotlin.math.sqrt

/**
 * Bubble chart Composable.
 *
 * Extends scatter chart with a size dimension, where each data point
 * is rendered as a circle whose radius is proportional to the size value.
 *
 * Basic usage:
 * ```kotlin
 * BubbleChart(
 *     data = BubbleChartData.fromValues(
 *         xValues = listOf(1f, 2f, 3f),
 *         yValues = listOf(10f, 25f, 18f),
 *         sizes = listOf(5f, 15f, 10f),
 *     ),
 *     modifier = Modifier.fillMaxWidth().height(200.dp),
 * )
 * ```
 *
 * @param data Data to display in the chart
 * @param modifier Layout Modifier (size must be specified)
 * @param style Chart style configuration
 * @param colors Bubble color palette. Used in order when no color is specified per point.
 * @param onBubbleSelected Callback on bubble touch. null for no callback.
 */
@Composable
fun BubbleChart(
    data: BubbleChartData,
    modifier: Modifier = Modifier,
    style: BubbleChartStyle = BubbleChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    onBubbleSelected: ((index: Int, point: BubblePoint) -> Unit)? = null,
) {
    val isDark = isSystemInDarkTheme()
    val resolvedGridStyle = style.grid.copy(
        lineColor = ChartDefaults.resolveGridLineColor(style.grid.lineColor, isDark),
    )
    val resolvedAxisStyle = style.axis.copy(
        labelColor = ChartDefaults.resolveAxisLabelColor(style.axis.labelColor, isDark),
    )

    val progress by rememberChartAnimation(style.animationDurationMs)

    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    val validPoints = remember(data) {
        data.points.filter { it.x.isFinite() && it.y.isFinite() && it.size.isFinite() }
    }
    if (validPoints.isEmpty()) return

    val minX = validPoints.minOf { it.safeX }
    val maxX = validPoints.maxOf { it.safeX }
    val minY = validPoints.minOf { it.safeY }
    val maxY = validPoints.maxOf { it.safeY }
    val yRange = (maxY - minY).let { if (it == 0f) 1f else it }
    val yPadding = yRange * 0.1f
    val adjustedMinY = minY - yPadding
    val adjustedMaxY = maxY + yPadding
    val xRange = (maxX - minX).let { if (it == 0f) 1f else it }

    val minSize = validPoints.minOf { it.safeSize }
    val maxSize = validPoints.maxOf { it.safeSize }
    val sizeRange = (maxSize - minSize).let { if (it == 0f) 1f else it }

    val chartPaddingPx = style.chart.chartPadding

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .chartTouchHandler { offset -> touchOffset = offset },
    ) {
        val paddingPx = chartPaddingPx.toPx()

        val yAxisWidth = if (resolvedAxisStyle.showYAxis) 40.dp.toPx() else 0f
        val xAxisHeight = if (resolvedAxisStyle.showXAxis) 20.dp.toPx() else 0f

        val chartArea = Rect(
            left = paddingPx + yAxisWidth,
            top = paddingPx,
            right = size.width - paddingPx,
            bottom = size.height - paddingPx - xAxisHeight,
        )

        drawGrid(resolvedGridStyle, chartArea, resolvedAxisStyle.yLabelCount)

        if (resolvedAxisStyle.showYAxis) {
            drawYAxisLabels(adjustedMinY, adjustedMaxY, resolvedAxisStyle, chartArea)
        }

        if (resolvedAxisStyle.showXAxis && data.xLabels.isNotEmpty()) {
            drawXAxisLabels(data.xLabels, resolvedAxisStyle, chartArea)
        }

        if (chartArea.width <= 0f || chartArea.height <= 0f) return@Canvas

        val minRadiusPx = style.minBubbleRadius.toPx()
        val maxRadiusPx = style.maxBubbleRadius.toPx()

        // Map points to canvas coordinates and calculate bubble radii
        val mappedPoints = validPoints.map { point ->
            Offset(
                x = chartArea.left + ((point.safeX - minX) / xRange) * chartArea.width,
                y = chartArea.bottom - ((point.safeY - adjustedMinY) / (adjustedMaxY - adjustedMinY)) * chartArea.height,
            )
        }

        val bubbleRadii = validPoints.map { point ->
            minRadiusPx + ((point.safeSize - minSize) / sizeRange) * (maxRadiusPx - minRadiusPx)
        }

        // Draw bubbles with animation
        mappedPoints.forEachIndexed { index, center ->
            val bubbleColor = if (validPoints[index].color == Color.Unspecified) {
                colors[index % colors.size]
            } else {
                validPoints[index].color
            }

            drawCircle(
                color = bubbleColor,
                radius = bubbleRadii[index] * progress,
                center = center,
                alpha = style.bubbleAlpha,
            )
        }

        // Touch interaction
        val currentTouch = touchOffset
        if (currentTouch != null && style.showTooltipOnTouch) {
            var nearestIndex = -1
            var minDistance = Float.MAX_VALUE

            mappedPoints.forEachIndexed { index, point ->
                val dx = currentTouch.x - point.x
                val dy = currentTouch.y - point.y
                val distance = sqrt(dx * dx + dy * dy)
                if (distance < minDistance) {
                    minDistance = distance
                    nearestIndex = index
                }
            }

            if (nearestIndex >= 0) {
                val nearestPoint = mappedPoints[nearestIndex]
                val dataPoint = validPoints[nearestIndex]

                drawVerticalIndicatorLine(
                    x = nearestPoint.x,
                    topY = chartArea.top,
                    bottomY = chartArea.bottom,
                )

                val tooltipText = dataPoint.label.ifEmpty {
                    val yText = if (dataPoint.y == dataPoint.y.toLong().toFloat()) {
                        dataPoint.y.toLong().toString()
                    } else {
                        String.format("%.1f", dataPoint.y)
                    }
                    yText
                }

                drawTooltip(
                    position = nearestPoint,
                    text = tooltipText,
                    style = style.tooltip,
                    lineColor = if (dataPoint.color == Color.Unspecified) {
                        colors[nearestIndex % colors.size]
                    } else {
                        dataPoint.color
                    },
                    canvasSize = size,
                )

                onBubbleSelected?.invoke(nearestIndex, dataPoint)
            }
        }
    }
}
