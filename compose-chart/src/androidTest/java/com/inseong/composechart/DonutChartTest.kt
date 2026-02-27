package com.inseong.composechart

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.style.DonutChartStyle
import org.junit.Assert.assertTrue
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

    // ── Touch interaction tests ──

    @Test
    fun donutChart_touch_pieMode_invokesCallback() {
        var callbackInvoked = false
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 50f, "B" to 50f),
                ),
                modifier = defaultModifier,
                style = DonutChartStyle(holeRadius = 0f),
                onSliceSelected = { _, _ -> callbackInvoked = true },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().performTouchInput { down(center) }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
        assertTrue("onSliceSelected should be invoked on touch in pie mode", callbackInvoked)
    }

    @Test
    fun donutChart_touch_nullCallback_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 50f, "B" to 50f),
                ),
                modifier = defaultModifier,
                onSliceSelected = null,
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.waitForIdle()
    }

    // ── Style configuration tests ──

    @Test
    fun donutChart_customColors_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 60f),
                ),
                modifier = defaultModifier,
                colors = listOf(Color.Cyan, Color.Yellow),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_customStartAngle_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 60f),
                ),
                modifier = defaultModifier,
                style = DonutChartStyle(startAngle = 0f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_noSpacing_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 30f, "C" to 30f),
                ),
                modifier = defaultModifier,
                style = DonutChartStyle(sliceSpacing = 0.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_maxHoleRadius_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 50f, "B" to 50f),
                ),
                modifier = defaultModifier,
                style = DonutChartStyle(holeRadius = 0.95f),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Data change stability tests ──

    @Test
    fun donutChart_dataChange_rendersWithoutCrash() {
        var data by mutableStateOf(
            DonutChartData.fromValues(values = mapOf("A" to 40f, "B" to 60f)),
        )
        composeTestRule.setContent {
            DonutChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = DonutChartData.fromValues(values = mapOf("X" to 10f, "Y" to 20f, "Z" to 70f))
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_dataChangeToEmpty_rendersWithoutCrash() {
        var data by mutableStateOf(
            DonutChartData.fromValues(values = mapOf("A" to 40f)),
        )
        composeTestRule.setContent {
            DonutChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = DonutChartData(slices = emptyList())
        composeTestRule.waitForIdle()
    }

    // ── Mixed data tests ──

    @Test
    fun donutChart_mixedNanAndValid_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(Float.NaN, "Bad"),
                        DonutSlice(30f, "Good"),
                        DonutSlice(Float.NaN, "Bad2"),
                        DonutSlice(70f, "Good2"),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_mixedNegativeAndPositive_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(-10f, "Neg"),
                        DonutSlice(50f, "Pos"),
                        DonutSlice(-5f, "Neg2"),
                        DonutSlice(30f, "Pos2"),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_extremeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(0.001f, "Tiny"),
                        DonutSlice(10000f, "Huge"),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_sliceWithCustomColor_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(40f, "Custom", Color.Red),
                        DonutSlice(60f, "Default"),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }
}
