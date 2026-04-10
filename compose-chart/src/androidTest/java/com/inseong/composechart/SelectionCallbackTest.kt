package com.inseong.composechart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.BarEntry
import com.inseong.composechart.data.BarGroup
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.radar.RadarChart
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
        composeTestRule.onRoot().performTouchInput {
            down(Offset(x = 1f, y = centerY))
        }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)

        assertNotNull("LineChart should emit a ChartSelection.Line on touch", selection)
    }

    @Test
    fun barChart_onSelectionChanged_emitsBarSelection() {
        var selection: ChartSelection.Bar? = null
        composeTestRule.setContent {
            BarChart(
                data = BarChartData(
                    groups = listOf(
                        BarGroup(entries = listOf(BarEntry(values = listOf(30f))), label = "A"),
                        BarGroup(entries = listOf(BarEntry(values = listOf(50f))), label = "B"),
                        BarGroup(entries = listOf(BarEntry(values = listOf(40f))), label = "C"),
                    ),
                ),
                modifier = wideModifier,
                onSelectionChanged = { selection = it },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.mainClock.advanceTimeBy(500)

        assertNotNull("BarChart should emit a ChartSelection.Bar on touch", selection)
    }

    @Test
    fun donutChart_onSelectionChanged_emitsDonutSelection() {
        var selection: ChartSelection.Donut? = null
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(60f, "A"),
                        DonutSlice(40f, "B"),
                    ),
                ),
                modifier = squareModifier,
                onSelectionChanged = { selection = it },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.mainClock.advanceTimeBy(500)

        assertNotNull("DonutChart should emit a ChartSelection.Donut on touch", selection)
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
        composeTestRule.onRoot().performTouchInput {
            down(Offset(x = centerX, y = top + 20f))
        }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)

        assertNotNull("RadarChart should emit a ChartSelection.Radar on touch", selection)
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
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.mainClock.advanceTimeBy(500)

        assertNotNull("GaugeChart should emit a ChartSelection.Gauge on touch", selection)
    }
}
