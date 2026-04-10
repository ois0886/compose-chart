package com.inseong.composechart.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.emptyBarData
import com.inseong.composechart.extremeBarData
import com.inseong.composechart.invalidBarData
import com.inseong.composechart.normalBarData

@Composable
fun BarChartScreen(onBack: () -> Unit) {
    ChartShowcaseScreen(
        title = "Bar Chart",
        onBack = onBack,
    ) {
        ChartShowcaseSection(title = "Normal") {
            BarChart(
                data = normalBarData,
                modifier = Modifier.fillMaxWidth().height(200.dp),
            )
        }
        ChartShowcaseSection(title = "Large Size (400dp)") {
            BarChart(
                data = normalBarData,
                modifier = Modifier.fillMaxWidth().height(400.dp),
            )
        }
        ChartShowcaseSection(title = "Small Size (60dp)") {
            BarChart(
                data = normalBarData,
                modifier = Modifier.fillMaxWidth().height(60.dp),
            )
        }
        ChartShowcaseSection(title = "Extreme Values (1 ~ 10000)") {
            BarChart(
                data = extremeBarData,
                modifier = Modifier.fillMaxWidth().height(200.dp),
            )
        }
        ChartShowcaseSection(title = "Invalid Data (NaN / Infinity)") {
            BarChart(
                data = invalidBarData,
                modifier = Modifier.fillMaxWidth().height(200.dp),
            )
        }
        ChartShowcaseSection(title = "Empty Data") {
            BarChart(
                data = emptyBarData,
                modifier = Modifier.fillMaxWidth().height(200.dp),
            )
        }
    }
}
