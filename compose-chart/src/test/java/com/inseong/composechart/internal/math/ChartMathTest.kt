package com.inseong.composechart.internal.math

import org.junit.Assert.assertEquals
import org.junit.Test

class ChartMathTest {

    @Test
    fun calculateXYRange_normalData_returnsCorrectRange() {
        val range = ChartMath.calculateXYRange(
            xValues = listOf(0f, 1f, 2f, 3f),
            yValues = listOf(10f, 20f, 30f, 40f),
        )
        assertEquals(0f, range.minX, 0.001f)
        assertEquals(3f, range.maxX, 0.001f)
        assertEquals(3f, range.xRange, 0.001f)
        // Y range = 30, padding = 3
        assertEquals(7f, range.adjustedMinY, 0.001f)
        assertEquals(43f, range.adjustedMaxY, 0.001f)
    }

    @Test
    fun calculateXYRange_singlePoint_rangeIsOne() {
        val range = ChartMath.calculateXYRange(
            xValues = listOf(5f),
            yValues = listOf(10f),
        )
        assertEquals(1f, range.xRange, 0.001f)
        // yRange=0 -> forced to 1, padding=0.1
        assertEquals(9.9f, range.adjustedMinY, 0.001f)
        assertEquals(10.1f, range.adjustedMaxY, 0.001f)
    }

    @Test
    fun calculateXYRange_negativeValues_handlesCorrectly() {
        val range = ChartMath.calculateXYRange(
            xValues = listOf(-5f, 5f),
            yValues = listOf(-10f, 10f),
        )
        assertEquals(-5f, range.minX, 0.001f)
        assertEquals(5f, range.maxX, 0.001f)
        assertEquals(10f, range.xRange, 0.001f)
        // Y range = 20, padding = 2
        assertEquals(-12f, range.adjustedMinY, 0.001f)
        assertEquals(12f, range.adjustedMaxY, 0.001f)
    }

    @Test
    fun calculateXYRange_emptyLists_returnsZeroDefaults() {
        val range = ChartMath.calculateXYRange(
            xValues = emptyList(),
            yValues = emptyList(),
        )
        assertEquals(0f, range.minX, 0.001f)
        assertEquals(0f, range.maxX, 0.001f)
        assertEquals(1f, range.xRange, 0.001f) // 0-0 = 0, forced to 1
    }

    @Test
    fun mapToCanvas_mapsCorrectly() {
        val range = XYRange(
            minX = 0f, maxX = 10f, xRange = 10f,
            adjustedMinY = 0f, adjustedMaxY = 100f,
        )
        val (cx, cy) = ChartMath.mapToCanvas(
            dataX = 5f, dataY = 50f,
            range = range,
            chartLeft = 0f, chartBottom = 200f,
            chartWidth = 100f, chartHeight = 200f,
        )
        assertEquals(50f, cx, 0.001f)  // (5-0)/10 * 100
        assertEquals(100f, cy, 0.001f) // 200 - (50/100) * 200
    }

    @Test
    fun mapToCanvas_singlePointRange_noNaN() {
        val range = XYRange(
            minX = 5f, maxX = 5f, xRange = 1f,
            adjustedMinY = 9.9f, adjustedMaxY = 10.1f,
        )
        val (cx, cy) = ChartMath.mapToCanvas(
            dataX = 5f, dataY = 10f,
            range = range,
            chartLeft = 10f, chartBottom = 200f,
            chartWidth = 100f, chartHeight = 200f,
        )
        // Should not be NaN
        assertEquals(false, cx.isNaN())
        assertEquals(false, cy.isNaN())
    }

    @Test
    fun formatValue_integerValue_noDecimal() {
        assertEquals("25", ChartMath.formatValue(25.0f))
        assertEquals("0", ChartMath.formatValue(0.0f))
        assertEquals("-3", ChartMath.formatValue(-3.0f))
    }

    @Test
    fun formatValue_floatValue_oneDecimal() {
        assertEquals("25.3", ChartMath.formatValue(25.3f))
        assertEquals("0.1", ChartMath.formatValue(0.1f))
    }

    @Test
    fun calculateXYRange_manualYAxisOverrides() {
        val range = ChartMath.calculateXYRange(
            xValues = listOf(0f, 1f, 2f),
            yValues = listOf(10f, 20f, 30f),
            yAxisMin = 0f,
            yAxisMax = 50f,
        )
        assertEquals(0f, range.adjustedMinY, 0.001f)
        assertEquals(50f, range.adjustedMaxY, 0.001f)
    }

    @Test
    fun calculateXYRange_partialOverride_onlyMin() {
        val range = ChartMath.calculateXYRange(
            xValues = listOf(0f, 1f),
            yValues = listOf(10f, 20f),
            yAxisMin = 0f,
            yAxisMax = null,
        )
        assertEquals(0f, range.adjustedMinY, 0.001f)
        // maxY auto: 20 + 10*0.1 = 21
        assertEquals(21f, range.adjustedMaxY, 0.001f)
    }

    @Test
    fun calculateXYRange_invalidOverride_fallbackToAuto() {
        val range = ChartMath.calculateXYRange(
            xValues = listOf(0f, 1f),
            yValues = listOf(10f, 20f),
            yAxisMin = 50f,
            yAxisMax = 5f,
        )
        // Should fallback to auto range since min >= max
        assertEquals(true, range.adjustedMinY < range.adjustedMaxY)
    }
}
