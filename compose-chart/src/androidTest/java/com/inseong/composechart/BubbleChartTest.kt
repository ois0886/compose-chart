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
import com.inseong.composechart.bubble.BubbleChart
import com.inseong.composechart.data.BubbleChartData
import com.inseong.composechart.data.BubblePoint
import com.inseong.composechart.style.BubbleChartStyle
import com.inseong.composechart.style.GridStyle
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BubbleChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier
        .fillMaxWidth()
        .height(200.dp)

    @Test
    fun bubbleChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f, 4f),
                    yValues = listOf(10f, 25f, 18f, 32f),
                    sizes = listOf(5f, 15f, 10f, 20f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_singlePoint_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData(points = listOf(BubblePoint(1f, 42f, 10f))),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_emptyData_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData(points = emptyList()),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_nanValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData(
                    points = listOf(
                        BubblePoint(Float.NaN, 10f, 5f),
                        BubblePoint(2f, Float.NaN, 10f),
                        BubblePoint(3f, 20f, Float.NaN),
                        BubblePoint(4f, 30f, 15f),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_infinityValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData(
                    points = listOf(
                        BubblePoint(Float.POSITIVE_INFINITY, 10f, 5f),
                        BubblePoint(2f, Float.NEGATIVE_INFINITY, 10f),
                        BubblePoint(3f, 20f, 15f),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_zeroSizes_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                    sizes = listOf(0f, 0f, 0f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_allSameValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData.fromValues(
                    xValues = listOf(5f, 5f, 5f),
                    yValues = listOf(10f, 10f, 10f),
                    sizes = listOf(10f, 10f, 10f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_touch_invokesOnBubbleSelected() {
        var callbackInvoked = false
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                    sizes = listOf(5f, 15f, 10f),
                ),
                modifier = defaultModifier,
                onBubbleSelected = { _, _ -> callbackInvoked = true },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.mainClock.advanceTimeBy(1000)
        composeTestRule.onRoot().performTouchInput { down(center) }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.onRoot().performTouchInput { up() }
        composeTestRule.mainClock.advanceTimeBy(500)
        assertTrue("onBubbleSelected should be invoked", callbackInvoked)
    }

    @Test
    fun bubbleChart_dataChange_rendersWithoutCrash() {
        var data by mutableStateOf(
            BubbleChartData.fromValues(
                xValues = listOf(1f, 2f, 3f),
                yValues = listOf(10f, 25f, 18f),
                sizes = listOf(5f, 15f, 10f),
            ),
        )
        composeTestRule.setContent {
            BubbleChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = BubbleChartData.fromValues(
            xValues = listOf(4f, 5f, 6f),
            yValues = listOf(30f, 15f, 42f),
            sizes = listOf(20f, 5f, 30f),
        )
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_customColors_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                    sizes = listOf(5f, 15f, 10f),
                ),
                modifier = defaultModifier,
                colors = listOf(Color.Red, Color.Blue),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_customAlpha_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                    sizes = listOf(5f, 15f, 10f),
                ),
                modifier = defaultModifier,
                style = BubbleChartStyle(bubbleAlpha = 0.3f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_noGrid_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = BubbleChartData.fromValues(
                    xValues = listOf(1f, 2f, 3f),
                    yValues = listOf(10f, 25f, 18f),
                    sizes = listOf(5f, 15f, 10f),
                ),
                modifier = defaultModifier,
                style = BubbleChartStyle(
                    grid = GridStyle(showHorizontalLines = false, showVerticalLines = false),
                ),
            )
        }
        composeTestRule.waitForIdle()
    }
}
