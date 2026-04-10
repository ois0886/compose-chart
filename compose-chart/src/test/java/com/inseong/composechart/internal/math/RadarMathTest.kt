package com.inseong.composechart.internal.math

import org.junit.Assert.assertEquals
import org.junit.Test

class RadarMathTest {

    @Test
    fun polarToCartesian_zeroAngle_returnsRight() {
        val (x, y) = RadarMath.polarToCartesian(100f, 100f, 0f, 50f)
        assertEquals(150f, x, 0.001f)
        assertEquals(100f, y, 0.001f)
    }

    @Test
    fun polarToCartesian_90degrees_returnsBottom() {
        val (x, y) = RadarMath.polarToCartesian(100f, 100f, 90f, 50f)
        assertEquals(100f, x, 0.1f)
        assertEquals(150f, y, 0.1f)
    }

    @Test
    fun polarToCartesian_minus90degrees_returnsTop() {
        val (x, y) = RadarMath.polarToCartesian(100f, 100f, -90f, 50f)
        assertEquals(100f, x, 0.1f)
        assertEquals(50f, y, 0.1f)
    }

    @Test
    fun calculateDataPolygonVertices_allMaxValues_onBoundary() {
        val vertices = RadarMath.calculateDataPolygonVertices(
            values = listOf(100f, 100f, 100f),
            maxValue = 100f, axisCount = 3,
            centerX = 100f, centerY = 100f,
            radius = 50f, startAngle = -90f, progress = 1f,
        )
        assertEquals(3, vertices.size)
        // First vertex at -90°: (100, 50) — top
        assertEquals(100f, vertices[0].first, 0.1f)
        assertEquals(50f, vertices[0].second, 0.1f)
    }

    @Test
    fun calculateDataPolygonVertices_allZeroValues_atCenter() {
        val vertices = RadarMath.calculateDataPolygonVertices(
            values = listOf(0f, 0f, 0f),
            maxValue = 100f, axisCount = 3,
            centerX = 100f, centerY = 100f,
            radius = 50f, startAngle = -90f, progress = 1f,
        )
        vertices.forEach { (x, y) ->
            assertEquals(100f, x, 0.001f)
            assertEquals(100f, y, 0.001f)
        }
    }

    @Test
    fun calculateDataPolygonVertices_halfProgress_halfRadius() {
        val vertices = RadarMath.calculateDataPolygonVertices(
            values = listOf(100f),
            maxValue = 100f, axisCount = 1,
            centerX = 100f, centerY = 100f,
            radius = 50f, startAngle = -90f, progress = 0.5f,
        )
        // At -90°, half progress: y = 100 - 50*0.5 = 75
        assertEquals(100f, vertices[0].first, 0.1f)
        assertEquals(75f, vertices[0].second, 0.1f)
    }

    @Test
    fun calculateDataPolygonVertices_valuesExceedMax_clamped() {
        val vertices = RadarMath.calculateDataPolygonVertices(
            values = listOf(200f), // exceeds max
            maxValue = 100f, axisCount = 1,
            centerX = 100f, centerY = 100f,
            radius = 50f, startAngle = -90f, progress = 1f,
        )
        // Should be clamped to 100/100 = 1.0 * radius
        assertEquals(100f, vertices[0].first, 0.1f)
        assertEquals(50f, vertices[0].second, 0.1f)
    }

    @Test
    fun polarToCartesian_nanRadius_handlesGracefully() {
        val (x, y) = RadarMath.polarToCartesian(100f, 100f, 0f, Float.NaN)
        // NaN 입력 시 크래시 없이 동작 (결과는 NaN일 수 있음)
        assertEquals(true, x.isNaN() || x.isFinite())
    }

    @Test
    fun calculateDataPolygonVertices_zeroMaxValue_handlesGracefully() {
        // maxValue=0은 0 나누기가 발생할 수 있음
        val vertices = RadarMath.calculateDataPolygonVertices(
            values = listOf(10f, 20f, 30f),
            maxValue = 0f, axisCount = 3,
            centerX = 100f, centerY = 100f,
            radius = 50f, startAngle = -90f, progress = 1f,
        )
        assertEquals(3, vertices.size)
    }

    @Test
    fun findNearestAxisIndex_exactVertex_correctIndex() {
        // 4 axes, start=-90, angleStep=90
        // Axis 0 at top (100, 50), Axis 1 at right (150, 100)
        // Touch at (150, 100) => nearest to axis 1
        val index = RadarMath.findNearestAxisIndex(
            touchX = 150f, touchY = 100f,
            axisCount = 4, centerX = 100f, centerY = 100f,
            radius = 50f, startAngle = -90f,
        )
        assertEquals(1, index)
    }

    @Test
    fun findNearestAxisIndex_betweenVertices_nearestReturned() {
        // 4 axes, touch closer to axis 0 (top)
        val index = RadarMath.findNearestAxisIndex(
            touchX = 110f, touchY = 55f,
            axisCount = 4, centerX = 100f, centerY = 100f,
            radius = 50f, startAngle = -90f,
        )
        assertEquals(0, index)
    }

    @Test
    fun findNearestAxisIndex_zeroAxes_returnsNegativeOne() {
        val index = RadarMath.findNearestAxisIndex(
            touchX = 100f, touchY = 100f,
            axisCount = 0, centerX = 100f, centerY = 100f,
            radius = 50f, startAngle = -90f,
        )
        assertEquals(-1, index)
    }

    @Test
    fun calculateAxisLabelPosition_topVertex_startAngleMinus90() {
        // 5 axes, first axis at -90° (top), label sits 20px outside radius 80
        val (x, y) = RadarMath.calculateAxisLabelPosition(
            axisIndex = 0, axisCount = 5,
            centerX = 100f, centerY = 100f,
            radius = 80f, startAngleDegrees = -90f,
            labelOffset = 20f, labelBaselineOffset = 0f,
        )
        assertEquals(100f, x, 0.1f)
        assertEquals(0f, y, 0.1f) // 100 - (80 + 20)
    }

    @Test
    fun calculateAxisLabelPosition_rightVertex_4axesZeroStart() {
        // axisIndex=0 at angle 0° (right), radius+offset = 100
        val (x, y) = RadarMath.calculateAxisLabelPosition(
            axisIndex = 0, axisCount = 4,
            centerX = 100f, centerY = 100f,
            radius = 80f, startAngleDegrees = 0f,
            labelOffset = 20f, labelBaselineOffset = 0f,
        )
        assertEquals(200f, x, 0.1f)
        assertEquals(100f, y, 0.1f)
    }

    @Test
    fun calculateAxisLabelPosition_baselineOffset_isAddedToY() {
        val (_, y) = RadarMath.calculateAxisLabelPosition(
            axisIndex = 0, axisCount = 4,
            centerX = 100f, centerY = 100f,
            radius = 80f, startAngleDegrees = 0f,
            labelOffset = 20f, labelBaselineOffset = 5f,
        )
        assertEquals(105f, y, 0.1f)
    }

    @Test
    fun calculateAxisLabelPosition_zeroAxes_returnsCenter() {
        val (x, y) = RadarMath.calculateAxisLabelPosition(
            axisIndex = 0, axisCount = 0,
            centerX = 100f, centerY = 100f,
            radius = 80f, startAngleDegrees = 0f,
            labelOffset = 20f, labelBaselineOffset = 0f,
        )
        assertEquals(100f, x, 0.001f)
        assertEquals(100f, y, 0.001f)
    }
}
