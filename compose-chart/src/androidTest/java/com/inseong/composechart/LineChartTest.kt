package com.inseong.composechart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.inseong.composechart.data.ChartPoint
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.LineSeries
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.style.LineChartStyle
import org.junit.Rule
import org.junit.Test

/**
 * LineChart UI 테스트.
 *
 * 정상 데이터, 빈 데이터, 비정상 데이터(NaN, Infinity, 음수)에 대해
 * 크래시 없이 렌더링되는지 검증한다.
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
                    xLabels = listOf("1월", "2월", "3월", "4월", "5월"),
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
}
