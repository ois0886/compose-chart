package com.inseong.composechart.line

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.ChartSelection
import com.inseong.composechart.ChartZoomState
import com.inseong.composechart.data.ChartPoint
import com.inseong.composechart.data.LineChartData
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.math.ChartMath
import com.inseong.composechart.internal.canvas.drawGradientFill
import com.inseong.composechart.internal.canvas.drawGrid
import com.inseong.composechart.internal.canvas.drawTooltip
import com.inseong.composechart.internal.canvas.drawVerticalIndicatorLine
import com.inseong.composechart.internal.canvas.drawXAxisLabels
import com.inseong.composechart.internal.canvas.drawYAxisLabels
import com.inseong.composechart.internal.canvas.toBezierPath
import com.inseong.composechart.internal.canvas.toLinearPath
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.internal.touch.chartTouchHandlerWithZoom
import com.inseong.composechart.internal.touch.findNearestPointIndex
import com.inseong.composechart.style.LineChartStyle

/**
 * Line chart Composable.
 *
 * Displays one or more data series as smooth curves (or straight lines),
 * with gradient area fill, entry animation, and touch interaction.
 *
 * Basic usage:
 * ```kotlin
 * LineChart(
 *     data = LineChartData(
 *         series = listOf(
 *             LineSeries(
 *                 points = listOf(
 *                     ChartPoint(0f, 10f, "Jan"),
 *                     ChartPoint(1f, 25f, "Feb"),
 *                     ChartPoint(2f, 18f, "Mar"),
 *                 )
 *             )
 *         ),
 *         xLabels = listOf("Jan", "Feb", "Mar"),
 *     ),
 *     modifier = Modifier.fillMaxWidth().height(200.dp),
 * )
 * ```
 *
 * @param data Data to display in the chart
 * @param modifier Layout Modifier (size must be specified)
 * @param style Chart style configuration
 * @param colors Series color palette. Used in order when no color is specified per series.
 * @param zoomState Optional zoom/pan state. Pass [rememberChartZoomState] to enable pinch-to-zoom and pan.
 * @param accessibilityLabel Prefix used in the chart's semantics contentDescription.
 * @param onClickLabel Screen reader click action label. When non-null, a click action semantics
 *   node is added so assistive tech can announce the action.
 * @param onSelectionChanged Callback emitting [ChartSelection.Line] on data point touch.
 *   Prefer this over [onPointSelected] for a callback signature shared across all charts.
 * @param onPointSelected Positional-argument callback kept for source compatibility.
 *   Will be removed in v2.0 — migrate to [onSelectionChanged].
 */
