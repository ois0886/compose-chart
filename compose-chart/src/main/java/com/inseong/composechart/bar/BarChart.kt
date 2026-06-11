package com.inseong.composechart.bar

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.ChartSelection
import com.inseong.composechart.ChartZoomState
import com.inseong.composechart.data.BarEntry
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.math.BarMath
import com.inseong.composechart.internal.math.ChartMath
import com.inseong.composechart.internal.canvas.drawGrid
import com.inseong.composechart.internal.canvas.drawTooltip
import com.inseong.composechart.internal.canvas.drawXAxisLabels
import com.inseong.composechart.internal.canvas.drawYAxisLabels
import com.inseong.composechart.internal.canvas.rememberAxisLabelPaint
import com.inseong.composechart.internal.canvas.rememberTooltipTextPaint
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.internal.touch.chartTouchHandlerWithZoom
import com.inseong.composechart.style.BarChartStyle

private class BarGroupLayout(
    val groupIndex: Int,
    val label: String,
    val groupLeft: Float,
    val barWidth: Float,
    val entries: List<BarEntryLayout>,
)

private class BarEntryLayout(
    val entryIndex: Int,
    val entry: BarEntry,
    val barLeft: Float,
    val segmentColors: List<Color>,
    val totalValue: Float,
    val tooltipLineColor: Color,
)

/**
 * Bar chart Composable.
 *
 * Supports single bars, grouped bars (side-by-side), and stacked bars,
 * with vertical/horizontal orientation, rounded corners, growth animation,
 * and touch highlighting.
 *
 * Basic usage:
 * ```kotlin
 * BarChart(
 *     data = BarChartData(
 *         groups = listOf(
 *             BarGroup(
 *                 entries = listOf(BarEntry(values = listOf(30f))),
 *                 label = "Jan",
 *             ),
 *             BarGroup(
 *                 entries = listOf(BarEntry(values = listOf(45f))),
 *                 label = "Feb",
 *             ),
 *         ),
 *     ),
 *     modifier = Modifier.fillMaxWidth().height(200.dp),
 * )
 * ```
 *
 * @param data Data to display. Groups without entries are skipped; rendering is a no-op if all
 *   groups are empty.
 * @param modifier Layout Modifier. A finite size must be provided — the chart will not size itself.
 * @param style Chart style (orientation, bar corner radius, grouping, grid, axis, tooltip,
 *   animation duration, highlight alpha).
 * @param colors Palette for bar segments. See [ChartDefaults.colors] for the default palette.
 * @param zoomState Optional zoom/pan state. Pass [com.inseong.composechart.rememberChartZoomState]
 *   to enable pinch-to-zoom and pan gestures.
 * @param accessibilityLabel Prefix used in the chart's semantics `contentDescription`. Default
 *   `"막대 차트"`; override when localizing or using a more specific label.
 * @param onClickLabel Screen reader click action label. When non-null, an onClick semantics node
 *   is added so TalkBack announces the action.
 * @param onSelectionChanged Callback emitting [ChartSelection.Bar] on bar touch. Prefer this over
 *   [onBarSelected] for a signature shared across all charts.
 * @param onBarSelected Positional-argument callback kept for source compatibility. Will be
 *   removed in v2.0 — migrate to [onSelectionChanged].
 *
 * @see ChartSelection.Bar
 * @see ChartZoomState
 * @see com.inseong.composechart.ChartCaptureState
 */
