package com.inseong.composechart.internal.math

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Pure math functions for DonutChart and PieChart.
 */
internal object DonutMath {

    /**
     * Determines which slice was touched by angle and distance.
     *
     * @param touchX touch X coordinate
     * @param touchY touch Y coordinate
     * @param centerX chart center X
     * @param centerY chart center Y
     * @param outerRadius outer radius of the donut/pie
     * @param holeRadius inner hole radius (0 for pie)
     * @param sliceValues list of slice values (already filtered, all > 0)
     * @param total sum of all slice values
     * @param startAngle starting angle in degrees
     * @return index of the touched slice, or -1 if not in any slice
     */
    fun findTouchedSliceIndex(
        touchX: Float,
        touchY: Float,
        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        holeRadius: Float,
        sliceValues: List<Float>,
        total: Float,
        startAngle: Float,
    ): Int {
        val dx = touchX - centerX
        val dy = touchY - centerY
        val distance = sqrt(dx * dx + dy * dy)

        if (distance > outerRadius || distance < holeRadius) return -1

        var touchAngle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
        touchAngle = (touchAngle - startAngle + 720f) % 360f

        var accumulatedAngle = 0f
        sliceValues.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f
            if (touchAngle >= accumulatedAngle && touchAngle < accumulatedAngle + sweepAngle) {
                return index
            }
            accumulatedAngle += sweepAngle
        }
        return -1
    }

    /**
     * Calculates offset for a selected (expanded) slice.
     *
     * @param midAngle angle to the midpoint of the slice in degrees
     * @param scale current scale (1.0 = no expansion)
     * @param radius outer radius
     * @return Pair of (offsetX, offsetY)
     */
    fun calculateSliceOffset(
        midAngle: Float,
        scale: Float,
        radius: Float,
    ): Pair<Float, Float> {
        if (scale <= 1f) return 0f to 0f
        val midAngleRad = Math.toRadians(midAngle.toDouble())
        val offsetDistance = radius * (scale - 1f) * 2
        val offsetX = (cos(midAngleRad) * offsetDistance).toFloat()
        val offsetY = (sin(midAngleRad) * offsetDistance).toFloat()
        return offsetX to offsetY
    }

    /**
     * Calculates spacing angle between slices based on pixel spacing and circumference.
     *
     * @param sliceCount number of slices
     * @param spacingPx spacing in pixels
     * @param radius outer radius
     * @return spacing angle in degrees
     */
    fun calculateSpacingAngle(
        sliceCount: Int,
        spacingPx: Float,
        radius: Float,
    ): Float {
        if (sliceCount <= 1) return 0f
        val circumference = 2 * Math.PI.toFloat() * radius
        return if (circumference > 0f) {
            (spacingPx / circumference) * 360f
        } else {
            0f
        }
    }

    /**
     * Calculates the (x, y) anchor point for a slice label, given the slice mid-angle.
     *
     * @param midAngleDegrees angle to the midpoint of the slice in degrees
     * @param centerX chart center X
     * @param centerY chart center Y
     * @param labelRadius distance from center at which to place the label
     * @return Pair of (labelX, labelY) in canvas coordinates
     */
    fun calculateLabelAnchor(
        midAngleDegrees: Float,
        centerX: Float,
        centerY: Float,
        labelRadius: Float,
    ): Pair<Float, Float> {
        val rad = Math.toRadians(midAngleDegrees.toDouble())
        val x = centerX + (cos(rad) * labelRadius).toFloat()
        val y = centerY + (sin(rad) * labelRadius).toFloat()
        return x to y
    }

    /**
     * Checks whether a label's bounding box fits entirely within the canvas minus a margin.
     *
     * @param labelX horizontal center of the label
     * @param labelY vertical reference of the label
     * @param textWidth measured text width in px
     * @param textHeight measured text height in px
     * @param canvasWidth canvas width in px
     * @param canvasHeight canvas height in px
     * @param marginPx padding from canvas edges
     * @return true if the label can be drawn without clipping
     */
    fun isLabelWithinBounds(
        labelX: Float,
        labelY: Float,
        textWidth: Float,
        textHeight: Float,
        canvasWidth: Float,
        canvasHeight: Float,
        marginPx: Float,
    ): Boolean {
        if (labelX - textWidth / 2 < marginPx) return false
        if (labelX + textWidth / 2 > canvasWidth - marginPx) return false
        if (labelY - textHeight < marginPx) return false
        if (labelY + textHeight / 2 > canvasHeight - marginPx) return false
        return true
    }
}
