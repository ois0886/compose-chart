package com.inseong.composechart.internal.math

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
 * Pure math functions shared across XY-axis charts (Line, Scatter, Bubble, Bar).
 *
 * All functions are free of Compose/Android dependencies and can run on JVM.
 */
internal object ChartMath {

    /**
     * Calculates X/Y data range with 10% Y-axis padding.
     *
     * @param xValues safe X values (NaN/Infinity already filtered)
     * @param yValues safe Y values (NaN/Infinity already filtered)
     * @return [XYRange] with min/max and adjusted Y range
     */
    fun calculateXYRange(xValues: List<Float>, yValues: List<Float>): XYRange {
        val minX = xValues.minOrNull() ?: 0f
        val maxX = xValues.maxOrNull() ?: 0f
        val minY = yValues.minOrNull() ?: 0f
        val maxY = yValues.maxOrNull() ?: 0f

        val yRange = (maxY - minY).let { if (it == 0f) 1f else it }
        val yPadding = yRange * 0.1f
        val adjustedMinY = minY - yPadding
        val adjustedMaxY = maxY + yPadding
        val xRange = (maxX - minX).let { if (it == 0f) 1f else it }

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

    /**
     * Normalizes a bubble size value to a pixel radius using linear interpolation.
     */
    fun normalizeBubbleRadius(
        sizeValue: Float,
        minSize: Float,
        sizeRange: Float,
        minRadius: Float,
        maxRadius: Float,
    ): Float {
        val safeSizeRange = if (sizeRange == 0f) 1f else sizeRange
        return minRadius + ((sizeValue - minSize) / safeSizeRange) * (maxRadius - minRadius)
    }
}
