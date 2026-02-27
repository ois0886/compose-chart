package com.inseong.composechart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.bubble.BubbleChart
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.BubbleChartData
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.data.ScatterChartData
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.pie.PieChart
import com.inseong.composechart.radar.RadarChart
import com.inseong.composechart.scatter.ScatterChart
import com.inseong.composechart.style.DonutChartStyle
import com.inseong.composechart.style.GaugeChartStyle
import org.junit.Rule
import org.junit.Test

/**
 * Stability tests for chart components under various sizes.
 *
 * Verifies that charts render without crashing at
 * very small, very large, and asymmetric sizes.
 */
class ChartSizeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val lineData = LineChartData.fromValues(
        values = listOf(10f, 25f, 18f, 32f),
        xLabels = listOf("A", "B", "C", "D"),
    )
    private val barData = BarChartData.simple(
        values = listOf(30f, 45f, 28f),
        labels = listOf("Jan", "Feb", "Mar"),
    )
    private val donutData = DonutChartData.fromValues(
        values = mapOf("Food" to 40f, "Transport" to 30f, "Other" to 30f),
    )
    private val gaugeData = GaugeChartData(value = 72f, maxValue = 100f, label = "Score")
    private val scatterData = ScatterChartData.fromValues(
        xValues = listOf(1f, 2f, 3f, 4f),
        yValues = listOf(10f, 25f, 18f, 32f),
    )
    private val bubbleData = BubbleChartData.fromValues(
        xValues = listOf(1f, 2f, 3f),
        yValues = listOf(10f, 25f, 18f),
        sizes = listOf(5f, 15f, 10f),
    )
    private val radarData = RadarChartData.single(
        values = listOf(80f, 65f, 90f, 70f, 85f),
        axisLabels = listOf("A", "B", "C", "D", "E"),
    )
    private val pieData = DonutChartData.fromValues(
        values = mapOf("Food" to 40f, "Transport" to 30f, "Other" to 30f),
    )

    // ── Very small sizes ──

    @Test
    fun lineChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = lineData,
                modifier = Modifier.size(10.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = barData,
                modifier = Modifier.size(10.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = donutData,
                modifier = Modifier.size(10.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = gaugeData,
                modifier = Modifier.size(10.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── 1dp size (extreme) ──

    @Test
    fun lineChart_1dp_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = lineData,
                modifier = Modifier.size(1.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_1dp_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = barData,
                modifier = Modifier.size(1.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_1dp_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = donutData,
                modifier = Modifier.size(1.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_1dp_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = gaugeData,
                modifier = Modifier.size(1.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Very large sizes ──

    @Test
    fun lineChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = lineData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2000.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = barData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2000.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = donutData,
                modifier = Modifier.size(1000.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = gaugeData,
                modifier = Modifier.size(1000.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Asymmetric sizes (very wide + short / narrow + tall) ──

    @Test
    fun lineChart_wideAndShort_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = lineData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_narrowAndTall_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = lineData,
                modifier = Modifier
                    .width(10.dp)
                    .height(500.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_wideAndShort_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = barData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun barChart_narrowAndTall_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = barData,
                modifier = Modifier
                    .width(10.dp)
                    .height(500.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_nonSquare_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = donutData,
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_nonSquare_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = gaugeData,
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Donut chart small size ──

    @Test
    fun donutChart_verySmallSize_donutMode_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = donutData,
                modifier = Modifier.size(20.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_largeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = donutData,
                modifier = Modifier.size(500.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Gauge full circle mode size variations ──

    @Test
    fun gaugeChart_fullCircle_smallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = gaugeData,
                modifier = Modifier.size(20.dp),
                style = GaugeChartStyle(sweepAngle = 360f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChart_fullCircle_largeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            GaugeChart(
                data = gaugeData,
                modifier = Modifier.size(500.dp),
                style = GaugeChartStyle(sweepAngle = 360f),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Many data items + small size ──

    @Test
    fun barChart_manyBars_smallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            BarChart(
                data = BarChartData.simple(
                    values = (1..50).map { it.toFloat() },
                ),
                modifier = Modifier.size(50.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChart_manyPoints_smallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            LineChart(
                data = LineChartData.fromValues(
                    values = (1..100).map { it.toFloat() },
                ),
                modifier = Modifier.size(30.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_manySlices_smallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            DonutChart(
                data = DonutChartData.fromValues(
                    values = (1..20).associate { "Item$it" to it.toFloat() },
                ),
                modifier = Modifier.size(30.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Scatter chart sizes ──

    @Test
    fun scatterChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(data = scatterData, modifier = Modifier.size(10.dp))
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            ScatterChart(
                data = scatterData,
                modifier = Modifier.fillMaxWidth().height(2000.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Bubble chart sizes ──

    @Test
    fun bubbleChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(data = bubbleData, modifier = Modifier.size(10.dp))
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            BubbleChart(
                data = bubbleData,
                modifier = Modifier.fillMaxWidth().height(2000.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Radar chart sizes ──

    @Test
    fun radarChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(data = radarData, modifier = Modifier.size(10.dp))
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(data = radarData, modifier = Modifier.size(1000.dp))
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChart_nonSquare_rendersWithoutCrash() {
        composeTestRule.setContent {
            RadarChart(
                data = radarData,
                modifier = Modifier.width(300.dp).height(50.dp),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── Pie chart sizes ──

    @Test
    fun pieChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(data = pieData, modifier = Modifier.size(10.dp))
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun pieChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(data = pieData, modifier = Modifier.size(1000.dp))
        }
        composeTestRule.waitForIdle()
    }
}
