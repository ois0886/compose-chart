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
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.BarEntry
import com.inseong.composechart.data.BarGroup
import com.inseong.composechart.style.AxisStyle
import com.inseong.composechart.style.BarChartStyle
import com.inseong.composechart.style.GridStyle
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * BarChart UI tests.
 *
 * Verifies that the chart renders without crashing for
 * normal data, empty data, and abnormal data (NaN, negative).
 */
class BarChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier
        .fillMaxWidth()
        .height(200.dp)

    @Test
    fun barChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = listOf(30f, 45f, 28f, 55f),
                    labels = listOf("Jan", "Feb", "Mar", "Apr"),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_singleBar_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(42f)),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_emptyData_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData(groups = emptyList()),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_emptyEntries_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData(
                    groups = listOf(BarGroup(entries = emptyList(), label = "Empty")),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_nanValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = listOf(Float.NaN, 10f, Float.NaN),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_negativeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = listOf(-10f, -5f, 0f, 5f, 10f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_infinityValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = listOf(Float.POSITIVE_INFINITY, 10f, Float.NEGATIVE_INFINITY),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_allZeroValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(0f, 0f, 0f)),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_stackedBars_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData(
                    groups = listOf(
                        BarGroup(
                            entries = listOf(BarEntry(values = listOf(20f, 15f, 10f))),
                            label = "A",
                        ),
                        BarGroup(
                            entries = listOf(BarEntry(values = listOf(30f, 20f))),
                            label = "B",
                        ),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_groupedBars_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData(
                    groups = listOf(
                        BarGroup(
                            entries = listOf(
                                BarEntry(values = listOf(20f)),
                                BarEntry(values = listOf(30f)),
                            ),
                            label = "GroupA",
                        ),
                        BarGroup(
                            entries = listOf(
                                BarEntry(values = listOf(25f)),
                                BarEntry(values = listOf(35f)),
                            ),
                            label = "GroupB",
                        ),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_horizontalMode_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(30f, 45f, 28f)),
                modifier = defaultModifier,
                style = BarChartStyle(horizontal = true),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_noCornerRadius_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(30f, 45f, 28f)),
                modifier = defaultModifier,
                style = BarChartStyle(cornerRadius = 0.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_largeDataset_rendersWithoutCrash() {
        val values = (1..50).map { it.toFloat() }
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = values),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Touch interaction tests ──

    @Test
    fun barChart_touch_invokesOnBarSelected() {
        var callbackInvoked = false
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = listOf(30f, 45f, 28f),
                    labels = listOf("A", "B", "C"),
                ),
                modifier = defaultModifier,
                onBarSelected = { _, _, _ -> callbackInvoked = true },
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.waitForIdle()
        assertTrue("onBarSelected should be invoked on touch", callbackInvoked)
    }

    @Test
    fun barChart_touch_nullCallback_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(30f, 45f, 28f)),
                modifier = defaultModifier,
                onBarSelected = null,
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.waitForIdle()
    }

    // ── Style configuration tests ──

    @Test
    fun barChart_customColors_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(30f, 45f, 28f)),
                modifier = defaultModifier,
                colors = listOf(Color.Red, Color.Green, Color.Blue),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_noGrid_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(30f, 45f)),
                modifier = defaultModifier,
                style = BarChartStyle(
                    grid = GridStyle(showHorizontalLines = false, showVerticalLines = false),
                ),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_noAxes_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(30f, 45f)),
                modifier = defaultModifier,
                style = BarChartStyle(
                    axis = AxisStyle(showXAxis = false, showYAxis = false),
                ),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_noHighlight_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(30f, 45f, 28f)),
                modifier = defaultModifier,
                style = BarChartStyle(highlightOnTouch = false),
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performTouchInput { down(center); up() }
        composeTestRule.waitForIdle()
    }

    // ── Data change stability tests ──

    @Test
    fun barChart_dataChange_rendersWithoutCrash() {
        var data by mutableStateOf(
            BarChartData.simple(values = listOf(10f, 20f, 30f)),
        )
        composeTestRule.setContent {
            BarChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = BarChartData.simple(values = listOf(50f, 60f))
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_dataChangeToEmpty_rendersWithoutCrash() {
        var data by mutableStateOf(
            BarChartData.simple(values = listOf(10f, 20f)),
        )
        composeTestRule.setContent {
            BarChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = BarChartData(groups = emptyList())
        composeTestRule.waitForIdle()
    }

    // ── Mixed data tests ──

    @Test
    fun barChart_mixedNanAndValid_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = listOf(Float.NaN, 30f, Float.NaN, 45f, Float.NaN),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_mixedInfinityAndValid_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = listOf(Float.POSITIVE_INFINITY, 30f, Float.NEGATIVE_INFINITY, 45f),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_extremeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(1f, 10000f, 0.001f)),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_singleColor_manyBars_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(values = listOf(10f, 20f, 30f, 40f, 50f)),
                modifier = defaultModifier,
                colors = listOf(Color.Blue),
            )
        }
        composeTestRule.waitForIdle()
    }
}
