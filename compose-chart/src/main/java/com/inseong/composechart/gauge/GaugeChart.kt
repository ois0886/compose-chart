package com.inseong.composechart.gauge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.style.GaugeChartStyle
import kotlin.math.min

/**
 * Gauge/progress chart Composable.
 *
 * Displays a circular progress or semicircular gauge,
 * with optional value and label text overlaid at the center.
 *
 * Circular progress (360 degrees):
 * ```kotlin
 * GaugeChart(
 *     data = GaugeChartData(value = 72f, maxValue = 100f, label = "Score"),
 *     modifier = Modifier.size(180.dp),
 *     style = GaugeChartStyle(sweepAngle = 360f),
 * )
 * ```
 *
 * Semicircular gauge (240 degrees, default):
 * ```kotlin
 * GaugeChart(
 *     data = GaugeChartData(value = 72f, maxValue = 100f, label = "Progress"),
 *     modifier = Modifier.size(180.dp),
 * )
 * ```
 *
 * @param data Data to display in the gauge (current value, max value, label)
 * @param modifier Layout Modifier (size must be specified)
 * @param style Gauge style configuration
 * @param centerContent Custom Composable to display at the center. If null, default text is shown.
 *                      Default text is only shown when [GaugeChartStyle.showCenterText] is true.
 */
@Composable
fun GaugeChart(
    data: GaugeChartData,
    modifier: Modifier = Modifier,
    style: GaugeChartStyle = GaugeChartStyle(),
    centerContent: (@Composable (animatedValue: Float) -> Unit)? = null,
) {
    // Detect dark theme and resolve colors
    val isDark = isSystemInDarkTheme()
    val resolvedTrackColor = ChartDefaults.resolveGaugeTrackColor(style.trackColor, isDark)
    val resolvedCenterTextColor = ChartDefaults.resolveGaugeCenterTextColor(style.centerTextColor, isDark)

    val progress by rememberChartAnimation(style.animationDurationMs)

    // Guard against negative/NaN/Infinity/zero: clamp to safe values
    val safeMax = if (data.maxValue.isFinite() && data.maxValue > 0f) data.maxValue else 1f
    val safeValue = if (data.value.isFinite()) data.value.coerceIn(0f, safeMax) else 0f
    val ratio = (safeValue / safeMax).coerceIn(0f, 1f)

    // Animated current value
    val animatedValue = safeValue * progress
    val animatedRatio = ratio * progress

    // Start angle: calculated so the gap is centered at the bottom
    // e.g. sweepAngle=240 -> gap=120 degrees, start=150 degrees (12 o'clock=-90, 6 o'clock=90)
    val startAngle = 90f + (360f - style.sweepAngle) / 2

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .background(style.chart.backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        // Reference diameter (in px) at which full-size text is displayed
        val referenceDiameterDp = 120.dp

        // Draw gauge arcs
        Canvas(modifier = Modifier.matchParentSize()) {
            val paddingPx = style.chart.chartPadding.toPx()
            val strokeWidthPx = style.strokeWidth.toPx()

            val diameter = min(size.width, size.height) - paddingPx * 2 - strokeWidthPx
            if (diameter <= 0f) return@Canvas

            val topLeft = Offset(
                x = (size.width - diameter) / 2,
                y = (size.height - diameter) / 2,
            )
            val arcSize = Size(diameter, diameter)
            val cap = if (style.roundCap) StrokeCap.Round else StrokeCap.Butt

            // Background track arc
            drawArc(
                color = resolvedTrackColor,
                startAngle = startAngle,
                sweepAngle = style.sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = cap),
            )

            // Progress arc (animated)
            val progressSweep = style.sweepAngle * animatedRatio
            if (progressSweep > 0f) {
                drawArc(
                    color = style.progressColor,
                    startAngle = startAngle,
                    sweepAngle = progressSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = cap),
                )
            }
        }

        // Center content (text overlay)
        if (centerContent != null) {
            centerContent(animatedValue)
        } else if (style.showCenterText) {
            // Use BoxWithConstraints to scale text proportionally to available diameter
            androidx.compose.foundation.layout.BoxWithConstraints {
                val density = androidx.compose.ui.platform.LocalDensity.current
                val availableDiameter = with(density) {
                    val paddingPx = style.chart.chartPadding.toPx()
                    val strokeWidthPx = style.strokeWidth.toPx()
                    min(maxWidth.toPx(), maxHeight.toPx()) - paddingPx * 2 - strokeWidthPx
                }
                val referencePx = with(density) { referenceDiameterDp.toPx() }
                val textScale = (availableDiameter / referencePx).coerceIn(0f, 1f)

                // Hide text when component is too small to display legibly
                if (textScale >= 0.3f) {
                    val scaledTextSize = style.centerTextSize * textScale

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Value text
                        val valueText = if (animatedValue == animatedValue.toLong().toFloat()) {
                            animatedValue.toLong().toString()
                        } else {
                            String.format("%.1f", animatedValue)
                        }
                        androidx.compose.foundation.text.BasicText(
                            text = valueText,
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = scaledTextSize,
                                color = resolvedCenterTextColor,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            ),
                        )

                        // Label text
                        if (data.label.isNotEmpty()) {
                            androidx.compose.foundation.text.BasicText(
                                text = data.label,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = scaledTextSize * 0.5f,
                                    color = resolvedCenterTextColor.copy(alpha = 0.6f),
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
