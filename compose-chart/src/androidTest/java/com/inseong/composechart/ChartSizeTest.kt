package com.inseong.composechart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.PieChartData
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.pie.PieChart
import com.inseong.composechart.style.GaugeChartStyle
import com.inseong.composechart.style.PieChartStyle
import org.junit.Rule
import org.junit.Test

/**
 * 차트 컴포넌트의 크기 변화에 대한 안정성 테스트.
 *
 * 매우 작은 크기, 매우 큰 크기, 비대칭 크기 등
 * 다양한 크기 조건에서 크래시 없이 렌더링되는지 검증한다.
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
        labels = listOf("1월", "2월", "3월"),
    )
    private val pieData = PieChartData.fromValues(
        values = mapOf("식비" to 40f, "교통" to 30f, "기타" to 30f),
    )
    private val gaugeData = GaugeChartData(value = 72f, maxValue = 100f, label = "점수")

    // ── 매우 작은 크기 ──

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
    fun pieChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = pieData,
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

    // ── 1dp 크기 (극한) ──

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
    fun pieChart_1dp_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = pieData,
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

    // ── 매우 큰 크기 ──

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
    fun pieChart_veryLargeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = pieData,
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

    // ── 비대칭 크기 (가로 극대, 세로 극소 / 그 반대) ──

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
    fun pieChart_nonSquare_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = pieData,
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

    // ── 도넛 차트 작은 크기 ──

    @Test
    fun donutChart_verySmallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = pieData,
                modifier = Modifier.size(20.dp),
                style = PieChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChart_largeSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = pieData,
                modifier = Modifier.size(500.dp),
                style = PieChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }

    // ── 게이지 원형 모드 크기 변화 ──

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

    // ── 많은 데이터 + 작은 크기 ──

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
    fun pieChart_manySlices_smallSize_rendersWithoutCrash() {
        composeTestRule.setContent {
            PieChart(
                data = PieChartData.fromValues(
                    values = (1..20).associate { "항목$it" to it.toFloat() },
                ),
                modifier = Modifier.size(30.dp),
                style = PieChartStyle(holeRadius = 0.6f),
            )
        }
        composeTestRule.waitForIdle()
    }
}
