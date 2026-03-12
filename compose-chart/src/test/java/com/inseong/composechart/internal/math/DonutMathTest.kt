package com.inseong.composechart.internal.math

import org.junit.Assert.assertEquals
import org.junit.Test

class DonutMathTest {

    @Test
    fun findTouchedSliceIndex_firstSlice_returnsZero() {
        // 3 equal slices (120 degrees each), startAngle=0
        // Touch at angle ~60 degrees (midpoint of first slice)
        // Touch at (100 + 50*cos(60°), 100 + 50*sin(60°)) = (125, 143.3)
        val index = DonutMath.findTouchedSliceIndex(
            touchX = 125f, touchY = 143.3f,
            centerX = 100f, centerY = 100f,
            outerRadius = 80f, holeRadius = 30f,
            sliceValues = listOf(10f, 10f, 10f),
            total = 30f, startAngle = 0f,
        )
        assertEquals(0, index)
    }

    @Test
    fun findTouchedSliceIndex_insideHole_returnsNegativeOne() {
        val index = DonutMath.findTouchedSliceIndex(
            touchX = 100f, touchY = 100f, // at center
            centerX = 100f, centerY = 100f,
            outerRadius = 80f, holeRadius = 30f,
            sliceValues = listOf(10f, 10f),
            total = 20f, startAngle = 0f,
        )
        assertEquals(-1, index)
    }

    @Test
    fun findTouchedSliceIndex_outsideRadius_returnsNegativeOne() {
        val index = DonutMath.findTouchedSliceIndex(
            touchX = 300f, touchY = 100f, // far from center
            centerX = 100f, centerY = 100f,
            outerRadius = 80f, holeRadius = 30f,
            sliceValues = listOf(10f, 10f),
            total = 20f, startAngle = 0f,
        )
        assertEquals(-1, index)
    }

    @Test
    fun findTouchedSliceIndex_equalSlices_correctAngleMapping() {
        // 4 equal slices (90° each), startAngle=-90
        // Touch directly to the right (angle=0°), relative to start -90 => 90° => second slice
        val index = DonutMath.findTouchedSliceIndex(
            touchX = 150f, touchY = 100f, // directly right
            centerX = 100f, centerY = 100f,
            outerRadius = 80f, holeRadius = 0f,
            sliceValues = listOf(10f, 10f, 10f, 10f),
            total = 40f, startAngle = -90f,
        )
        assertEquals(1, index)
    }

    @Test
    fun calculateSliceOffset_noScale_zeroOffset() {
        val (ox, oy) = DonutMath.calculateSliceOffset(45f, 1f, 100f)
        assertEquals(0f, ox, 0.001f)
        assertEquals(0f, oy, 0.001f)
    }

    @Test
    fun calculateSliceOffset_withScale_nonZeroOffset() {
        val (ox, oy) = DonutMath.calculateSliceOffset(0f, 1.1f, 100f)
        // offsetDistance = 100 * 0.1 * 2 = 20, cos(0)=1, sin(0)=0
        assertEquals(20f, ox, 0.001f)
        assertEquals(0f, oy, 0.5f) // allow small float imprecision
    }

    @Test
    fun calculateSpacingAngle_singleSlice_returnsZero() {
        val angle = DonutMath.calculateSpacingAngle(1, 5f, 100f)
        assertEquals(0f, angle, 0.001f)
    }

    @Test
    fun calculateSpacingAngle_multipleSlices_correctAngle() {
        val angle = DonutMath.calculateSpacingAngle(3, 5f, 100f)
        val expected = (5f / (2 * Math.PI.toFloat() * 100f)) * 360f
        assertEquals(expected, angle, 0.001f)
    }

    @Test
    fun calculateSpacingAngle_zeroRadius_returnsZero() {
        val angle = DonutMath.calculateSpacingAngle(3, 5f, 0f)
        assertEquals(0f, angle, 0.001f)
    }
}
