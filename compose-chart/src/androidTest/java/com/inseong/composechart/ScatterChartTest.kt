package com.inseong.composechart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import com.inseong.composechart.data.ScatterChartData
import com.inseong.composechart.data.ScatterPoint
import com.inseong.composechart.scatter.ScatterChart
import com.inseong.composechart.style.GridStyle
import com.inseong.composechart.style.ScatterChartStyle
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ScatterChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier
        .fillMaxWidth()
        .height(200.dp)

    @Test
    fun scatterChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f, 4f, 5f),
                    yValues = listOf(10f, 25f, 18f, 32f, 22f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_singlePoint_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData(points = listOf(ScatterPoint(1f, 42f))),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_emptyData_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData(points = emptyList()),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_nanValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData(
                    points = listOf(
                        ScatterPoint(Float.NaN, 10f),
                        ScatterPoint(2f, Float.NaN),
                        ScatterPoint(3f, 20f),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_infinityValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData(
                    points = listOf(
                        ScatterPoint(Float.POSITIVE_INFINITY, 10f),
                        ScatterPoint(2f, Float.NEGATIVE_INFINITY),
                        ScatterPoint(3f, 20f),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_allSameValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData.fromValues(
                    xValues = listOf(5f, 5f, 5f),
                    yValues = listOf(10f, 10f, 10f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_negativeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData.fromValues(
                    xValues = listOf(-5f, -2f, 0f, 3f),
                    yValues = listOf(-10f, 5f, -3f, 20f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_touch_invokesOnPointSelected() {
        var callbackInvoked = false
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                ),
                modifier = defaultModifier,
                onPointSelected = { _, _ -> callbackInvoked = true },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().performTouchInput { down(center) }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.waitForIdle()
        assertTrue("onPointSelected should be invoked", callbackInvoked)
    }

    @Test
    fun scatterChart_nullCallback_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                ),
                modifier = defaultModifier,
                onPointSelected = null,
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_dataChange_rendersWithoutCrash() {
        var data by mutableStateOf(
            ScatterChartData.fromValues(
                xValues = listOf(1f, 2f, 3f),
                yValues = listOf(10f, 25f, 18f),
            ),
        )
        composeTestRule.setContent {
            ScatterChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = ScatterChartData.fromValues(
            xValues = listOf(4f, 5f, 6f),
            yValues = listOf(30f, 15f, 42f),
        )
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_customColors_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                ),
                modifier = defaultModifier,
                colors = listOf(Color.Red, Color.Blue, Color.Green),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_noGrid_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                ),
                modifier = defaultModifier,
                style = ScatterChartStyle(
                    grid = GridStyle(showHorizontalLines = false, showVerticalLines = false),
                ),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_extremeRange_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = ScatterChartData.fromValues(
                    xValues = listOf(1f, 500f, 10000f),
                    yValues = listOf(1f, 5000f, 10000f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }
}
