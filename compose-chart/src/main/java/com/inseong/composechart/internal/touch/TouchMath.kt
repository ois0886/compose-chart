package com.inseong.composechart.internal.touch

import kotlin.math.sqrt

/**
 * Pure math functions for 2D touch detection, shared by ScatterChart and BubbleChart.
 */
internal object TouchMath {

    /**
     * Finds the nearest point index by 2D Euclidean distance.
     *
     * @param touchX touch X coordinate
     * @param touchY touch Y coordinate
     * @param pointXPositions X coordinates of data points
     * @param pointYPositions Y coordinates of data points
     * @return index of the nearest point, or -1 if lists are empty
     */
    fun findNearest2DPointIndex(
        touchX: Float,
        touchY: Float,
        pointXPositions: List<Float>,
        pointYPositions: List<Float>,
    ): Int {
        if (pointXPositions.isEmpty()) return -1
        var nearestIndex = -1
        var minDistance = Float.MAX_VALUE

        pointXPositions.forEachIndexed { index, x ->
            val y = pointYPositions[index]
            val dx = touchX - x
            val dy = touchY - y
            val distance = sqrt(dx * dx + dy * dy)
            if (distance < minDistance) {
                minDistance = distance
                nearestIndex = index
            }
        }
        return nearestIndex
    }
}
