package com.inseong.composechart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.radar.RadarChart
import com.inseong.composechart.style.DonutChartStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

/**
 * Verifies that the new `onSelectionChanged` callbacks emit the correct
 * `ChartSelection.*` subtype when the chart is touched.
 */
class SelectionCallbackTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val wideModifier = Modifier.fillMaxWidth().height(200.dp)
    private val squareModifier = Modifier.size(220.dp)

    @Test
    fun lineChart_onSelectionChanged_emitsLineSelection() {
        var selection: ChartSelection.Line? = null
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = listOf(10f, 25f, 18f, 32f),
                    xLabels = listOf("A", "B", "C", "D"),
                ),
                modifier = wideModifier,
                onSelectionChanged = { selection = it },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule
            .onNodeWithContentDescription("선 차트, 1개 시리즈, 4개 데이터 포인트, 4개 X축 라벨")
            .performTouchInput {
                down(Offset(x = 1f, y = centerY))
            }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.runOnIdle {
            assertNotNull("LineChart should emit a ChartSelection.Line on touch", selection)
        }
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
    }

    @Test
    fun lineChart_onSelectionChanged_sameTouchRecomposition_doesNotRepeat() {
        var callbackCount = 0
        var recompositionTick by mutableIntStateOf(0)

        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(values = listOf(10f, 25f, 18f)),
                modifier = wideModifier,
                accessibilityLabel = "선 차트 $recompositionTick",
                onSelectionChanged = { callbackCount++ },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule
            .onNodeWithContentDescription("선 차트 0, 1개 시리즈, 3개 데이터 포인트")
            .performTouchInput {
                down(Offset(x = 1f, y = centerY))
            }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.runOnIdle {
            assertEquals(1, callbackCount)
            recompositionTick = 1
        }
        composeTestRule.waitForIdle()
        composeTestRule.runOnIdle {
            assertEquals("Selection callback should not repeat for the same touch selection", 1, callbackCount)
        }
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
    }

    @Test
    fun barChart_onSelectionChanged_emitsBarSelection() {
        var selection: ChartSelection.Bar? = null
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = listOf(30f, 45f, 28f),
                    labels = listOf("A", "B", "C"),
                ),
                modifier = wideModifier,
                onSelectionChanged = { selection = it },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule
            .onNodeWithContentDescription("막대 차트, 3개 그룹")
            .performTouchInput {
                down(Offset(x = 1f, y = centerY))
            }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.runOnIdle {
            assertNotNull("BarChart should emit a ChartSelection.Bar on touch", selection)
        }
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
    }

    @Test
    fun donutChart_onSelectionChanged_emitsDonutSelection() {
        var selection: ChartSelection.Donut? = null
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(values = mapOf("Only" to 100f)),
                modifier = squareModifier,
                style = DonutChartStyle(holeRadius = 0f),
                onSelectionChanged = { selection = it },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule
            .onNodeWithContentDescription("도넛 차트, 1개 항목, 총합 100")
            .performTouchInput { down(center) }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.runOnIdle {
            assertNotNull("DonutChart should emit a ChartSelection.Donut on touch", selection)
        }
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
    }

    @Test
    fun radarChart_onSelectionChanged_emitsRadarSelection() {
        var selection: ChartSelection.Radar? = null
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(80f, 65f, 90f, 70f, 85f),
                    axisLabels = listOf("STR", "DEX", "INT", "WIS", "CHA"),
                ),
                modifier = squareModifier,
                onSelectionChanged = { selection = it },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule
            .onNodeWithContentDescription("레이더 차트, 5개 축, 1개 시리즈")
            .performTouchInput {
                down(Offset(x = centerX, y = top + 20f))
            }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.runOnIdle {
            assertNotNull("RadarChart should emit a ChartSelection.Radar on touch", selection)
        }
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
    }

    @Test
    fun gaugeChart_onSelectionChanged_emitsGaugeSelection() {
        var selection: ChartSelection.Gauge? = null
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f, label = "Score"),
                modifier = squareModifier,
                onSelectionChanged = { selection = it },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1500)
        composeTestRule
            .onNodeWithContentDescription("게이지", substring = true)
            .performTouchInput { down(center) }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.runOnIdle {
            assertNotNull("GaugeChart should emit a ChartSelection.Gauge on touch", selection)
        }
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
    }
}
