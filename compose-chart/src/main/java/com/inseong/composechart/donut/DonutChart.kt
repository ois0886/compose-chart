package com.inseong.composechart.donut

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.style.DonutChartStyle
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

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
 * @param data Data to display in the chart
 * @param modifier Layout Modifier (size must be specified)
 * @param style Chart style configuration
 * @param colors Slice color palette
 * @param onSliceSelected Callback on slice touch
 */
@Composable
fun DonutChart(
    data: DonutChartData,
    modifier: Modifier = Modifier,
    style: DonutChartStyle = DonutChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    onSliceSelected: ((index: Int, slice: DonutSlice) -> Unit)? = null,
) {
    val progress by rememberChartAnimation(style.animationDurationMs)
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    // Filter valid slices (value > 0)
    val validSlices = remember(data) { data.slices.filter { it.value > 0f } }
    if (validSlices.isEmpty()) return

    val total = validSlices.sumOf { it.value.toDouble() }.toFloat()
    if (total == 0f) return

    // Scale animation for selected slice
    val selectedScales = validSlices.indices.map { index ->
        animateFloatAsState(
            targetValue = if (index == selectedIndex) style.selectedScale else 1f,
            animationSpec = tween(200),
            label = "sliceScale$index",
        )
    }

    Canvas(
        modifier = modifier.chartTouchHandler { offset ->
            touchOffset = offset
            if (offset == null) selectedIndex = -1
        },
    ) {
        val paddingPx = style.chart.chartPadding.toPx()
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = min(size.width, size.height) / 2 - paddingPx
        if (radius <= 0f) return@Canvas

        val holeRadiusPx = radius * style.holeRadius.coerceIn(0f, 0.95f)

        // Spacing angle between slices — no spacing for single slice
        val spacingAngle = if (validSlices.size > 1) {
            val circumference = 2 * Math.PI.toFloat() * radius
            if (circumference > 0f) {
                (style.sliceSpacing.toPx() / circumference) * 360f
            } else {
                0f
            }
        } else {
            0f
        }

        // Touch detection: determine slice by angle and distance
        val currentTouch = touchOffset
        if (currentTouch != null) {
            val dx = currentTouch.x - centerX
            val dy = currentTouch.y - centerY
            val distance = sqrt(dx * dx + dy * dy)

            if (distance <= radius && distance >= holeRadiusPx) {
                var touchAngle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                touchAngle = (touchAngle - style.startAngle + 720f) % 360f

                var accumulatedAngle = 0f
                validSlices.forEachIndexed { index, slice ->
                    val sweepAngle = (slice.value / total) * 360f
                    if (touchAngle >= accumulatedAngle && touchAngle < accumulatedAngle + sweepAngle) {
                        if (selectedIndex != index) {
                            selectedIndex = index
                            onSliceSelected?.invoke(index, slice)
                        }
                        return@forEachIndexed
                    }
                    accumulatedAngle += sweepAngle
                }
            } else {
                selectedIndex = -1
            }
        }

        // Draw slices
        var currentAngle = style.startAngle

        validSlices.forEachIndexed { index, slice ->
            val rawSweep = (slice.value / total) * 360f
            val sweepAngle = rawSweep * progress
            val actualSweep = (sweepAngle - spacingAngle).coerceAtLeast(0.1f)

            val sliceColor = if (slice.color == Color.Unspecified) {
                colors[index % colors.size]
            } else {
                slice.color
            }

            val scale = selectedScales[index].value

            // Selected slice is offset slightly outward from center
            val midAngle = currentAngle + sweepAngle / 2
            val midAngleRad = Math.toRadians(midAngle.toDouble())
            val offsetDistance = if (scale > 1f) radius * (scale - 1f) * 2 else 0f
            val offsetX = (cos(midAngleRad) * offsetDistance).toFloat()
            val offsetY = (sin(midAngleRad) * offsetDistance).toFloat()

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
            val minLabelRadius = 40f * density
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
                    chartRadius = radius,
                    canvasWidth = size.width,
                    canvasHeight = size.height,
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
    chartRadius: Float,
    canvasWidth: Float,
    canvasHeight: Float,
) {
    val midAngleRad = Math.toRadians(midAngle.toDouble())
    val labelX = centerX + (cos(midAngleRad) * labelRadius).toFloat()
    val labelY = centerY + (sin(midAngleRad) * labelRadius).toFloat()

    // Text size proportional to chart size (min 8dp, max ~14dp)
    val scaledTextSize = (chartRadius * 0.12f).coerceIn(8f * density, 14f * density)

    val paint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = scaledTextSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }

    // Measure text size
    val textWidth = paint.measureText(label)
    val textHeight = scaledTextSize

    // Do not draw if text extends beyond canvas bounds
    val margin = 4f
    if (labelX - textWidth / 2 < margin || labelX + textWidth / 2 > canvasWidth - margin) return
    if (labelY - textHeight < margin || labelY + textHeight / 2 > canvasHeight - margin) return

    drawContext.canvas.nativeCanvas.drawText(
        label,
        labelX,
        labelY + textHeight / 3, // Vertical centering adjustment
        paint,
    )
}
