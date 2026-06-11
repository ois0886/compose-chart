package com.inseong.composechart.internal.math

import com.inseong.composechart.data.LineSeries

/**
 * XY range calculation result for axis-based charts.
 */
internal data class XYRange(
    val minX: Float,
    val maxX: Float,
    val xRange: Float,
    val adjustedMinY: Float,
    val adjustedMaxY: Float,
)

/**
 * Pure math functions shared across XY-axis charts (Line, Bar).
 *
 * All functions are free of Compose/Android dependencies and can run on JVM.
 */
internal object ChartMath {

    /**
     * Calculates X/Y data range with 10% Y-axis padding.
     *
     * When [yAxisMin] or [yAxisMax] is provided, they override the auto-calculated values.
     * If both are provided but min >= max, falls back to auto-calculation.
     *
     * @param xValues safe X values (NaN/Infinity already filtered)
     * @param yValues safe Y values (NaN/Infinity already filtered)
     * @param yAxisMin Manual Y-axis minimum override. null for auto.
     * @param yAxisMax Manual Y-axis maximum override. null for auto.
     * @return [XYRange] with min/max and adjusted Y range
     */
    fun calculateXYRange(
        xValues: List<Float>,
        yValues: List<Float>,
        yAxisMin: Float? = null,
        yAxisMax: Float? = null,
    ): XYRange {
        val minX = xValues.minOrNull() ?: 0f
        val maxX = xValues.maxOrNull() ?: 0f
        val minY = yValues.minOrNull() ?: 0f
        val maxY = yValues.maxOrNull() ?: 0f

        return buildXYRange(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY,
            yAxisMin = yAxisMin,
            yAxisMax = yAxisMax,
        )
    }

    /**
     * Calculates X/Y data range for line series without building intermediate point/value lists.
     */
    fun calculateXYRange(
        series: List<LineSeries>,
        yAxisMin: Float? = null,
        yAxisMax: Float? = null,
    ): XYRange {
        var hasPoint = false
        var minX = 0f
        var maxX = 0f
        var minY = 0f
        var maxY = 0f

        series.forEach { lineSeries ->
            lineSeries.points.forEach { point ->
                val safeX = point.safeX
                val safeY = point.safeY
                if (!hasPoint) {
                    minX = safeX
                    maxX = safeX
                    minY = safeY
                    maxY = safeY
                    hasPoint = true
                } else {
                    if (safeX < minX) minX = safeX
                    if (safeX > maxX) maxX = safeX
                    if (safeY < minY) minY = safeY
                    if (safeY > maxY) maxY = safeY
                }
            }
        }

        return buildXYRange(
            minX = minX,
            maxX = maxX,
            minY = minY,
            maxY = maxY,
            yAxisMin = yAxisMin,
            yAxisMax = yAxisMax,
        )
    }

    private fun buildXYRange(
        minX: Float,
        maxX: Float,
        minY: Float,
        maxY: Float,
        yAxisMin: Float?,
        yAxisMax: Float?,
    ): XYRange {
        val yRange = (maxY - minY).let { if (it == 0f) 1f else it }
        val yPadding = yRange * 0.1f
        val autoMinY = minY - yPadding
        val autoMaxY = maxY + yPadding
        val xRange = (maxX - minX).let { if (it == 0f) 1f else it }

        // Apply manual overrides with validation
        val adjustedMinY: Float
        val adjustedMaxY: Float
        if (yAxisMin != null && yAxisMax != null && yAxisMin >= yAxisMax) {
            // Invalid range: fallback to auto
            adjustedMinY = autoMinY
            adjustedMaxY = autoMaxY
        } else {
            adjustedMinY = yAxisMin ?: autoMinY
            adjustedMaxY = yAxisMax ?: autoMaxY
        }

        return XYRange(
            minX = minX,
            maxX = maxX,
            xRange = xRange,
            adjustedMinY = adjustedMinY,
            adjustedMaxY = adjustedMaxY,
        )
    }

    /**
     * Maps a data point to canvas pixel coordinates.
     *
     * @return Pair of (canvasX, canvasY)
     */
    fun mapToCanvas(
        dataX: Float,
        dataY: Float,
        range: XYRange,
        chartLeft: Float,
        chartBottom: Float,
        chartWidth: Float,
        chartHeight: Float,
    ): Pair<Float, Float> {
        val yRange = range.adjustedMaxY - range.adjustedMinY
        val safeYRange = if (yRange == 0f) 1f else yRange
        val canvasX = chartLeft + ((dataX - range.minX) / range.xRange) * chartWidth
        val canvasY = chartBottom - ((dataY - range.adjustedMinY) / safeYRange) * chartHeight
        return canvasX to canvasY
    }

    /**
     * Formats a float value for display: integer if whole, otherwise one decimal place.
     */
    fun formatValue(value: Float): String {
        return if (value == value.toLong().toFloat()) {
            value.toLong().toString()
        } else {
            String.format("%.1f", value)
        }
    }
}