@Composable
fun LineChart(
    data: LineChartData,
    modifier: Modifier = Modifier,
    style: LineChartStyle = LineChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    zoomState: ChartZoomState? = null,
    accessibilityLabel: String = "선 차트",
    onClickLabel: String? = null,
    onSelectionChanged: ((ChartSelection.Line) -> Unit)? = null,
    onPointSelected: ((seriesIndex: Int, pointIndex: Int, point: ChartPoint) -> Unit)? = null,
) {
    // Detect dark theme and resolve styles
    val isDark = isSystemInDarkTheme()
    val resolvedGridStyle = style.grid.copy(
        lineColor = ChartDefaults.resolveGridLineColor(style.grid.lineColor, isDark),
    )
    val resolvedAxisStyle = style.axis.copy(
        labelColor = ChartDefaults.resolveAxisLabelColor(style.axis.labelColor, isDark),
    )

    // Animation progress (0 -> 1)
    val progress by rememberChartAnimation(style.animationDurationMs, animationKey = data)

    // Touch state
    var touchOffset by remember { mutableStateOf<Offset?>(null) }
    var selectedPointSummary by remember { mutableStateOf<String?>(null) }

    // Filter valid series only (series with points)
    val validSeries = remember(data) { data.series.filter { it.points.isNotEmpty() } }
    if (validSeries.isEmpty()) return

    // Calculate data range (safeX/safeY guard against NaN/Infinity)
    val allPoints = validSeries.flatMap { it.points }
    val xyRange = ChartMath.calculateXYRange(
        xValues = allPoints.map { it.safeX },
        yValues = allPoints.map { it.safeY },
        yAxisMin = style.axis.yAxisMin,
        yAxisMax = style.axis.yAxisMax,
    )

    val chartPaddingPx = style.chart.chartPadding

    val accessibilityDescription = buildString {
        append("$accessibilityLabel, ${validSeries.size}개 시리즈, ${allPoints.size}개 데이터 포인트")
        if (data.xLabels.isNotEmpty()) {
            append(", ${data.xLabels.size}개 X축 라벨")
        }
    }

    val touchModifier = if (zoomState != null) {
        Modifier.chartTouchHandlerWithZoom(zoomState = zoomState, onTouch = { offset -> touchOffset = offset })
    } else {
        Modifier.chartTouchHandler(onTouch = { offset -> touchOffset = offset })
    }

    Canvas(
        modifier = modifier
            .semantics {
                contentDescription = accessibilityDescription
                stateDescription = selectedPointSummary ?: "선택된 포인트 없음"
                onClickLabel?.let { label -> onClick(label = label, action = null) }
            }
            .fillMaxWidth()
            .then(touchModifier),
    ) {
        val paddingPx = chartPaddingPx.toPx()

        // Y-axis label area width
        val yAxisWidth = if (resolvedAxisStyle.showYAxis) 40.dp.toPx() else 0f
        // X-axis label area height
        val xAxisHeight = if (resolvedAxisStyle.showXAxis) 20.dp.toPx() else 0f

        // Chart data area (excluding axis labels and padding)
        val chartArea = Rect(
            left = paddingPx + yAxisWidth,
            top = paddingPx,
            right = size.width - paddingPx,
            bottom = size.height - paddingPx - xAxisHeight,
        )

        // Draw grid
        drawGrid(resolvedGridStyle, chartArea, resolvedAxisStyle.yLabelCount)

        // Draw Y-axis labels
        if (resolvedAxisStyle.showYAxis) {
            drawYAxisLabels(xyRange.adjustedMinY, xyRange.adjustedMaxY, resolvedAxisStyle, chartArea)
        }

        // Draw X-axis labels
        if (resolvedAxisStyle.showXAxis && data.xLabels.isNotEmpty()) {
            drawXAxisLabels(data.xLabels, resolvedAxisStyle, chartArea)
        }

        if (chartArea.width <= 0f || chartArea.height <= 0f) return@Canvas

        // Clamp zoom offsets
        zoomState?.clampOffset(chartArea.width, chartArea.height)
        val zScale = zoomState?.scale ?: 1f
        val zOffsetX = zoomState?.offsetX ?: 0f
        val zOffsetY = zoomState?.offsetY ?: 0f

        // Inverse-transform touch coordinates for accurate hit testing under zoom
        val currentTouch = touchOffset?.let { touch ->
            if (zoomState != null && zoomState.isZoomed) {
                Offset(
                    (touch.x - zOffsetX) / zScale,
                    (touch.y - zOffsetY) / zScale,
                )
            } else {
                touch
            }
        }
        if (currentTouch == null) selectedPointSummary = null

        // Clip to chart area and apply zoom transform
        clipRect(
            left = chartArea.left,
            top = chartArea.top,
            right = chartArea.right,
            bottom = chartArea.bottom,
        ) {
            withTransform({
                translate(zOffsetX, zOffsetY)
                scale(zScale, zScale, Offset(chartArea.left, chartArea.top))
            }) {
                // Draw each series
                validSeries.forEachIndexed { seriesIndex, series ->

                    // Determine series color
                    val seriesColor = if (series.color == Color.Unspecified) {
                        colors[seriesIndex % colors.size]
                    } else {
                        series.color
                    }

                    // Map data points to canvas coordinates (using safeX/safeY)
                    val mappedPoints = series.points.map { point ->
                        val (cx, cy) = ChartMath.mapToCanvas(
                            dataX = point.safeX, dataY = point.safeY,
                            range = xyRange,
                            chartLeft = chartArea.left, chartBottom = chartArea.bottom,
                            chartWidth = chartArea.width, chartHeight = chartArea.height,
                        )
                        Offset(cx, cy)
                    }

                    // Create line path (curved or straight)
                    val linePath = if (style.curved) {
                        mappedPoints.toBezierPath()
                    } else {
                        mappedPoints.toLinearPath()
                    }

                    // Animation: clip left-to-right based on progress
                    val clipRight = chartArea.left + chartArea.width * progress

                    clipRect(
                        left = chartArea.left,
                        top = chartArea.top,
                        right = clipRight,
                        bottom = chartArea.bottom,
                    ) {
                        // Gradient area fill
                        if (style.gradientFill && mappedPoints.size >= 2) {
                            drawGradientFill(
                                linePath = linePath,
                                color = seriesColor,
                                alpha = style.gradientAlpha,
                                bottomY = chartArea.bottom,
                                startX = mappedPoints.first().x,
                                endX = mappedPoints.last().x,
                            )
                        }

                        // Draw line
                        drawPath(
                            path = linePath,
                            color = seriesColor,
                            style = Stroke(width = style.lineWidth.toPx()),
                        )

                        // Draw data point dots
                        if (style.showDots) {
                            mappedPoints.forEach { point ->
                                drawCircle(
                                    color = seriesColor,
                                    radius = style.dotRadius.toPx(),
                                    center = point,
                                )
                            }
                        }
                    }

                    // Touch interaction handling
                    if (currentTouch != null && style.showTooltipOnTouch) {
                        val pointXPositions = mappedPoints.map { it.x }
                        val nearestIndex = findNearestPointIndex(currentTouch.x, pointXPositions)

                        if (nearestIndex >= 0 && nearestIndex < mappedPoints.size) {
                            val nearestPoint = mappedPoints[nearestIndex]
                            val dataPoint = series.points[nearestIndex]
                            if (seriesIndex == 0) {
                                val xLabel = data.xLabels.getOrNull(nearestIndex).orEmpty()
                                val labelPrefix = if (xLabel.isNotEmpty()) "$xLabel, " else ""
                                selectedPointSummary =
                                    "선택된 포인트: ${labelPrefix}값 ${ChartDefaults.formatSemanticsValue(dataPoint.y)}"
                            }

                            // Draw vertical indicator line only for the first series (avoid duplicates)
                            if (seriesIndex == 0) {
                                drawVerticalIndicatorLine(
                                    x = nearestPoint.x,
                                    topY = chartArea.top,
                                    bottomY = chartArea.bottom,
                                )
                            }

                            // Determine tooltip text
                            val tooltipText = dataPoint.label.ifEmpty {
                                ChartMath.formatValue(dataPoint.y)
                            }

                            // Draw tooltip
                            drawTooltip(
                                position = nearestPoint,
                                text = tooltipText,
                                style = style.tooltip,
                                lineColor = seriesColor,
                                canvasSize = size,
                            )

                            // Invoke callbacks (deprecated + new sealed-type variant)
                            onPointSelected?.invoke(seriesIndex, nearestIndex, dataPoint)
                            onSelectionChanged?.invoke(
                                ChartSelection.Line(seriesIndex, nearestIndex, dataPoint),
                            )
                        }
                    }
                }
            }
        }
    }
}
