package com.inseong.composechart.radar

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.ChartSelection
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.canvas.toTypefaceStyle
import com.inseong.composechart.internal.math.RadarMath
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.style.RadarChartStyle
import kotlin.math.min

private data class RadarGeometry(
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
    val axisVertices: List<Offset>,
    val axisLabelPositions: List<Offset>,
    val webPaths: List<Path>,
)

private data class RadarEntryLayout(
    val color: Color,
    val normalizedValues: List<Float>,
)

/**
 * Radar chart Composable.
 *
 * Displays data as a polygon on a radial grid,
 * with concentric web levels and axis labels at each vertex.
 * Supports multiple series (overlapping polygons).
 *
 * Basic usage:
 * ```kotlin
 * RadarChart(
 *     data = RadarChartData.single(
 *         values = listOf(80f, 65f, 90f, 70f, 85f),
 *         axisLabels = listOf("STR", "DEX", "INT", "WIS", "CHA"),
 *     ),
 *     modifier = Modifier.size(200.dp),
 * )
 * ```
 *
 * @param data Data to display. Requires `axisLabels.size >= 3`; otherwise rendering is a no-op.
 *   Entries with fewer values than `axisLabels` are dropped. `maxValue` 0 or less falls back
 *   to the maximum observed value.
 * @param modifier Layout Modifier. A finite size must be provided — the chart will not size itself.
 * @param style Chart style (web levels, web line color/width, dot radius, fill alpha, label size,
 *   animation duration).
 * @param colors Palette used for entries without an explicit color. Cycles after the palette is
 *   exhausted. See [ChartDefaults.colors].
 * @param accessibilityLabel Prefix used in the chart's semantics `contentDescription`. Default
 *   `"레이더 차트"`; override when localizing or using a more specific label.
 * @param onClickLabel Screen reader click action label. When non-null, an onClick semantics node
 *   is added so TalkBack announces the action.
 * @param onSelectionChanged Callback emitting [ChartSelection.Radar] when an axis vertex is
 *   touched. Prefer this over [onAxisSelected] for a signature shared across all charts.
 * @param onAxisSelected Positional-argument callback kept for source compatibility. Will be
 *   removed in v2.0 — migrate to [onSelectionChanged].
 *
 * @see ChartSelection.Radar
 * @see com.inseong.composechart.ChartCaptureState
 */
