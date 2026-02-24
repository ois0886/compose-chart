package com.inseong.composechart

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.style.GaugeChartStyle
import org.junit.Rule
import org.junit.Test

/**
 * GaugeChart UI 테스트.
 *
 * 정상 데이터, 비정상 데이터(NaN, Infinity, 음수, 0)에 대해
 * 크래시 없이 렌더링되는지 검증한다.
 */
class GaugeChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier.size(180.dp)

    @Test
    fun gaugeChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f, label = "달성률"),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_zeroValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 0f, maxValue = 100f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_maxValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 100f, maxValue = 100f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_overMaxValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 150f, maxValue = 100f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_negativeValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = -30f, maxValue = 100f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_negativeMaxValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 50f, maxValue = -100f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_zeroMaxValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 50f, maxValue = 0f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_nanValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = Float.NaN, maxValue = 100f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_infinityValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = Float.POSITIVE_INFINITY, maxValue = 100f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_nanMaxValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 50f, maxValue = Float.NaN),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_fullCircle_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                style = GaugeChartStyle(sweepAngle = 360f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_halfCircle_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                style = GaugeChartStyle(sweepAngle = 180f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_noCenterText_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                style = GaugeChartStyle(showCenterText = false),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_customCenterContent_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                centerContent = { animatedValue ->
                    BasicText(text = "${animatedValue.toInt()}%")
                },
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_smallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = Modifier.size(30.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_decimalValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 33.7f, maxValue = 100f, label = "점수"),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }
}
