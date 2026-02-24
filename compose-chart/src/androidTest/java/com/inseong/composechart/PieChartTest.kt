package com.inseong.composechart

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.inseong.composechart.data.PieChartData
import com.inseong.composechart.data.PieSlice
import com.inseong.composechart.pie.PieChart
import com.inseong.composechart.style.PieChartStyle
import org.junit.Rule
import org.junit.Test

/**
 * PieChart(파이/도넛) UI 테스트.
 *
 * 정상 데이터, 빈 데이터, 비정상 데이터(NaN, 음수, 0)에 대해
 * 크래시 없이 렌더링되는지 검증한다.
 */
class PieChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier.size(200.dp)

    @Test
    fun pieChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData.fromValues(
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
                data = PieChartData(slices = listOf(PieSlice(100f, "유일"))),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_emptyData_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData(slices = emptyList()),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_allZeroValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData(
                    slices = listOf(PieSlice(0f, "A"), PieSlice(0f, "B")),
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
                data = PieChartData(
                    slices = listOf(PieSlice(-10f, "음수"), PieSlice(30f, "양수")),
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
                data = PieChartData(
                    slices = listOf(PieSlice(Float.NaN, "NaN"), PieSlice(50f, "정상")),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_infinityValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData(
                    slices = listOf(
                        PieSlice(Float.POSITIVE_INFINITY, "Inf"),
                        PieSlice(50f, "정상"),
                    ),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_donutMode_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData.fromValues(
                    values = mapOf("식비" to 40f, "교통" to 25f, "쇼핑" to 20f, "기타" to 15f),
                ),
                modifier = defaultModifier,
                style = PieChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_smallChart_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData.fromValues(
                    values = mapOf("A" to 30f, "B" to 70f),
                ),
                modifier = Modifier.size(50.dp),
                style = PieChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_manySlices_rendersWithoutCrash() {
        val slices = (1..20).map { PieSlice(it.toFloat(), "항목$it") }
        composeTestRule.setContent {
            PieChart(
                data = PieChartData(slices = slices),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_verySmallSlices_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData(
                    slices = listOf(
                        PieSlice(0.001f, "극소"),
                        PieSlice(99.999f, "대부분"),
                    ),
                ),
                modifier = defaultModifier,
                style = PieChartStyle(holeRadius = 0.6f, showLabels = true),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_noLabels_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData(
                    slices = listOf(PieSlice(40f), PieSlice(60f)),
                ),
                modifier = defaultModifier,
                style = PieChartStyle(showLabels = false),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_allNegative_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData(
                    slices = listOf(PieSlice(-10f, "A"), PieSlice(-20f, "B")),
                ),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }
}
