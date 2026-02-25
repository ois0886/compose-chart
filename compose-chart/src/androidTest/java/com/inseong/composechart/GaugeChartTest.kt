package com.inseong.composechart

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.style.GaugeChartStyle
import org.junit.Rule
import org.junit.Test

/**
 * GaugeChart UI tests.
 *
 * Verifies that the chart renders without crashing for
 * normal data and abnormal data (NaN, Infinity, negative, 0).
 */
class GaugeChartTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val defaultModifier = Modifier.size(180.dp)

    @Test
    fun gaugeChart_normalData_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f, label = "Progress"),
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
                data = GaugeChartData(value = 33.7f, maxValue = 100f, label = "Score"),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Style configuration tests ──

    @Test
    fun gaugeChart_customProgressColor_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                style = GaugeChartStyle(progressColor = Color.Red),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_customStrokeWidth_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                style = GaugeChartStyle(strokeWidth = 24.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_flatCap_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                style = GaugeChartStyle(roundCap = false),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_customTextSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                style = GaugeChartStyle(centerTextSize = 32.sp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_narrowSweepAngle_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f),
                modifier = defaultModifier,
                style = GaugeChartStyle(sweepAngle = 90f),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Data change stability tests ──

    @Test
    fun gaugeChart_dataChange_rendersWithoutCrash() {
        var data by mutableStateOf(GaugeChartData(value = 30f, maxValue = 100f))
        composeTestRule.setContent {
            GaugeChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = GaugeChartData(value = 90f, maxValue = 200f, label = "Updated")
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_dataChangeToInvalid_rendersWithoutCrash() {
        var data by mutableStateOf(GaugeChartData(value = 50f, maxValue = 100f))
        composeTestRule.setContent {
            GaugeChart(data = data, modifier = defaultModifier)
        }
        composeTestRule.waitForIdle()
        data = GaugeChartData(value = Float.NaN, maxValue = Float.NaN)
        composeTestRule.waitForIdle()
    }

    // ── Mixed/extreme data tests ──

    @Test
    fun gaugeChart_verySmallValue_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 0.001f, maxValue = 10000f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_veryLargeValues_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 999999f, maxValue = 1000000f),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_noLabel_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 50f, maxValue = 100f, label = ""),
                modifier = defaultModifier,
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_tinySize_hidesText_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = GaugeChartData(value = 72f, maxValue = 100f, label = "Score"),
                modifier = Modifier.size(20.dp),
            )
        }
        composeTestRule.waitForIdle()
    }
}
