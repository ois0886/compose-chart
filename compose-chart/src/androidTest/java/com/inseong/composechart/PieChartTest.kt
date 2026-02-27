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
import com.inseong.composechart.pie.PieChart
import com.inseong.composechart.style.PieChartStyle
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PieChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier.size(200.dp)

    @Test
    fun pieChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 30f, "C" to 20f, "D" to 10f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_singleSlice_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData(slices = listOf(DonutSlice(100f, "Only"))),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_emptyData_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData(slices = emptyList()),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_allZeroValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData(
                    slices = listOf(DonutSlice(0f, "A"), DonutSlice(0f, "B")),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_negativeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(-10f, "Neg"),
                        DonutSlice(30f, "Pos"),
                        DonutSlice(-5f, "Neg2"),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_nanValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(Float.NaN, "NaN"),
                        DonutSlice(30f, "Valid"),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_touch_invokesOnSliceSelected() {
        var callbackInvoked = false
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 30f, "C" to 30f),
                ),
                modifier = defaultModifier,
                onSliceSelected = { _, _ -> callbackInvoked = true },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.waitForIdle()
        assertTrue("onSliceSelected should be invoked", callbackInvoked)
    }

    @Test
    fun pieChart_dataChange_rendersWithoutCrash() {
        var data by mutableStateOf(
            DonutChartData.fromValues(values = mapOf("A" to 40f, "B" to 60f)),
        )
        composeTestRule.setContent {
            PieChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = DonutChartData.fromValues(values = mapOf("X" to 25f, "Y" to 75f))
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_customColors_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 30f, "C" to 30f),
                ),
                modifier = defaultModifier,
                colors = listOf(Color.Red, Color.Blue, Color.Green),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_customStartAngle_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 30f, "C" to 30f),
                ),
                modifier = defaultModifier,
                style = PieChartStyle(startAngle = 0f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_noLabels_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData.fromValues(
                    values = mapOf("A" to 40f, "B" to 30f, "C" to 30f),
                ),
                modifier = defaultModifier,
                style = PieChartStyle(showLabels = false),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_manySlices_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData.fromValues(
                    values = (1..20).associate { "Item$it" to it.toFloat() },
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_extremeRange_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData.fromValues(
                    values = mapOf("Min" to 1f, "Max" to 10000f, "Mid" to 500f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }
}
