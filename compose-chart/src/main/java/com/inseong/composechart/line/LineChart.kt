package com.inseong.composechart.line

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
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

private class LineSeriesLayout(
    val seriesIndex: Int,
    val color: Color,
    val points: List<Offset>,
    val path: Path,
    val xPositions: FloatArray,
    val xPositionsSortedAscending: Boolean,
)

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
 * @param data Data to display. Series with empty points are skipped; rendering is a no-op if all
 *   series are empty.
 * @param modifier Layout Modifier. A finite size must be provided — the chart will not size itself.
 * @param style Chart style (curve mode, dots, grid, axis, tooltip, animation duration).
 * @param colors Palette used for series without an explicit color. Cycles when more series than
 *   colors. See [ChartDefaults.colors] for the default palette.
 * @param zoomState Optional zoom/pan state. Pass [com.inseong.composechart.rememberChartZoomState]
 *   to enable pinch-to-zoom and pan gestures.
 * @param accessibilityLabel Prefix used in the chart's semantics `contentDescription`. Default
 *   `"선 차트"`; override when localizing or using a more specific label.
 * @param onClickLabel Screen reader click action label. When non-null, an onClick semantics node
 *   is added so TalkBack announces the action. null hides the click action.
 * @param onSelectionChanged Callback emitting [ChartSelection.Line] on data point touch. Prefer
 *   this over [onPointSelected] for a signature shared across all charts.
 * @param onPointSelected Positional-argument callback kept for source compatibility. Will be
 *   removed in v2.0 — migrate to [onSelectionChanged].
 *
 * @see ChartSelection.Line
 * @see ChartZoomState
 * @see com.inseong.composechart.ChartCaptureState
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
    val density = LocalDensity.current
    var chartSize by remember { mutableStateOf(IntSize.Zero) }

    // Animation progress (0 -> 1)
    val progress by rememberChartAnimation(style.animationDurationMs, animationKey = data)

    // Touch state
    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    // Filter valid series only (series with points)
    val validSeries = remember(data) { data.series.filter { it.points.isNotEmpty() } }
    if (validSeries.isEmpty()) return

    // Calculate data range (safeX/safeY guard against NaN/Infinity)
    val allPoints = remember(validSeries) { validSeries.flatMap { it.points } }
    val xyRange = remember(allPoints, style.axis.yAxisMin, style.axis.yAxisMax) {
        ChartMath.calculateXYRange(
            xValues = allPoints.map { it.safeX },
            yValues = allPoints.map { it.safeY },
            yAxisMin = style.axis.yAxisMin,
            yAxisMax = style.axis.yAxisMax,
        )
    }

    val chartPaddingPx = style.chart.chartPadding
    val chartArea = remember(
        chartSize,
        chartPaddingPx,
        resolvedAxisStyle.showYAxis,
        resolvedAxisStyle.showXAxis,
        density,
    ) {
        with(density) {
            val paddingPx = chartPaddingPx.toPx()
            val yAxisWidth = if (resolvedAxisStyle.showYAxis) 40.dp.toPx() else 0f
            val xAxisHeight = if (resolvedAxisStyle.showXAxis) 20.dp.toPx() else 0f
            Rect(
                left = paddingPx + yAxisWidth,
                top = paddingPx,
                right = chartSize.width.toFloat() - paddingPx,
                bottom = chartSize.height.toFloat() - paddingPx - xAxisHeight,
            )
        }
    }

    LaunchedEffect(
        zoomState,
        chartArea.width,
        chartArea.height,
        zoomState?.scale,
        zoomState?.offsetX,
        zoomState?.offsetY,
    ) {
        if (chartArea.width > 0f && chartArea.height > 0f) {
            zoomState?.clampOffset(chartArea.width, chartArea.height)
        }
    }

    val zScale = zoomState?.scale ?: 1f
    val zOffsetX = zoomState?.offsetX ?: 0f
    val zOffsetY = zoomState?.offsetY ?: 0f

    val lineLayouts = remember(validSeries, xyRange, chartArea, style.curved, colors) {
        if (chartArea.width <= 0f || chartArea.height <= 0f) {
            emptyList()
        } else {
            validSeries.mapIndexed { seriesIndex, series ->
                val seriesColor = if (series.color == Color.Unspecified) {
                    colors[seriesIndex % colors.size]
                } else {
                    series.color
                }
                val mappedPoints = series.points.map { point ->
                    val (cx, cy) = ChartMath.mapToCanvas(
                        dataX = point.safeX,
                        dataY = point.safeY,
                        range = xyRange,
                        chartLeft = chartArea.left,
                        chartBottom = chartArea.bottom,
                        chartWidth = chartArea.width,
                        chartHeight = chartArea.height,
                    )
                    Offset(cx, cy)
                }
                LineSeriesLayout(
                    seriesIndex = seriesIndex,
                    color = seriesColor,
                    points = mappedPoints,
                    path = if (style.curved) mappedPoints.toBezierPath() else mappedPoints.toLinearPath(),
                    xPositions = FloatArray(mappedPoints.size) { index -> mappedPoints[index].x },
                    xPositionsSortedAscending = series.points.hasAscendingSafeXValues(),
                )
            }
        }
    }

    val currentTouch = remember(touchOffset, zoomState, zScale, zOffsetX, zOffsetY) {
        touchOffset?.let { touch ->
            if (zoomState != null && zoomState.isZoomed) {
                Offset(
                    (touch.x - zOffsetX) / zScale,
                    (touch.y - zOffsetY) / zScale,
                )
            } else {
                touch
            }
        }
    }

    val selectedLineSelections = remember(currentTouch, lineLayouts, validSeries, style.showTooltipOnTouch) {
        if (currentTouch == null || !style.showTooltipOnTouch) {
            emptyList()
        } else {
            lineLayouts.mapNotNull { layout ->
                val nearestIndex = findNearestPointIndex(
                    touchX = currentTouch.x,
                    pointXPositions = layout.xPositions,
                    sortedAscending = layout.xPositionsSortedAscending,
                )
                val dataPoint = validSeries[layout.seriesIndex].points.getOrNull(nearestIndex)
                dataPoint?.let { ChartSelection.Line(layout.seriesIndex, nearestIndex, it) }
            }
        }
    }

    val selectedPointSummary = remember(selectedLineSelections, data.xLabels) {
        selectedLineSelections.firstOrNull { it.seriesIndex == 0 }?.let { selection ->
            val xLabel = data.xLabels.getOrNull(selection.pointIndex).orEmpty()
            val labelPrefix = if (xLabel.isNotEmpty()) "$xLabel, " else ""
            "선택된 포인트: ${labelPrefix}값 ${ChartDefaults.formatSemanticsValue(selection.point.y)}"
        }
    }

    val currentOnPointSelected by rememberUpdatedState(onPointSelected)
    val currentOnSelectionChanged by rememberUpdatedState(onSelectionChanged)
    LaunchedEffect(selectedLineSelections) {
        selectedLineSelections.forEach { selection ->
            currentOnPointSelected?.invoke(selection.seriesIndex, selection.pointIndex, selection.point)
            currentOnSelectionChanged?.invoke(selection)
        }
    }

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
            .onSizeChanged { chartSize = it }
            .then(touchModifier),
    ) {
        if (chartArea.width <= 0f || chartArea.height <= 0f) return@Canvas

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
                val clipRight = chartArea.left + chartArea.width * progress
                val lineWidthPx = style.lineWidth.toPx()
                val dotRadiusPx = style.dotRadius.toPx()
                lineLayouts.forEach { layout ->
                    clipRect(
                        left = chartArea.left,
                        top = chartArea.top,
                        right = clipRight,
                        bottom = chartArea.bottom,
                    ) {
                        // Gradient area fill
                        if (style.gradientFill && layout.points.size >= 2) {
                            drawGradientFill(
                                linePath = layout.path,
                                color = layout.color,
                                alpha = style.gradientAlpha,
                                bottomY = chartArea.bottom,
                                startX = layout.points.first().x,
                                endX = layout.points.last().x,
                            )
                        }

                        // Draw line
                        drawPath(
                            path = layout.path,
                            color = layout.color,
                            style = Stroke(width = lineWidthPx),
                        )

                        // Draw data point dots
                        if (style.showDots) {
                            layout.points.forEach { point ->
                                drawCircle(
                                    color = layout.color,
                                    radius = dotRadiusPx,
                                    center = point,
                                )
                            }
                        }
                    }
                }

                selectedLineSelections.firstOrNull { it.seriesIndex == 0 }?.let { selection ->
                    lineLayouts.getOrNull(selection.seriesIndex)?.points?.getOrNull(selection.pointIndex)?.let { point ->
                        drawVerticalIndicatorLine(
                            x = point.x,
                            topY = chartArea.top,
                            bottomY = chartArea.bottom,
                        )
                    }
                }

                selectedLineSelections.forEach { selection ->
                    val layout = lineLayouts.getOrNull(selection.seriesIndex) ?: return@forEach
                    val nearestPoint = layout.points.getOrNull(selection.pointIndex) ?: return@forEach
                    val tooltipText = selection.point.label.ifEmpty {
                        ChartMath.formatValue(selection.point.y)
                    }
                    drawTooltip(
                        position = nearestPoint,
                        text = tooltipText,
                        style = style.tooltip,
                        lineColor = layout.color,
                        canvasSize = size,
                    )
                }
            }
        }
    }
}

private fun List<ChartPoint>.hasAscendingSafeXValues(): Boolean {
    for (index in 1 until size) {
        if (this[index].safeX < this[index - 1].safeX) return false
    }
    return true
}
