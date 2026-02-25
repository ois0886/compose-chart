package com.inseong.composechart

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.style.DonutChartStyle
import org.junit.Rule
import org.junit.Test

/**
 * DonutChart UI tests.
 *
 * Verifies that the chart renders without crashing for
 * normal data, empty data, and abnormal data (NaN, negative, 0).
 */
class DonutChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier.size(200.dp)

    @Test
    fun donutChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 30f, "C" to 20f, "D" to 10f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_singleSlice_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(slices = listOf(DonutSlice(100f, "Only"))),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_emptyData_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(slices = emptyList()),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_allZeroValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(DonutSlice(0f, "A"), DonutSlice(0f, "B")),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_negativeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(DonutSlice(-10f, "Negative"), DonutSlice(30f, "Positive")),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_nanValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(DonutSlice(Float.NaN, "NaN"), DonutSlice(50f, "Normal")),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_infinityValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(Float.POSITIVE_INFINITY, "Inf"),
                        DonutSlice(50f, "Normal"),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_donutMode_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("Food" to 40f, "Transport" to 25f, "Shopping" to 20f, "Other" to 15f),
                ),
                modifier = defaultModifier,
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_smallChart_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 30f, "B" to 70f),
                ),
                modifier = Modifier.size(50.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_manySlices_rendersWithoutCrash() {
        val slices = (1..20).map { DonutSlice(it.toFloat(), "Item$it") }
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(slices = slices),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_verySmallSlices_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(0.001f, "Tiny"),
                        DonutSlice(99.999f, "Most"),
                    ),
                ),
                modifier = defaultModifier,
                style = DonutChartStyle(holeRadius = 0.6f, showLabels = true),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_noLabels_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(DonutSlice(40f), DonutSlice(60f)),
                ),
                modifier = defaultModifier,
                style = DonutChartStyle(showLabels = false),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_allNegative_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(DonutSlice(-10f, "A"), DonutSlice(-20f, "B")),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }
}
