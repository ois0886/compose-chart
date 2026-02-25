package com.inseong.composechart.bar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.canvas.drawGrid
import com.inseong.composechart.internal.canvas.drawTooltip
import com.inseong.composechart.internal.canvas.drawXAxisLabels
import com.inseong.composechart.internal.canvas.drawYAxisLabels
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.style.BarChartStyle

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
 * @param data Data to display in the chart
 * @param modifier Layout Modifier (size must be specified)
 * @param style Chart style configuration
 * @param colors Bar color palette
 * @param onBarSelected Callback on bar touch (group index, entry index, stack index)
 */
@Composable
fun BarChart(
    data: BarChartData,
    modifier: Modifier = Modifier,
    style: BarChartStyle = BarChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
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

    val progress by rememberChartAnimation(style.animationDurationMs)
    var touchOffset by remember { mutableStateOf<Offset?>(null) }
    var selectedGroupIndex by remember { mutableIntStateOf(-1) }

    // Filter valid groups (only groups with entries)
    val validGroups = remember(data) {
        data.groups.filter { it.entries.isNotEmpty() }
    }
    if (validGroups.isEmpty()) return

    // Calculate data range (safeValues guard against NaN/negative)
    val maxValue = validGroups.maxOf { group ->
        group.entries.maxOfOrNull { entry ->
            entry.safeValues.sum()
        } ?: 0f
    }
    val adjustedMax = if (maxValue <= 0f) 1f else maxValue * 1.1f // 10% top margin

    val chartPaddingPx = style.chart.chartPadding

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .chartTouchHandler { offset ->
                touchOffset = offset
                if (offset == null) selectedGroupIndex = -1
            },
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

        // Draw grid and axes
        drawGrid(resolvedGridStyle, chartArea, resolvedAxisStyle.yLabelCount)

        if (resolvedAxisStyle.showYAxis) {
            drawYAxisLabels(0f, adjustedMax, resolvedAxisStyle, chartArea)
        }

        // Bar layout calculation
        if (chartArea.width <= 0f || chartArea.height <= 0f) return@Canvas

        val groupCount = validGroups.size
        val groupSpacingPx = style.groupSpacing.toPx()
        val barSpacingPx = style.barSpacing.toPx()
        val totalGroupSpacing = groupSpacingPx * (groupCount - 1).coerceAtLeast(0)
        val groupWidth = ((chartArea.width - totalGroupSpacing) / groupCount).coerceAtLeast(1f)

        // X-axis labels (centered under each bar group)
        if (resolvedAxisStyle.showXAxis) {
            val labels = validGroups.map { it.label }
            if (labels.any { it.isNotEmpty() }) {
                drawXAxisLabels(labels, resolvedAxisStyle, chartArea, groupWidth, groupSpacingPx)
            }
        }

        // Calculate touched group index
        val currentTouch = touchOffset
        if (currentTouch != null) {
            val relativeX = currentTouch.x - chartArea.left
            val groupIndex = ((relativeX + groupSpacingPx / 2) / (groupWidth + groupSpacingPx)).toInt()
            selectedGroupIndex = groupIndex.coerceIn(0, groupCount - 1)
        }

        // Draw bars and tooltips
        validGroups.forEachIndexed { groupIndex, group ->
            val groupLeft = chartArea.left + groupIndex * (groupWidth + groupSpacingPx)
            val entryCount = group.entries.size
            val totalBarSpacing = barSpacingPx * (entryCount - 1).coerceAtLeast(0)
            val barWidth = ((groupWidth - totalBarSpacing) / entryCount.coerceAtLeast(1)).coerceAtLeast(1f)

            group.entries.forEachIndexed { entryIndex, entry ->
                val barLeft = groupLeft + entryIndex * (barWidth + barSpacingPx)

                // Highlight: apply transparency to unselected bars
                val isHighlighted = !style.highlightOnTouch ||
                    selectedGroupIndex == -1 ||
                    selectedGroupIndex == groupIndex
                val alpha = if (isHighlighted) 1f else style.highlightAlpha

                if (style.horizontal) {
                    drawHorizontalStackedBar(
                        entry.safeValues,
                        entry.colors.ifEmpty { colors },
                        barLeft, barWidth,
                        chartArea, adjustedMax, progress, alpha,
                        style.cornerRadius.toPx(),
                    )
                } else {
                    drawVerticalStackedBar(
                        entry.safeValues,
                        entry.colors.ifEmpty { colors },
                        barLeft, barWidth,
                        chartArea, adjustedMax, progress, alpha,
                        style.cornerRadius.toPx(),
                    )
                }
            }

            // Show tooltip for selected group
            if (selectedGroupIndex == groupIndex && group.entries.isNotEmpty()) {
                // Determine which entry was touched within the group
                val touchedEntryIndex = if (currentTouch != null && entryCount > 1) {
                    val relativeInGroup = currentTouch.x - groupLeft
                    (relativeInGroup / (barWidth + barSpacingPx)).toInt().coerceIn(0, entryCount - 1)
                } else {
                    0
                }

                val entry = group.entries[touchedEntryIndex]
                val totalValue = entry.safeValues.sum()
                val formattedValue = if (totalValue == totalValue.toLong().toFloat()) {
                    totalValue.toLong().toString()
                } else {
                    String.format("%.1f", totalValue)
                }
                val tooltipText = if (group.label.isNotEmpty()) {
                    "${group.label}: $formattedValue"
                } else {
                    formattedValue
                }

                val barLeft = groupLeft + touchedEntryIndex * (barWidth + barSpacingPx)
                val barHeight = (totalValue / adjustedMax) * chartArea.height * progress
                val tooltipX = barLeft + barWidth / 2
                val tooltipY = chartArea.bottom - barHeight

                drawTooltip(
                    position = Offset(tooltipX, tooltipY),
                    text = tooltipText,
                    style = style.tooltip,
                    lineColor = colors[touchedEntryIndex % colors.size],
                    canvasSize = size,
                )

                onBarSelected?.invoke(groupIndex, touchedEntryIndex, 0)
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
) {
    var currentBottom = chartArea.bottom
    val totalSegments = values.size

    values.forEachIndexed { index, value ->
        val segmentHeight = (value / maxValue) * chartArea.height * progress
        val segmentTop = currentBottom - segmentHeight
        val color = colors[index % colors.size].copy(alpha = alpha)

        val isTopSegment = index == totalSegments - 1

        if (isTopSegment && cornerRadius > 0f) {
            // Top segment: rounded top corners only
            val path = Path().apply {
                addRoundRect(
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
            }
            drawPath(path = path, color = color, style = Fill)
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
) {
    var currentLeft = chartArea.left
    val totalSegments = values.size

    values.forEachIndexed { index, value ->
        val segmentWidth = (value / maxValue) * chartArea.width * progress
        val color = colors[index % colors.size].copy(alpha = alpha)

        val isRightSegment = index == totalSegments - 1

        if (isRightSegment && cornerRadius > 0f) {
            // Rightmost segment: rounded right corners only
            val path = Path().apply {
                addRoundRect(
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
            }
            drawPath(path = path, color = color, style = Fill)
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
