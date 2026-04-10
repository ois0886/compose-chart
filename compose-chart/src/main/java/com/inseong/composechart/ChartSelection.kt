package com.inseong.composechart

import com.inseong.composechart.data.ChartPoint
import com.inseong.composechart.data.DonutSlice

/**
 * Common sealed type representing a touch selection within a chart.
 *
 * Each chart emits its own subtype through the `onSelectionChanged` callback.
 * Use [ChartSelection] when you need a single callback handler capable of
 * receiving selections from multiple chart types; otherwise the type-specific
 * lambda parameters on each chart already expose the concrete subtype.
 */
sealed interface ChartSelection {

    /** Selection emitted by `LineChart.onSelectionChanged`. */
    data class Line(
        val seriesIndex: Int,
        val pointIndex: Int,
        val point: ChartPoint,
    ) : ChartSelection

    /** Selection emitted by `BarChart.onSelectionChanged`. */
    data class Bar(
        val groupIndex: Int,
        val entryIndex: Int,
        val stackIndex: Int,
    ) : ChartSelection

    /** Selection emitted by `DonutChart.onSelectionChanged` and `PieChart.onSelectionChanged`. */
    data class Donut(
        val index: Int,
        val slice: DonutSlice,
    ) : ChartSelection

    /** Selection emitted by `RadarChart.onSelectionChanged`. */
    data class Radar(
        val axisIndex: Int,
    ) : ChartSelection

    /**
     * Selection emitted by `GaugeChart.onSelectionChanged` on touch.
     *
     * [value] and [ratio] reflect the currently animated progress at the
     * moment of touch. Ratio is in the `[0f, 1f]` range.
     */
    data class Gauge(
        val value: Float,
        val ratio: Float,
    ) : ChartSelection
}
