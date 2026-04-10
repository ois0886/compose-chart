package com.inseong.composechart.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inseong.composechart.emptyPieData
import com.inseong.composechart.extremePieData
import com.inseong.composechart.invalidPieData
import com.inseong.composechart.normalPieData
import com.inseong.composechart.pie.PieChart

@Composable
fun PieChartScreen(onBack: () -> Unit) {
    ChartShowcaseScreen(
        title = "Pie Chart",
        onBack = onBack,
    ) {
        ChartShowcaseSection(title = "Normal") {
            PieChart(
                data = normalPieData,
                modifier = Modifier.size(200.dp),
            )
        }
        ChartShowcaseSection(title = "Large Size (400dp)") {
            PieChart(
                data = normalPieData,
                modifier = Modifier.size(350.dp),
            )
        }
        ChartShowcaseSection(title = "Small Size (60dp)") {
            PieChart(
                data = normalPieData,
                modifier = Modifier.size(60.dp),
            )
        }
        ChartShowcaseSection(title = "Extreme Values (1 ~ 10000)") {
            PieChart(
                data = extremePieData,
                modifier = Modifier.size(200.dp),
            )
        }
        ChartShowcaseSection(title = "Invalid Data (NaN / Infinity)") {
            PieChart(
                data = invalidPieData,
                modifier = Modifier.size(200.dp),
            )
        }
        ChartShowcaseSection(title = "Empty Data") {
            PieChart(
                data = emptyPieData,
                modifier = Modifier.size(200.dp),
            )
        }
    }
}
