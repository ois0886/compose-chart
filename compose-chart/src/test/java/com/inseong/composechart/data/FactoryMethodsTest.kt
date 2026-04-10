package com.inseong.composechart.data

import org.junit.Assert.assertEquals
import org.junit.Test

class FactoryMethodsTest {

    @Test
    fun lineChartData_fromValues_createsIndexedPoints() {
        val data = LineChartData.fromValues(
            values = listOf(10f, 20f, 30f),
            xLabels = listOf("Jan", "Feb", "Mar"),
            label = "Revenue",
        )

        assertEquals(1, data.series.size)
        assertEquals("Revenue", data.series[0].label)
        assertEquals(0f, data.series[0].points[0].x, 0.001f)
        assertEquals(10f, data.series[0].points[0].y, 0.001f)
        assertEquals(2f, data.series[0].points[2].x, 0.001f)
        assertEquals(30f, data.series[0].points[2].y, 0.001f)
        assertEquals(listOf("Jan", "Feb", "Mar"), data.xLabels)
    }

    @Test
    fun barChartData_grouped_createsCorrectStructure() {
        val data = BarChartData.grouped(
            seriesValues = listOf(
                listOf(10f, 20f, 30f),
                listOf(15f, 25f, 35f),
            ),
            labels = listOf("Jan", "Feb", "Mar"),
        )
        assertEquals(3, data.groups.size)
        assertEquals(2, data.groups[0].entries.size)
        assertEquals(10f, data.groups[0].entries[0].values[0], 0.001f)
        assertEquals(15f, data.groups[0].entries[1].values[0], 0.001f)
        assertEquals("Feb", data.groups[1].label)
    }

    @Test
    fun lineChartData_fromMap_createsMultiSeries() {
        val data = LineChartData.fromMap(
            seriesMap = mapOf(
                "Revenue" to listOf(10f, 20f, 30f),
                "Cost" to listOf(5f, 15f, 25f),
            ),
            xLabels = listOf("Q1", "Q2", "Q3"),
        )
        assertEquals(2, data.series.size)
        assertEquals(3, data.series[0].points.size)
        assertEquals(3, data.xLabels.size)
    }

    @Test
    fun donutChartData_fromValues_vararg_autoGeneratesLabels() {
        val data = DonutChartData.fromValues(40f, 25f, 20f)
        assertEquals(3, data.slices.size)
        assertEquals("Item 1", data.slices[0].label)
        assertEquals("Item 2", data.slices[1].label)
        assertEquals(25f, data.slices[1].value, 0.001f)
    }

    @Test
    fun barChartData_simple_emptyValues_createsEmptyGroups() {
        val data = BarChartData.simple(values = emptyList())
        assertEquals(0, data.groups.size)
    }

    @Test
    fun barChartData_grouped_emptySeriesValues_createsEmptyGroups() {
        val data = BarChartData.grouped(seriesValues = emptyList())
        assertEquals(0, data.groups.size)
    }

    @Test
    fun barChartData_simple_singleValue_createsOneGroup() {
        val data = BarChartData.simple(values = listOf(42f))
        assertEquals(1, data.groups.size)
        assertEquals(42f, data.groups[0].entries[0].values[0], 0.001f)
    }

    @Test
    fun lineChartData_fromMap_emptyMap_createsEmptySeries() {
        val data = LineChartData.fromMap(seriesMap = emptyMap())
        assertEquals(0, data.series.size)
    }

    @Test
    fun donutChartData_fromValues_singleValue_createsOneSlice() {
        val data = DonutChartData.fromValues(100f)
        assertEquals(1, data.slices.size)
        assertEquals(100f, data.slices[0].value, 0.001f)
    }

    @Test
    fun donutChartData_fromValues_map_preservesLabelsAndValues() {
        val data = DonutChartData.fromValues(
            values = linkedMapOf(
                "Food" to 40f,
                "Transport" to 25f,
            ),
        )

        assertEquals(2, data.slices.size)
        assertEquals("Food", data.slices[0].label)
        assertEquals(40f, data.slices[0].value, 0.001f)
        assertEquals("Transport", data.slices[1].label)
        assertEquals(25f, data.slices[1].value, 0.001f)
    }
}
