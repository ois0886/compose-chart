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
import com.inseong.composechart.data.ChartPoint
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.LineSeries
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.style.AxisStyle
import com.inseong.composechart.style.GridStyle
import com.inseong.composechart.style.LineChartStyle
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * LineChart UI tests.
 *
 * Verifies that the chart renders without crashing for
 * normal data, empty data, and abnormal data (NaN, Infinity, negative).
 */
class LineChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier
        .fillMaxWidth()
        .height(200.dp)

    @Test
    fun lineChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(10f, 25f, 18f, 32f, 28f),
                    xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May"),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_singlePoint_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(42f)),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_emptyData_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData(series = emptyList()),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_emptyPoints_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData(
                    series = listOf(LineSeries(points = emptyList())),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_nanValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(Float.NaN, 10f, Float.NaN, 20f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_infinityValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(Float.POSITIVE_INFINITY, 10f, Float.NEGATIVE_INFINITY),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_negativeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(-10f, -5f, 0f, 5f, 10f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_allZeroValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(0f, 0f, 0f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_allSameValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(50f, 50f, 50f, 50f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_multiSeries_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData(
                    series = listOf(
                        LineSeries(
                            points = listOf(
                                ChartPoint(0f, 10f), ChartPoint(1f, 20f), ChartPoint(2f, 15f),
                            ),
                        ),
                        LineSeries(
                            points = listOf(
                                ChartPoint(0f, 5f), ChartPoint(1f, 15f), ChartPoint(2f, 25f),
                            ),
                        ),
                    ),
                    xLabels = listOf("A", "B", "C"),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_linearMode_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 20f, 15f)),
                modifier = defaultModifier,
                style = LineChartStyle(curved = false),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_withDots_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 20f, 15f)),
                modifier = defaultModifier,
                style = LineChartStyle(showDots = true),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_largeDataset_rendersWithoutCrash() {
        val values = (1..100).map { it.toFloat() }
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = values),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Touch interaction tests ──

    @Test
    fun lineChart_touch_invokesOnPointSelected() {
        var callbackInvoked = false
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(10f, 25f, 18f, 32f),
                    xLabels = listOf("A", "B", "C", "D"),
                ),
                modifier = defaultModifier,
                onPointSelected = { _, _, _ -> callbackInvoked = true },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().performTouchInput { down(center) }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
        assertTrue("onPointSelected should be invoked on touch", callbackInvoked)
    }

    @Test
    fun lineChart_touch_nullCallback_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 25f, 18f)),
                modifier = defaultModifier,
                onPointSelected = null,
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.waitForIdle()
    }

    // ── Style configuration tests ──

    @Test
    fun lineChart_customColors_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 20f, 15f)),
                modifier = defaultModifier,
                colors = listOf(Color.Magenta),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_noGrid_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 20f, 15f)),
                modifier = defaultModifier,
                style = LineChartStyle(
                    grid = GridStyle(showHorizontalLines = false, showVerticalLines = false),
                ),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_noAxes_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 20f, 15f)),
                modifier = defaultModifier,
                style = LineChartStyle(
                    axis = AxisStyle(showXAxis = false, showYAxis = false),
                ),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_noGradient_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 20f, 15f)),
                modifier = defaultModifier,
                style = LineChartStyle(gradientFill = false),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_noTooltip_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 20f, 15f)),
                modifier = defaultModifier,
                style = LineChartStyle(showTooltipOnTouch = false),
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.waitForIdle()
    }

    // ── Data change stability tests ──

    @Test
    fun lineChart_dataChange_rendersWithoutCrash() {
        var data by mutableStateOf(
            LineChartData.fromValues(values = listOf(10f, 20f, 30f)),
        )
        composeTestRule.setContent {
            LineChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = LineChartData.fromValues(values = listOf(50f, 60f))
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_dataChangeToEmpty_rendersWithoutCrash() {
        var data by mutableStateOf(
            LineChartData.fromValues(values = listOf(10f, 20f)),
        )
        composeTestRule.setContent {
            LineChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = LineChartData(series = emptyList())
        composeTestRule.waitForIdle()
    }

    // ── Mixed data tests ──

    @Test
    fun lineChart_mixedNanAndValid_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(Float.NaN, 15f, Float.NaN, 30f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_mixedInfinityAndValid_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(Float.POSITIVE_INFINITY, 15f, Float.NEGATIVE_INFINITY, 30f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_extremeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(1f, 10000f, 0.001f)),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_multiSeries_customColors_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData(
                    series = listOf(
                        LineSeries(
                            points = listOf(ChartPoint(0f, 10f), ChartPoint(1f, 20f)),
                            color = Color.Red,
                        ),
                        LineSeries(
                            points = listOf(ChartPoint(0f, 15f), ChartPoint(1f, 25f)),
                            color = Color.Blue,
                        ),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }
}
