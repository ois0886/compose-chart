package com.inseong.composechart

import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.BubbleChartData
import com.inseong.composechart.data.BubblePoint
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.data.RadarEntry
import com.inseong.composechart.data.ScatterChartData
import com.inseong.composechart.data.ScatterPoint

enum class ChartType(val displayName: String) {
    LINE("Line Chart"),
    BAR("Bar Chart"),
    DONUT("Donut Chart"),
    GAUGE("Gauge Chart"),
    SCATTER("Scatter Chart"),
    BUBBLE("Bubble Chart"),
    RADAR("Radar Chart"),
    PIE("Pie Chart"),
}

// ── Normal Data ──

val normalLineData = LineChartData.fromValues(
    values = listOf(15f, 28f, 22f, 35f, 30f, 42f),
    xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
)

val normalBarData = BarChartData.simple(
    values = listOf(30f, 45f, 28f, 55f, 38f),
    labels = listOf("Jan", "Feb", "Mar", "Apr", "May"),
)

val normalDonutData = DonutChartData.fromValues(
    values = mapOf("Food" to 40f, "Transport" to 25f, "Shopping" to 20f, "Other" to 15f),
)

val normalGaugeData = GaugeChartData(value = 72f, maxValue = 100f, label = "Progress")

val normalScatterData = ScatterChartData.fromValues(
    xValues = listOf(1f, 2f, 3f, 4f, 5f, 6f),
    yValues = listOf(12f, 28f, 15f, 35f, 22f, 40f),
    xLabels = listOf("A", "B", "C", "D", "E", "F"),
)

val normalBubbleData = BubbleChartData.fromValues(
    xValues = listOf(1f, 2f, 3f, 4f, 5f),
    yValues = listOf(20f, 35f, 15f, 45f, 28f),
    sizes = listOf(8f, 20f, 5f, 30f, 12f),
    xLabels = listOf("A", "B", "C", "D", "E"),
)

val normalRadarData = RadarChartData.single(
    values = listOf(80f, 65f, 90f, 70f, 85f),
    axisLabels = listOf("STR", "DEX", "INT", "WIS", "CHA"),
)

val normalPieData = DonutChartData.fromValues(
    values = mapOf("Food" to 35f, "Transport" to 20f, "Shopping" to 25f, "Other" to 20f),
)

// ── Extreme Data (1 ~ 10000) ──

val extremeLineData = LineChartData.fromValues(
    values = listOf(1f, 5000f, 10f, 10000f, 3f, 8000f),
    xLabels = listOf("A", "B", "C", "D", "E", "F"),
)

val extremeBarData = BarChartData.simple(
    values = listOf(1f, 10000f, 50f, 7500f, 3f),
    labels = listOf("A", "B", "C", "D", "E"),
)

val extremeDonutData = DonutChartData.fromValues(
    values = mapOf("Min" to 1f, "Max" to 10000f, "Mid" to 500f),
)

val extremeGaugeData = GaugeChartData(value = 1f, maxValue = 10000f, label = "Score")

val extremeScatterData = ScatterChartData.fromValues(
    xValues = listOf(1f, 500f, 10f, 10000f, 3f),
    yValues = listOf(1f, 5000f, 10f, 10000f, 3f),
)

val extremeBubbleData = BubbleChartData.fromValues(
    xValues = listOf(1f, 500f, 10000f),
    yValues = listOf(1f, 5000f, 10000f),
    sizes = listOf(1f, 500f, 10000f),
)

val extremeRadarData = RadarChartData.single(
    values = listOf(1f, 10000f, 50f, 7500f, 3f),
    axisLabels = listOf("A", "B", "C", "D", "E"),
)

val extremePieData = DonutChartData.fromValues(
    values = mapOf("Min" to 1f, "Max" to 10000f, "Mid" to 500f),
)

// ── Invalid Data (NaN / Infinity / Negative) ──

val invalidLineData = LineChartData.fromValues(
    values = listOf(Float.NaN, 25f, Float.POSITIVE_INFINITY, -10f, 30f),
    xLabels = listOf("NaN", "25", "Inf", "-10", "30"),
)

val invalidBarData = BarChartData.simple(
    values = listOf(Float.NaN, 45f, Float.NEGATIVE_INFINITY, -20f, 38f),
    labels = listOf("NaN", "45", "-Inf", "-20", "38"),
)

val invalidDonutData = DonutChartData(
    slices = listOf(
        DonutSlice(Float.NaN, "NaN"),
        DonutSlice(30f, "Valid"),
        DonutSlice(-10f, "Negative"),
        DonutSlice(Float.POSITIVE_INFINITY, "Infinity"),
    ),
)

val invalidGaugeData = GaugeChartData(value = Float.NaN, maxValue = 100f, label = "Invalid")

val invalidScatterData = ScatterChartData(
    points = listOf(
        ScatterPoint(Float.NaN, 10f, "NaN X"),
        ScatterPoint(2f, Float.POSITIVE_INFINITY, "Inf Y"),
        ScatterPoint(3f, -15f, "Negative"),
        ScatterPoint(4f, 25f, "Valid"),
    ),
)

val invalidBubbleData = BubbleChartData(
    points = listOf(
        BubblePoint(Float.NaN, 10f, 5f, "NaN X"),
        BubblePoint(2f, Float.NEGATIVE_INFINITY, 10f, "Inf Y"),
        BubblePoint(3f, 20f, Float.NaN, "NaN Size"),
        BubblePoint(4f, 30f, 15f, "Valid"),
    ),
)

val invalidRadarData = RadarChartData(
    entries = listOf(
        RadarEntry(
            values = listOf(Float.NaN, 65f, Float.POSITIVE_INFINITY, -10f, 85f),
            label = "Invalid",
        ),
    ),
    axisLabels = listOf("NaN", "65", "Inf", "-10", "85"),
)

val invalidPieData = DonutChartData(
    slices = listOf(
        DonutSlice(Float.NaN, "NaN"),
        DonutSlice(30f, "Valid"),
        DonutSlice(-5f, "Negative"),
    ),
)

// ── Empty Data ──

val emptyLineData = LineChartData(series = emptyList())
val emptyBarData = BarChartData(groups = emptyList())
val emptyDonutData = DonutChartData(slices = emptyList())
val emptyGaugeData = GaugeChartData(value = 0f, maxValue = 0f, label = "")
val emptyScatterData = ScatterChartData(points = emptyList())
val emptyBubbleData = BubbleChartData(points = emptyList())
val emptyRadarData = RadarChartData(entries = emptyList(), axisLabels = emptyList())
val emptyPieData = DonutChartData(slices = emptyList())
