package com.inseong.composechart.internal.math

import kotlin.math.cos
import kotlin.math.sin

/**
 * Pure math functions for RadarChart.
 */
internal object RadarMath {

    /**
     * Converts polar coordinates to cartesian (x, y).
     *
     * @param centerX center X coordinate
     * @param centerY center Y coordinate
     * @param angleDegrees angle in degrees
     * @param radius distance from center
     * @return Pair of (x, y)
     */
    fun polarToCartesian(
        centerX: Float,
        centerY: Float,
        angleDegrees: Float,
        radius: Float,
    ): Pair<Float, Float> {
        val angleRad = Math.toRadians(angleDegrees.toDouble())
        val x = centerX + (cos(angleRad) * radius).toFloat()
        val y = centerY + (sin(angleRad) * radius).toFloat()
        return x to y
    }

    /**
     * Calculates data polygon vertex positions.
     *
     * @param values data values for each axis
     * @param maxValue maximum value for normalization
     * @param axisCount number of axes
     * @param centerX center X
     * @param centerY center Y
     * @param radius chart radius
     * @param startAngle starting angle in degrees
     * @param progress animation progress (0-1)
     * @return list of (x, y) positions for each vertex
     */
    fun calculateDataPolygonVertices(
        values: List<Float>,
        maxValue: Float,
        axisCount: Int,
        centerX: Float,
        centerY: Float,
        radius: Float,
        startAngle: Float,
        progress: Float,
    ): List<Pair<Float, Float>> {
        val angleStep = 360f / axisCount
        return (0 until axisCount).map { i ->
            val value = values[i].coerceIn(0f, maxValue)
            val normalizedValue = value / maxValue * progress
            val angle = startAngle + angleStep * i
            polarToCartesian(centerX, centerY, angle, radius * normalizedValue)
        }
    }

    /**
     * Calculates the (x, y) anchor for an axis label placed outside the chart radius.
     *
     * Y already includes a baseline offset so the label is vertically centered
     * around the target position when drawn with a center-aligned Paint.
     *
     * @param axisIndex index of the axis (0-based)
     * @param axisCount total number of axes
     * @param centerX chart center X
     * @param centerY chart center Y
     * @param radius chart radius
     * @param startAngleDegrees angle of the first axis in degrees
     * @param labelOffset distance outside [radius] at which the label sits
     * @param labelBaselineOffset vertical baseline correction (typically textSize / 3)
     * @return Pair of (labelX, labelY) in canvas coordinates
     */
    fun calculateAxisLabelPosition(
        axisIndex: Int,
        axisCount: Int,
        centerX: Float,
        centerY: Float,
        radius: Float,
        startAngleDegrees: Float,
        labelOffset: Float,
        labelBaselineOffset: Float,
    ): Pair<Float, Float> {
        if (axisCount <= 0) return centerX to centerY
        val angleStep = 360f / axisCount
        val angle = startAngleDegrees + angleStep * axisIndex
        val (x, y) = polarToCartesian(centerX, centerY, angle, radius + labelOffset)
        return x to (y + labelBaselineOffset)
    }

    /**
     * Finds the nearest axis index to the touch point.
     *
     * @return nearest axis index, or -1 if axisCount is 0
     */
    fun findNearestAxisIndex(
        touchX: Float,
        touchY: Float,
        axisCount: Int,
        centerX: Float,
        centerY: Float,
        radius: Float,
        startAngle: Float,
    ): Int {
        if (axisCount == 0) return -1
        val angleStep = 360f / axisCount
        var nearestAxis = -1
        var minDistance = Float.MAX_VALUE

        for (i in 0 until axisCount) {
            val (x, y) = polarToCartesian(centerX, centerY, startAngle + angleStep * i, radius)
            val dx = touchX - x
            val dy = touchY - y
            val distance = dx * dx + dy * dy
            if (distance < minDistance) {
                minDistance = distance
                nearestAxis = i
            }
        }
        return nearestAxis
    }
}
