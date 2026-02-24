package com.inseong.composechart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.BarEntry
import com.inseong.composechart.data.BarGroup
import com.inseong.composechart.style.BarChartStyle
import org.junit.Rule
import org.junit.Test

/**
 * BarChart UI 테스트.
 *
 * 정상 데이터, 빈 데이터, 비정상 데이터(NaN, 음수)에 대해
 * 크래시 없이 렌더링되는지 검증한다.
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
                    labels = listOf("1월", "2월", "3월", "4월"),
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
                    groups = listOf(BarGroup(entries = emptyList(), label = "빈")),
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
                            label = "그룹A",
                        ),
                        BarGroup(
                            entries = listOf(
                                BarEntry(values = listOf(25f)),
                                BarEntry(values = listOf(35f)),
                            ),
                            label = "그룹B",
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
}