@Composable
fun RadarChart(
    data: RadarChartData,
    modifier: Modifier = Modifier,
    style: RadarChartStyle = RadarChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    accessibilityLabel: String = "레이더 차트",
    onClickLabel: String? = null,
    onSelectionChanged: ((ChartSelection.Radar) -> Unit)? = null,
    onAxisSelected: ((axisIndex: Int) -> Unit)? = null,
) {
    val isDark = isSystemInDarkTheme()
    val resolvedWebColor = ChartDefaults.resolveRadarWebColor(style.webLineColor, isDark)
    val resolvedLabelColor = ChartDefaults.resolveAxisLabelColor(style.labelColor, isDark)
    val density = LocalDensity.current
    var chartSize by remember { mutableStateOf(IntSize.Zero) }

    val progress by rememberChartAnimation(style.animationDurationMs, animationKey = data)

    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    val axisCount = data.axisLabels.size
    if (axisCount < 3) return

    val validEntries = remember(data) {
        data.entries.filter { it.values.size >= axisCount }
    }
    if (validEntries.isEmpty()) return

    val resolvedMaxValue = remember(data.maxValue, validEntries) {
        if (data.maxValue > 0f) {
            data.maxValue
        } else {
            (validEntries.asSequence().flatMap { it.safeValues.asSequence() }.maxOrNull() ?: 1f)
                .coerceAtLeast(1f)
        }
    }

    val geometry = remember(
        chartSize,
        style.chart.chartPadding,
        style.labelSize,
        style.webLevels,
        axisCount,
        density,
    ) {
        with(density) {
            val centerX = chartSize.width / 2f
            val centerY = chartSize.height / 2f
            val radius = min(chartSize.width.toFloat(), chartSize.height.toFloat()) / 2 -
                style.chart.chartPadding.toPx() -
                style.labelSize.toPx() * 1.5f
            val startAngle = -90f
            val angleStep = 360f / axisCount
            val axisVertices = if (radius > 0f) {
                (0 until axisCount).map { i ->
                    val (x, y) = RadarMath.polarToCartesian(centerX, centerY, startAngle + angleStep * i, radius)
                    Offset(x, y)
                }
            } else {
                emptyList()
            }
            val labelOffset = style.labelSize.toPx() * 1.2f
            val axisLabelPositions = if (radius > 0f) {
                (0 until axisCount).map { i ->
                    val (x, y) = RadarMath.calculateAxisLabelPosition(
                        axisIndex = i,
                        axisCount = axisCount,
                        centerX = centerX,
                        centerY = centerY,
                        radius = radius,
                        startAngleDegrees = startAngle,
                        labelOffset = labelOffset,
                        labelBaselineOffset = style.labelSize.toPx() / 3,
                    )
                    Offset(x, y)
                }
            } else {
                emptyList()
            }
            val webPaths = if (radius > 0f && style.webLevels > 0) {
                (1..style.webLevels).map { level ->
                    val levelRadius = radius * level / style.webLevels
                    Path().apply {
                        for (i in 0 until axisCount) {
                            val (x, y) = RadarMath.polarToCartesian(
                                centerX,
                                centerY,
                                startAngle + angleStep * i,
                                levelRadius,
                            )
                            if (i == 0) moveTo(x, y) else lineTo(x, y)
                        }
                        close()
                    }
                }
            } else {
                emptyList()
            }
            RadarGeometry(
                centerX = centerX,
                centerY = centerY,
                radius = radius,
                axisVertices = axisVertices,
                axisLabelPositions = axisLabelPositions,
                webPaths = webPaths,
            )
        }
    }

    val entryLayouts = remember(validEntries, resolvedMaxValue, axisCount, colors) {
        validEntries.mapIndexed { entryIndex, entry ->
            val entryColor = if (entry.color == Color.Unspecified) {
                colors[entryIndex % colors.size]
            } else {
                entry.color
            }
            RadarEntryLayout(
                color = entryColor,
                normalizedValues = List(axisCount) { i ->
                    entry.safeValues[i].coerceIn(0f, resolvedMaxValue) / resolvedMaxValue
                },
            )
        }
    }

    val selectedAxisIndex = remember(touchOffset, geometry.axisVertices) {
        touchOffset?.let { touch ->
            findNearestAxisIndex(touch, geometry.axisVertices).takeIf { it >= 0 }
        }
    }

    val currentOnAxisSelected by rememberUpdatedState(onAxisSelected)
    val currentOnSelectionChanged by rememberUpdatedState(onSelectionChanged)
    LaunchedEffect(selectedAxisIndex) {
        selectedAxisIndex?.let { axisIndex ->
            currentOnAxisSelected?.invoke(axisIndex)
            currentOnSelectionChanged?.invoke(ChartSelection.Radar(axisIndex))
        }
    }

    val accessibilityDescription = "$accessibilityLabel, ${axisCount}개 축, ${validEntries.size}개 시리즈"
    val selectionDescription = selectedAxisIndex
        ?.let(data.axisLabels::getOrNull)
        ?.let { "선택된 축: $it" }
        ?: "선택된 축 없음"

    Canvas(
        modifier = modifier
            .semantics {
                contentDescription = accessibilityDescription
                stateDescription = selectionDescription
                onClickLabel?.let { label -> onClick(label = label, action = null) }
            }
            .chartTouchHandler { offset ->
                touchOffset = offset
            }
            .onSizeChanged { chartSize = it },
    ) {
        val centerX = geometry.centerX
        val centerY = geometry.centerY
        val radius = geometry.radius
        if (radius <= 0f) return@Canvas

        val webLineWidthPx = style.webLineWidth.toPx()
        val dotRadiusPx = style.dotRadius.toPx()

        // Draw concentric web levels
        geometry.webPaths.forEach { webPath ->
            drawPath(
                path = webPath,
                color = resolvedWebColor,
                style = Stroke(width = webLineWidthPx),
            )
        }

        // Draw axis lines from center to each vertex
        geometry.axisVertices.forEach { vertex ->
            drawLine(
                color = resolvedWebColor,
                start = Offset(centerX, centerY),
                end = vertex,
                strokeWidth = webLineWidthPx,
            )
        }

        // Draw axis labels
        drawAxisLabels(
            axisLabels = data.axisLabels,
            positions = geometry.axisLabelPositions,
            labelColor = resolvedLabelColor,
            labelSize = style.labelSize.toPx(),
            fontWeight = style.labelFontWeight,
        )

        // Draw data polygons
        entryLayouts.forEach { layout ->
            val dataPath = Path()
            for (i in 0 until axisCount) {
                val axisVertex = geometry.axisVertices[i]
                val ratio = layout.normalizedValues[i] * progress
                val x = centerX + (axisVertex.x - centerX) * ratio
                val y = centerY + (axisVertex.y - centerY) * ratio
                if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            }
            dataPath.close()

            // Fill polygon
            drawPath(
                path = dataPath,
                color = layout.color,
                alpha = style.fillAlpha,
                style = Fill,
            )

            // Draw polygon outline
            drawPath(
                path = dataPath,
                color = layout.color,
                style = Stroke(width = 2f),
            )

            // Draw dots at vertices
            if (style.showDots) {
                for (i in 0 until axisCount) {
                    val axisVertex = geometry.axisVertices[i]
                    val ratio = layout.normalizedValues[i] * progress
                    drawCircle(
                        color = layout.color,
                        radius = dotRadiusPx,
                        center = Offset(
                            x = centerX + (axisVertex.x - centerX) * ratio,
                            y = centerY + (axisVertex.y - centerY) * ratio,
                        ),
                    )
                }
            }
        }
    }
}

private fun findNearestAxisIndex(
    touch: Offset,
    axisVertices: List<Offset>,
): Int {
    if (axisVertices.isEmpty()) return -1

    var nearestAxis = -1
    var minDistance = Float.MAX_VALUE
    axisVertices.forEachIndexed { index, vertex ->
        val dx = touch.x - vertex.x
        val dy = touch.y - vertex.y
        val distance = dx * dx + dy * dy
        if (distance < minDistance) {
            minDistance = distance
            nearestAxis = index
        }
    }
    return nearestAxis
}

private fun DrawScope.drawAxisLabels(
    axisLabels: List<String>,
    positions: List<Offset>,
    labelColor: Color,
    labelSize: Float,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    val paint = Paint().apply {
        color = labelColor.let {
            android.graphics.Color.argb(
                (it.alpha * 255).toInt(),
                (it.red * 255).toInt(),
                (it.green * 255).toInt(),
                (it.blue * 255).toInt(),
            )
        }
        textSize = labelSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.create(Typeface.DEFAULT, fontWeight.toTypefaceStyle())
    }

    axisLabels.forEachIndexed { index, label ->
        val position = positions.getOrNull(index) ?: return@forEachIndexed

        drawContext.canvas.nativeCanvas.drawText(label, position.x, position.y, paint)
    }
}