@Composable
fun BarChart(
    data: BarChartData,
    modifier: Modifier = Modifier,
    style: BarChartStyle = BarChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    zoomState: ChartZoomState? = null,
    accessibilityLabel: String = "막대 차트",
    onClickLabel: String? = null,
    onSelectionChanged: ((ChartSelection.Bar) -> Unit)? = null,
    onBarSelected: ((groupIndex: Int, entryIndex: Int, stackIndex: Int) -> Unit)? = null,
) {
    // Detect dark theme and resolve styles
    val isDark = isSystemInDarkTheme()
    val resolvedGridStyle = style.grid.copy(
        lineColor = ChartDefaults.resolveGridLineColor(style.grid.lineColor, isDark),
    )
    val resolvedAxisStyle = style.axis.copy(
        labelColor = ChartDefaults.resolveAxisLabelColor(style.axis.labelColor, isDark),
    )
    val xAxisLabelPaint = rememberAxisLabelPaint(resolvedAxisStyle, Paint.Align.CENTER)
    val yAxisLabelPaint = rememberAxisLabelPaint(resolvedAxisStyle, Paint.Align.RIGHT)
    val tooltipTextPaint = rememberTooltipTextPaint(style.tooltip)
    val density = LocalDensity.current
    var chartSize by remember { mutableStateOf(IntSize.Zero) }

    val progress by rememberChartAnimation(style.animationDurationMs, animationKey = data)
    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    // Filter valid groups (only groups with entries)
    val validGroups = remember(data) {
        data.groups.filter { it.entries.isNotEmpty() }
    }
    if (validGroups.isEmpty()) return

    // Calculate data range (safeValues guard against NaN/negative)
    val maxValue = remember(validGroups) {
        validGroups.maxOf { group ->
            group.entries.maxOfOrNull { entry ->
                entry.safeTotal
            } ?: 0f
        }
    }
    val adjustedMax = remember(maxValue, style.axis.yAxisMax) {
        style.axis.yAxisMax?.coerceAtLeast(maxValue) ?: BarMath.calculateAdjustedMax(maxValue)
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
    val groupCount = validGroups.size
    val groupSpacingPx = with(density) { style.groupSpacing.toPx() }
    val barSpacingPx = with(density) { style.barSpacing.toPx() }
    val groupWidth = remember(chartArea.width, groupCount, groupSpacingPx) {
        BarMath.calculateGroupWidth(chartArea.width, groupCount, groupSpacingPx)
    }
    val groupLabels = remember(validGroups) { validGroups.map { it.label } }
    val barLayouts = remember(validGroups, chartArea, groupWidth, groupSpacingPx, barSpacingPx, colors) {
        if (chartArea.width <= 0f || chartArea.height <= 0f) {
            emptyList()
        } else {
            validGroups.mapIndexed { groupIndex, group ->
                val groupLeft = chartArea.left + groupIndex * (groupWidth + groupSpacingPx)
                val entryCount = group.entries.size
                val barWidth = BarMath.calculateBarWidth(groupWidth, entryCount, barSpacingPx)
                BarGroupLayout(
                    groupIndex = groupIndex,
                    label = group.label,
                    groupLeft = groupLeft,
                    barWidth = barWidth,
                    entries = group.entries.mapIndexed { entryIndex, entry ->
                        BarEntryLayout(
                            entryIndex = entryIndex,
                            entry = entry,
                            barLeft = groupLeft + entryIndex * (barWidth + barSpacingPx),
                            segmentColors = entry.colors.ifEmpty { colors },
                            totalValue = entry.safeTotal,
                            tooltipLineColor = colors[entryIndex % colors.size],
                        )
                    },
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

    val selectedBarSelection = remember(
        currentTouch,
        validGroups,
        chartArea,
        groupWidth,
        groupSpacingPx,
        barSpacingPx,
    ) {
        if (currentTouch == null || chartArea.width <= 0f || chartArea.height <= 0f) {
            null
        } else {
            val groupIndex = BarMath.findTouchedGroupIndex(
                touchX = currentTouch.x,
                chartLeft = chartArea.left,
                groupWidth = groupWidth,
                groupSpacingPx = groupSpacingPx,
                groupCount = groupCount,
            )
            val groupLayout = barLayouts.getOrNull(groupIndex)
            val entryCount = groupLayout?.entries?.size ?: 0
            if (groupLayout == null || entryCount == 0) {
                null
            } else {
                val entryIndex = if (entryCount > 1) {
                    BarMath.findTouchedEntryIndex(
                        touchX = currentTouch.x,
                        groupLeft = groupLayout.groupLeft,
                        barWidth = groupLayout.barWidth,
                        barSpacingPx = barSpacingPx,
                        entryCount = entryCount,
                    )
                } else {
                    0
                }
                ChartSelection.Bar(groupIndex, entryIndex, 0)
            }
        }
    }
    val selectedGroupIndex = selectedBarSelection?.groupIndex ?: -1
    val selectedEntryIndex = selectedBarSelection?.entryIndex ?: -1

    val accessibilityDescription = "$accessibilityLabel, ${validGroups.size}개 그룹"
    val selectionDescription = barLayouts.getOrNull(selectedGroupIndex)?.let { group ->
        val entry = group.entries.getOrNull(selectedEntryIndex.coerceAtLeast(0))
        val label = group.label.takeIf { it.isNotEmpty() } ?: "${selectedGroupIndex + 1}번 그룹"
        if (entry != null) {
            "선택된 그룹: $label, 값 ${ChartDefaults.formatSemanticsValue(entry.totalValue)}"
        } else {
            "선택된 그룹: $label"
        }
    } ?: "선택된 그룹 없음"

    val touchCallback: (Offset?) -> Unit = { offset ->
        touchOffset = offset
    }
    val touchModifier = if (zoomState != null) {
        Modifier.chartTouchHandlerWithZoom(zoomState = zoomState, onTouch = touchCallback)
    } else {
        Modifier.chartTouchHandler(onTouch = touchCallback)
    }

    val currentOnBarSelected by rememberUpdatedState(onBarSelected)
    val currentOnSelectionChanged by rememberUpdatedState(onSelectionChanged)
    LaunchedEffect(selectedBarSelection) {
        selectedBarSelection?.let { selection ->
            currentOnBarSelected?.invoke(selection.groupIndex, selection.entryIndex, selection.stackIndex)
            currentOnSelectionChanged?.invoke(selection)
        }
    }

    Canvas(
        modifier = modifier
            .semantics {
                contentDescription = accessibilityDescription
                stateDescription = selectionDescription
                onClickLabel?.let { label -> onClick(label = label, action = null) }
            }
            .fillMaxWidth()
            .onSizeChanged { chartSize = it }
            .then(touchModifier),
    ) {
        if (chartArea.width <= 0f || chartArea.height <= 0f) return@Canvas

        // Draw grid and axes
        drawGrid(resolvedGridStyle, chartArea, resolvedAxisStyle.yLabelCount)

        if (resolvedAxisStyle.showYAxis) {
            drawYAxisLabels(
                minValue = 0f,
                maxValue = adjustedMax,
                style = resolvedAxisStyle,
                chartArea = chartArea,
                labelPaint = yAxisLabelPaint,
            )
        }

        // X-axis labels (centered under each bar group)
        if (resolvedAxisStyle.showXAxis) {
            if (groupLabels.any { it.isNotEmpty() }) {
                drawXAxisLabels(
                    labels = groupLabels,
                    style = resolvedAxisStyle,
                    chartArea = chartArea,
                    labelPaint = xAxisLabelPaint,
                    groupWidth = groupWidth,
                    groupSpacing = groupSpacingPx,
                )
            }
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
                // Draw bars and tooltips
                val cornerRadiusPx = style.cornerRadius.toPx()
                val roundedSegmentPath = Path()
                barLayouts.forEach { group ->
                    group.entries.forEach { entryLayout ->
                        // Highlight: apply transparency to unselected bars
                        val isHighlighted = !style.highlightOnTouch ||
                            selectedGroupIndex == -1 ||
                            selectedGroupIndex == group.groupIndex
                        val alpha = if (isHighlighted) 1f else style.highlightAlpha

                        if (style.horizontal) {
                            drawHorizontalStackedBar(
                                values = entryLayout.entry.safeValues,
                                colors = entryLayout.segmentColors,
                                barTop = entryLayout.barLeft,
                                barHeight = group.barWidth,
                                chartArea = chartArea,
                                maxValue = adjustedMax,
                                progress = progress,
                                alpha = alpha,
                                cornerRadius = cornerRadiusPx,
                                roundedPath = roundedSegmentPath,
                            )
                        } else {
                            drawVerticalStackedBar(
                                values = entryLayout.entry.safeValues,
                                colors = entryLayout.segmentColors,
                                barLeft = entryLayout.barLeft,
                                barWidth = group.barWidth,
                                chartArea = chartArea,
                                maxValue = adjustedMax,
                                progress = progress,
                                alpha = alpha,
                                cornerRadius = cornerRadiusPx,
                                roundedPath = roundedSegmentPath,
                            )
                        }
                    }

                    // Show tooltip for selected group
                    if (selectedGroupIndex == group.groupIndex && selectedEntryIndex in group.entries.indices) {
                        val entry = group.entries[selectedEntryIndex]
                        val totalValue = entry.totalValue
                        val formattedValue = ChartMath.formatValue(totalValue)
                        val tooltipText = if (group.label.isNotEmpty()) {
                            "${group.label}: $formattedValue"
                        } else {
                            formattedValue
                        }

                        val barHeight = (totalValue / adjustedMax) * chartArea.height * progress
                        val tooltipX = entry.barLeft + group.barWidth / 2
                        val tooltipY = chartArea.bottom - barHeight

                        drawTooltip(
                            position = Offset(tooltipX, tooltipY),
                            text = tooltipText,
                            style = style.tooltip,
                            lineColor = entry.tooltipLineColor,
                            canvasSize = size,
                            textPaint = tooltipTextPaint,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Draws a vertical stacked bar.
 * Rounded corners are applied only to the topmost segment.
 */
private fun DrawScope.drawVerticalStackedBar(
    values: List<Float>,
    colors: List<Color>,
    barLeft: Float,
    barWidth: Float,
    chartArea: Rect,
    maxValue: Float,
    progress: Float,
    alpha: Float,
    cornerRadius: Float,
    roundedPath: Path,
) {
    var currentBottom = chartArea.bottom
    val totalSegments = values.size

    values.forEachIndexed { index, value ->
        val segmentHeight = BarMath.calculateSegmentHeight(value, maxValue, chartArea.height, progress)
        val segmentTop = currentBottom - segmentHeight
        val color = colors[index % colors.size].copy(alpha = alpha)

        val isTopSegment = index == totalSegments - 1

        if (isTopSegment && cornerRadius > 0f) {
            // Top segment: rounded top corners only
            roundedPath.reset()
            roundedPath.addRoundRect(
                RoundRect(
                    left = barLeft,
                    top = segmentTop,
                    right = barLeft + barWidth,
                    bottom = currentBottom,
                    topLeftCornerRadius = CornerRadius(cornerRadius),
                    topRightCornerRadius = CornerRadius(cornerRadius),
                    bottomLeftCornerRadius = CornerRadius.Zero,
                    bottomRightCornerRadius = CornerRadius.Zero,
                )
            )
            drawPath(path = roundedPath, color = color, style = Fill)
        } else {
            // Bottom segments: square corners
            drawRect(
                color = color,
                topLeft = Offset(barLeft, segmentTop),
                size = Size(barWidth, segmentHeight),
            )
        }

        currentBottom = segmentTop
    }
}

/**
 * Draws a horizontal stacked bar.
 * Rounded corners are applied only to the rightmost segment.
 */
private fun DrawScope.drawHorizontalStackedBar(
    values: List<Float>,
    colors: List<Color>,
    barTop: Float,
    barHeight: Float,
    chartArea: Rect,
    maxValue: Float,
    progress: Float,
    alpha: Float,
    cornerRadius: Float,
    roundedPath: Path,
) {
    var currentLeft = chartArea.left
    val totalSegments = values.size

    values.forEachIndexed { index, value ->
        val segmentWidth = BarMath.calculateSegmentHeight(value, maxValue, chartArea.width, progress)
        val color = colors[index % colors.size].copy(alpha = alpha)

        val isRightSegment = index == totalSegments - 1

        if (isRightSegment && cornerRadius > 0f) {
            // Rightmost segment: rounded right corners only
            roundedPath.reset()
            roundedPath.addRoundRect(
                RoundRect(
                    left = currentLeft,
                    top = barTop,
                    right = currentLeft + segmentWidth,
                    bottom = barTop + barHeight,
                    topLeftCornerRadius = CornerRadius.Zero,
                    topRightCornerRadius = CornerRadius(cornerRadius),
                    bottomLeftCornerRadius = CornerRadius.Zero,
                    bottomRightCornerRadius = CornerRadius(cornerRadius),
                )
            )
            drawPath(path = roundedPath, color = color, style = Fill)
        } else {
            drawRect(
                color = color,
                topLeft = Offset(currentLeft, barTop),
                size = Size(segmentWidth, barHeight),
            )
        }

        currentLeft += segmentWidth
    }
}
