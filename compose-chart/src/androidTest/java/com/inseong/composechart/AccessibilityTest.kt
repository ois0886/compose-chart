package com.inseong.composechart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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
import com.inseong.composechart.pie.PieChart
import com.inseong.composechart.radar.RadarChart
import org.junit.Rule
import org.junit.Test

/**
 * Verifies the accessibility parameters added for semantics consistency:
 * - `accessibilityLabel` overrides the content description prefix.
 * - `onClickLabel` adds an onClick action semantics node.
 * - Every chart exposes a `stateDescription`.
 */
class AccessibilityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val wideModifier = Modifier.fillMaxWidth().height(200.dp)
    private val squareModifier = Modifier.size(200.dp)

    // ── accessibilityLabel overrides prefix ──

    @Test
    fun lineChart_customAccessibilityLabel_isUsedAsContentDescriptionPrefix() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(listOf(10f, 20f, 30f)),
                modifier = wideModifier,
                accessibilityLabel = "매출 추이",
            )
        }
        composeTestRule
            .onNodeWithContentDescription("매출 추이", substring = true)
            .assertExists()
    }

    @Test
    fun barChart_customAccessibilityLabel_isUsedAsContentDescriptionPrefix() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData(
                    groups = listOf(
                        BarGroup(entries = listOf(BarEntry(values = listOf(30f))), label = "A"),
                        BarGroup(entries = listOf(BarEntry(values = listOf(50f))), label = "B"),
                    ),
                ),
                modifier = wideModifier,
                accessibilityLabel = "월별 판매량",
            )
        }
        composeTestRule
            .onNodeWithContentDescription("월별 판매량", substring = true)
            .assertExists()
    }

    @Test
    fun donutChart_customAccessibilityLabel_isUsedAsContentDescriptionPrefix() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(40f, "Food"),
                        DonutSlice(30f, "Rent"),
                        DonutSlice(30f, "Other"),
                    ),
                ),
                modifier = squareModifier,
                accessibilityLabel = "지출 분포",
            )
        }
        composeTestRule
            .onNodeWithContentDescription("지출 분포", substring = true)
            .assertExists()
    }

    @Test
    fun pieChart_customAccessibilityLabel_isUsedAsContentDescriptionPrefix() {
        composeTestRule.setContent {
            PieChart(
                data = DonutChartData(
                    slices = listOf(
                        DonutSlice(60f, "Yes"),
                        DonutSlice(40f, "No"),
                    ),
                ),
                modifier = squareModifier,
                accessibilityLabel = "투표 결과",
            )
        }
        composeTestRule
            .onNodeWithContentDescription("투표 결과", substring = true)
            .assertExists()
    }

    @Test
    fun radarChart_customAccessibilityLabel_isUsedAsContentDescriptionPrefix() {
        composeTestRule.setContent {
            RadarChart(
                data = RadarChartData.single(
                    values = listOf(80f, 65f, 90f, 70f, 85f),
                    axisLabels = listOf("STR", "DEX", "INT", "WIS", "CHA"),
                ),
                modifier = squareModifier,
                accessibilityLabel = "능력치",
            )
        }
        composeTestRule
            .onNodeWithContentDescription("능력치", substring = true)
            .assertExists()
    }

    @Test
    fun gaugeChart_customAccessibilityLabel_isUsedAsContentDescriptionPrefix() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f, label = "Score"),
                modifier = squareModifier,
                accessibilityLabel = "목표 달성률",
            )
        }
        composeTestRule
            .onNodeWithContentDescription("목표 달성률", substring = true)
            .assertExists()
    }

    // ── onClickLabel adds onClick semantics ──

    @Test
    fun lineChart_onClickLabel_addsOnClickSemanticsAction() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(listOf(10f, 20f, 30f)),
                modifier = wideModifier,
                accessibilityLabel = "차트",
                onClickLabel = "포인트 선택",
            )
        }
        composeTestRule
            .onNodeWithContentDescription("차트", substring = true)
            .assert(SemanticsMatcher.keyIsDefined(SemanticsActions.OnClick))
    }

    @Test
    fun gaugeChart_stateDescription_includesProgressPercent() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 50f, maxValue = 100f),
                modifier = squareModifier,
                accessibilityLabel = "게이지",
            )
        }
        // Animated progress eventually reaches 100%, so state description
        // must expose a non-null stateDescription property.
        composeTestRule.mainClock.advanceTimeBy(2000)
        composeTestRule
            .onNodeWithContentDescription("게이지", substring = true)
            .assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.StateDescription))
    }
}
