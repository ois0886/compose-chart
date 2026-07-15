package com.inseong.composechart.donut

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.ChartSelection
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.internal.math.DonutMath
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.style.DonutChartStyle
import kotlin.math.min

private data class DonutGeometry(
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
    val holeRadius: Float,
)

/**
 * Donut chart Composable.
 *
 * Adjust [DonutChartStyle.holeRadius] to control the center hole size.
 * Set to 0 for a filled circle, or 0.6 for a typical donut.
 * Supports clockwise sweep animation and slice expansion on touch.
 *
 * Donut chart:
 * ```kotlin
 * DonutChart(
 *     data = DonutChartData(
 *         slices = listOf(
 *             DonutSlice(40f, "Food"),
 *             DonutSlice(25f, "Transport"),
 *             DonutSlice(35f, "Other"),
 *         ),
 *     ),
 *     modifier = Modifier.size(200.dp),
 * )
 * ```
 *
 * @param data Data to display. Slices with non-positive value are skipped; rendering is a no-op
 *   if all slices are filtered out.
 * @param modifier Layout Modifier. A finite size must be provided — the chart will not size itself.
 * @param style Chart style (hole radius, slice spacing, selected scale, labels, animation, start
 *   angle). Use `holeRadius = 0f` for a filled pie look, `0.6f` for a typical donut.
 * @param colors Slice color palette used when slices omit a color. An empty list falls back to
 *   [ChartDefaults.colors].
 * @param accessibilityLabel Base prefix used in the chart's semantics `contentDescription`.
 *   Default `"도넛 차트"`; override when localizing or using a more specific label.
 * @param onClickLabel Screen reader click action label. When non-null, an onClick semantics node
 *   is added so TalkBack announces the action.
 * @param onSelectionChanged Callback emitting [ChartSelection.Donut] on slice touch. Prefer this
 *   over [onSliceSelected] for a signature shared across all charts.
 * @param onSliceSelected Positional-argument callback kept for source compatibility. Will be
 *   removed in v2.0 — migrate to [onSelectionChanged].
 *
 * @see ChartSelection.Donut
 * @see com.inseong.composechart.pie.PieChart
 * @see com.inseong.composechart.ChartCaptureState
 */
