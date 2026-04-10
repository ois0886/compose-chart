package com.inseong.composechart.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inseong.composechart.emptyLineData
import com.inseong.composechart.extremeLineData
import com.inseong.composechart.invalidLineData
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.normalLineData
import com.inseong.composechart.style.LineChartStyle

@Composable
fun LineChartScreen(onBack: () -> Unit) {
    ChartShowcaseScreen(
        title = "Line Chart",
        onBack = onBack,
    ) {
        ChartShowcaseSection(title = "Normal") {
            LineChart(
                data = normalLineData,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                style = LineChartStyle(curved = true, gradientFill = true),
            )
        }
        ChartShowcaseSection(title = "Large Size (400dp)") {
            LineChart(
                data = normalLineData,
                modifier = Modifier.fillMaxWidth().height(400.dp),
                style = LineChartStyle(curved = true, gradientFill = true),
            )
        }
        ChartShowcaseSection(title = "Small Size (60dp)") {
            LineChart(
                data = normalLineData,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                style = LineChartStyle(curved = true, gradientFill = true),
            )
        }
        ChartShowcaseSection(title = "Extreme Values (1 ~ 10000)") {
            LineChart(
                data = extremeLineData,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                style = LineChartStyle(curved = true, gradientFill = true),
            )
        }
        ChartShowcaseSection(title = "Invalid Data (NaN / Infinity)") {
            LineChart(
                data = invalidLineData,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                style = LineChartStyle(curved = true, gradientFill = true),
            )
        }
        ChartShowcaseSection(title = "Empty Data") {
            LineChart(
                data = emptyLineData,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                style = LineChartStyle(curved = true, gradientFill = true),
            )
        }
    }
}
