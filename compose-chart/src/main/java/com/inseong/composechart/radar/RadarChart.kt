package com.inseong.composechart.radar

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.style.RadarChartStyle
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

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
 * @param data Data to display in the chart
 * @param modifier Layout Modifier (size must be specified)
 * @param style Chart style configuration
 * @param colors Series color palette. Used in order when no color is specified per entry.
 * @param onAxisSelected Callback when an axis vertex is touched. null for no callback.
 */
@Composable
fun RadarChart(
    data: RadarChartData,
    modifier: Modifier = Modifier,
    style: RadarChartStyle = RadarChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    onAxisSelected: ((axisIndex: Int) -> Unit)? = null,
) {
    val isDark = isSystemInDarkTheme()
    val resolvedWebColor = ChartDefaults.resolveRadarWebColor(style.webLineColor, isDark)
    val resolvedLabelColor = ChartDefaults.resolveAxisLabelColor(style.labelColor, isDark)

    val progress by rememberChartAnimation(style.animationDurationMs)

    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    val axisCount = data.axisLabels.size
    if (axisCount < 3) return

    val validEntries = remember(data) {
        data.entries.filter { it.values.size >= axisCount }
    }
    if (validEntries.isEmpty()) return

    val resolvedMaxValue = if (data.maxValue > 0f) {
        data.maxValue
    } else {
        validEntries.flatMap { it.safeValues }.maxOrNull() ?: 1f
    }

    Canvas(
        modifier = modifier.chartTouchHandler { offset ->
            touchOffset = offset
            if (offset == null) { /* no-op */ }
        },
    ) {
        val paddingPx = style.chart.chartPadding.toPx()
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = min(size.width, size.height) / 2 - paddingPx - style.labelSize.toPx() * 1.5f
        if (radius <= 0f) return@Canvas

        val angleStep = 360f / axisCount
        val startAngle = -90f

        // Draw concentric web levels
        for (level in 1..style.webLevels) {
            val levelRadius = radius * level / style.webLevels
            val webPath = Path()
            for (i in 0 until axisCount) {
                val angle = Math.toRadians((startAngle + angleStep * i).toDouble())
                val x = centerX + (cos(angle) * levelRadius).toFloat()
                val y = centerY + (sin(angle) * levelRadius).toFloat()
                if (i == 0) webPath.moveTo(x, y) else webPath.lineTo(x, y)
            }
            webPath.close()
            drawPath(
                path = webPath,
                color = resolvedWebColor,
                style = Stroke(width = style.webLineWidth.toPx()),
            )
        }

        // Draw axis lines from center to each vertex
        for (i in 0 until axisCount) {
            val angle = Math.toRadians((startAngle + angleStep * i).toDouble())
            val x = centerX + (cos(angle) * radius).toFloat()
            val y = centerY + (sin(angle) * radius).toFloat()
            drawLine(
                color = resolvedWebColor,
                start = Offset(centerX, centerY),
                end = Offset(x, y),
                strokeWidth = style.webLineWidth.toPx(),
            )
        }

        // Draw axis labels
        drawAxisLabels(
            axisLabels = data.axisLabels,
            centerX = centerX,
            centerY = centerY,
            radius = radius,
            startAngle = startAngle,
            angleStep = angleStep,
            labelColor = resolvedLabelColor,
            labelSize = style.labelSize.toPx(),
        )

        // Draw data polygons
        validEntries.forEachIndexed { entryIndex, entry ->
            val entryColor = if (entry.color == Color.Unspecified) {
                colors[entryIndex % colors.size]
            } else {
                entry.color
            }

            val safeValues = entry.safeValues
            val dataPath = Path()
            val dotPositions = mutableListOf<Offset>()

            for (i in 0 until axisCount) {
                val value = safeValues[i].coerceIn(0f, resolvedMaxValue)
                val normalizedValue = value / resolvedMaxValue * progress
                val angle = Math.toRadians((startAngle + angleStep * i).toDouble())
                val x = centerX + (cos(angle) * radius * normalizedValue).toFloat()
                val y = centerY + (sin(angle) * radius * normalizedValue).toFloat()

                if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
                dotPositions.add(Offset(x, y))
            }
            dataPath.close()

            // Fill polygon
            drawPath(
                path = dataPath,
                color = entryColor,
                alpha = style.fillAlpha,
                style = Fill,
            )

            // Draw polygon outline
            drawPath(
                path = dataPath,
                color = entryColor,
                style = Stroke(width = 2f),
            )

            // Draw dots at vertices
            if (style.showDots) {
                dotPositions.forEach { pos ->
                    drawCircle(
                        color = entryColor,
                        radius = style.dotRadius.toPx(),
                        center = pos,
                    )
                }
            }
        }

        // Touch interaction: find nearest axis
        val currentTouch = touchOffset
        if (currentTouch != null) {
            var nearestAxis = -1
            var minDistance = Float.MAX_VALUE

            for (i in 0 until axisCount) {
                val angle = Math.toRadians((startAngle + angleStep * i).toDouble())
                val x = centerX + (cos(angle) * radius).toFloat()
                val y = centerY + (sin(angle) * radius).toFloat()
                val dx = currentTouch.x - x
                val dy = currentTouch.y - y
                val distance = dx * dx + dy * dy
                if (distance < minDistance) {
                    minDistance = distance
                    nearestAxis = i
                }
            }

            if (nearestAxis >= 0) {
                onAxisSelected?.invoke(nearestAxis)
            }
        }
    }
}

private fun DrawScope.drawAxisLabels(
    axisLabels: List<String>,
    centerX: Float,
    centerY: Float,
    radius: Float,
    startAngle: Float,
    angleStep: Float,
    labelColor: Color,
    labelSize: Float,
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
    }

    val labelOffset = labelSize * 1.2f

    axisLabels.forEachIndexed { index, label ->
        val angle = Math.toRadians((startAngle + angleStep * index).toDouble())
        val x = centerX + (cos(angle) * (radius + labelOffset)).toFloat()
        val y = centerY + (sin(angle) * (radius + labelOffset)).toFloat() + labelSize / 3

        drawContext.canvas.nativeCanvas.drawText(label, x, y, paint)
    }
}
