package com.inseong.composechart.data

import org.junit.Assert.assertEquals
import org.junit.Test

class FactoryMethodsTest {

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
}
