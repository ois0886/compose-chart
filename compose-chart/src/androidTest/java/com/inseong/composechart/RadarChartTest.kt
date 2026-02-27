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
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.data.RadarEntry
import com.inseong.composechart.radar.RadarChart
import com.inseong.composechart.style.RadarChartStyle
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RadarChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier.size(200.dp)

    private val defaultLabels = listOf("STR", "DEX", "INT", "WIS", "CHA")

    @Test
    fun radarChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(80f, 65f, 90f, 70f, 85f),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_multiSeries_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData(
                    entries = listOf(
                        RadarEntry(values = listOf(80f, 65f, 90f, 70f, 85f), label = "Player 1"),
                        RadarEntry(values = listOf(60f, 85f, 70f, 90f, 50f), label = "Player 2"),
                    ),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_emptyEntries_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData(entries = emptyList(), axisLabels = defaultLabels),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_tooFewAxes_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(80f, 65f),
                    axisLabels = listOf("A", "B"),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_nanValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(Float.NaN, 65f, 90f, Float.NaN, 85f),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_infinityValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(Float.POSITIVE_INFINITY, 65f, 90f, Float.NEGATIVE_INFINITY, 85f),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_allZeroValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(0f, 0f, 0f, 0f, 0f),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_negativeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(-10f, 65f, -30f, 70f, 85f),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_customMaxValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(80f, 65f, 90f, 70f, 85f),
                    axisLabels = defaultLabels,
                    maxValue = 150f,
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_touch_invokesOnAxisSelected() {
        var callbackInvoked = false
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(80f, 65f, 90f, 70f, 85f),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
                onAxisSelected = { _ -> callbackInvoked = true },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().performTouchInput { down(center) }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.waitForIdle()
        assertTrue("onAxisSelected should be invoked", callbackInvoked)
    }

    @Test
    fun radarChart_dataChange_rendersWithoutCrash() {
        var data by mutableStateOf(
            RadarChartData.single(
                values = listOf(80f, 65f, 90f, 70f, 85f),
                axisLabels = defaultLabels,
            ),
        )
        composeTestRule.setContent {
            RadarChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = RadarChartData.single(
            values = listOf(50f, 85f, 60f, 90f, 40f),
            axisLabels = defaultLabels,
        )
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_customColors_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData(
                    entries = listOf(
                        RadarEntry(values = listOf(80f, 65f, 90f, 70f, 85f)),
                        RadarEntry(values = listOf(60f, 85f, 70f, 90f, 50f)),
                    ),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
                colors = listOf(Color.Red, Color.Blue),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_noDots_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(80f, 65f, 90f, 70f, 85f),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
                style = RadarChartStyle(showDots = false),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_customWebLevels_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(80f, 65f, 90f, 70f, 85f),
                    axisLabels = defaultLabels,
                ),
                modifier = defaultModifier,
                style = RadarChartStyle(webLevels = 10),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_manyAxes_rendersWithoutCrash() {
        val labels = (1..12).map { "Axis$it" }
        val values = (1..12).map { it * 10f }
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(values = values, axisLabels = labels),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }
}
