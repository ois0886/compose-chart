package com.inseong.composechart.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.emptyDonutData
import com.inseong.composechart.extremeDonutData
import com.inseong.composechart.invalidDonutData
import com.inseong.composechart.normalDonutData
import com.inseong.composechart.style.DonutChartStyle

@Composable
fun DonutChartScreen(onBack: () -> Unit) {
    ChartShowcaseScreen(
        title = "Donut Chart",
        onBack = onBack,
    ) {
        ChartShowcaseSection(title = "Normal") {
            DonutChart(
                data = normalDonutData,
                modifier = Modifier.size(200.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        ChartShowcaseSection(title = "Large Size (400dp)") {
            DonutChart(
                data = normalDonutData,
                modifier = Modifier.size(350.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        ChartShowcaseSection(title = "Small Size (60dp)") {
            DonutChart(
                data = normalDonutData,
                modifier = Modifier.size(60.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        ChartShowcaseSection(title = "Extreme Values (1 ~ 10000)") {
            DonutChart(
                data = extremeDonutData,
                modifier = Modifier.size(200.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        ChartShowcaseSection(title = "Invalid Data (NaN / Infinity)") {
            DonutChart(
                data = invalidDonutData,
                modifier = Modifier.size(200.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
        ChartShowcaseSection(title = "Empty Data") {
            DonutChart(
                data = emptyDonutData,
                modifier = Modifier.size(200.dp),
                style = DonutChartStyle(holeRadius = 0.6f),
            )
        }
    }
}