@Composable
fun DonutChart(
    data: DonutChartData,
    modifier: Modifier = Modifier,
    style: DonutChartStyle = DonutChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    accessibilityLabel: String = "도넛 차트",
    onClickLabel: String? = null,
    onSelectionChanged: ((ChartSelection.Donut) -> Unit)? = null,
    onSliceSelected: ((index: Int, slice: DonutSlice) -> Unit)? = null,
) {
    val resolvedColors = remember(colors) { ChartDefaults.resolveColors(colors) }

    val density = LocalDensity.current
    var chartSize by remember { mutableStateOf(IntSize.Zero) }
    val progress by rememberChartAnimation(style.animationDurationMs, animationKey = data)
    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    // Filter valid slices (value > 0)
    val validSlices = remember(data) { data.slices.filter { it.value > 0f } }
    if (validSlices.isEmpty()) return

    val sliceValues = remember(validSlices) { validSlices.map { it.value } }
    val total = remember(validSlices) { validSlices.sumOf { it.value.toDouble() }.toFloat() }
    if (total == 0f) return

    val geometry = remember(chartSize, style.chart.chartPadding, style.holeRadius, density) {
        with(density) {
            val paddingPx = style.chart.chartPadding.toPx()
            val radius = min(chartSize.width.toFloat(), chartSize.height.toFloat()) / 2 - paddingPx
            DonutGeometry(
                centerX = chartSize.width / 2f,
                centerY = chartSize.height / 2f,
                radius = radius,
                holeRadius = radius * style.holeRadius.coerceIn(0f, 0.95f),
            )
        }
    }

    val selectedIndex = remember(touchOffset, geometry, sliceValues, total, style.startAngle) {
        val currentTouch = touchOffset
        if (currentTouch == null || geometry.radius <= 0f) {
            -1
        } else {
            DonutMath.findTouchedSliceIndex(
                touchX = currentTouch.x,
                touchY = currentTouch.y,
                centerX = geometry.centerX,
                centerY = geometry.centerY,
                outerRadius = geometry.radius,
                holeRadius = geometry.holeRadius,
                sliceValues = sliceValues,
                total = total,
                startAngle = style.startAngle,
            )
        }
    }

    val currentOnSliceSelected by rememberUpdatedState(onSliceSelected)
    val currentOnSelectionChanged by rememberUpdatedState(onSelectionChanged)
    LaunchedEffect(selectedIndex, validSlices) {
        validSlices.getOrNull(selectedIndex)?.let { slice ->
            currentOnSliceSelected?.invoke(selectedIndex, slice)
            currentOnSelectionChanged?.invoke(ChartSelection.Donut(selectedIndex, slice))
        }
    }

    // Scale animation for selected slice
    val selectedScales = validSlices.indices.map { index ->
        animateFloatAsState(
            targetValue = if (index == selectedIndex) style.selectedScale else 1f,
            animationSpec = tween(200),
            label = "sliceScale$index",
        )
    }

    val accessibilityDescription =
        "$accessibilityLabel, ${validSlices.size}개 항목, 총합 ${ChartDefaults.formatSemanticsValue(total)}"
    val selectionDescription = validSlices.getOrNull(selectedIndex)?.let { slice ->
        val label = slice.label.takeIf { it.isNotEmpty() } ?: "${selectedIndex + 1}번 항목"
        "선택된 항목: $label, 값 ${ChartDefaults.formatSemanticsValue(slice.value)}"
    } ?: "선택된 항목 없음"

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

        val holeRadiusPx = geometry.holeRadius

        // Spacing angle between slices
        val spacingAngle = DonutMath.calculateSpacingAngle(
            sliceCount = validSlices.size,
            spacingPx = style.sliceSpacing.toPx(),
            radius = radius,
        )

        val labelPaint = if (style.showLabels && progress > 0.8f) {
            val scaledTextSize = (radius * 0.12f).coerceIn(8f * this.density, 14f * this.density)
            Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = scaledTextSize
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
                typeface = Typeface.DEFAULT_BOLD
            }
        } else {
            null
        }

        // Draw slices
        var currentAngle = style.startAngle

        validSlices.forEachIndexed { index, slice ->
            val rawSweep = (slice.value / total) * 360f
            val sweepAngle = rawSweep * progress
            val actualSweep = (sweepAngle - spacingAngle).coerceAtLeast(0.1f)

            val sliceColor = if (slice.color == Color.Unspecified) {
                resolvedColors[index % resolvedColors.size]
            } else {
                slice.color
            }

            val scale = selectedScales[index].value

            // Selected slice is offset slightly outward from center
            val midAngle = currentAngle + sweepAngle / 2
            val (offsetX, offsetY) = DonutMath.calculateSliceOffset(midAngle, scale, radius)

            val arcCenterX = centerX + offsetX
            val arcCenterY = centerY + offsetY

            if (style.holeRadius > 0f) {
                // Donut mode
                val strokeWidth = radius - holeRadiusPx
                val arcRadius = holeRadiusPx + strokeWidth / 2

                drawArc(
                    color = sliceColor,
                    startAngle = currentAngle + spacingAngle / 2,
                    sweepAngle = actualSweep,
                    useCenter = false,
                    topLeft = Offset(arcCenterX - arcRadius, arcCenterY - arcRadius),
                    size = Size(arcRadius * 2, arcRadius * 2),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth),
                )
            } else {
                // Filled mode
                drawArc(
                    color = sliceColor,
                    startAngle = currentAngle + spacingAngle / 2,
                    sweepAngle = actualSweep,
                    useCenter = true,
                    topLeft = Offset(arcCenterX - radius, arcCenterY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Fill,
                )
            }

            // Draw slice label
            // Skip labels when chart is too small or slice is too narrow (< 15 degrees)
            val minLabelRadius = 40f * this.density
            if (style.showLabels && slice.label.isNotEmpty() && progress > 0.8f && rawSweep >= 15f && radius >= minLabelRadius) {
                // Donut: midpoint between hole and outer edge, Filled: 65% of radius
                val labelRadius = if (style.holeRadius > 0f) {
                    (holeRadiusPx + radius) / 2
                } else {
                    radius * 0.65f
                }

                drawSliceLabel(
                    label = slice.label,
                    midAngle = midAngle,
                    centerX = arcCenterX,
                    centerY = arcCenterY,
                    labelRadius = labelRadius,
                    canvasWidth = size.width,
                    canvasHeight = size.height,
                    paint = labelPaint ?: return@forEachIndexed,
                )
            }

            currentAngle += sweepAngle
        }
    }
}

/**
 * Draws label text at the midpoint of a slice.
 *
 * Text size scales proportionally to chart size.
 * Text is not drawn if it would extend beyond canvas bounds.
 */
private fun DrawScope.drawSliceLabel(
    label: String,
    midAngle: Float,
    centerX: Float,
    centerY: Float,
    labelRadius: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    paint: Paint,
) {
    val (labelX, labelY) = DonutMath.calculateLabelAnchor(
        midAngleDegrees = midAngle,
        centerX = centerX,
        centerY = centerY,
        labelRadius = labelRadius,
    )

    // Measure text size
    val textWidth = paint.measureText(label)
    val textHeight = paint.textSize

    // Do not draw if text extends beyond canvas bounds
    if (!DonutMath.isLabelWithinBounds(
            labelX = labelX,
            labelY = labelY,
            textWidth = textWidth,
            textHeight = textHeight,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,
            marginPx = 4f,
        )
    ) return

    drawContext.canvas.nativeCanvas.drawText(
        label,
        labelX,
        labelY + textHeight / 3, // Vertical centering adjustment
        paint,
    )
}
