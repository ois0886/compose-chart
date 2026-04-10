package com.inseong.composechart.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inseong.composechart.emptyGaugeData
import com.inseong.composechart.extremeGaugeData
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.invalidGaugeData
import com.inseong.composechart.normalGaugeData

@Composable
fun GaugeChartScreen(onBack: () -> Unit) {
    ChartShowcaseScreen(
        title = "Gauge Chart",
        onBack = onBack,
    ) {
        ChartShowcaseSection(title = "Normal") {
            GaugeChart(
                data = normalGaugeData,
                modifier = Modifier.size(180.dp),
            )
        }
        ChartShowcaseSection(title = "Large Size (400dp)") {
            GaugeChart(
                data = normalGaugeData,
                modifier = Modifier.size(350.dp),
            )
        }
        ChartShowcaseSection(title = "Small Size (60dp)") {
            GaugeChart(
                data = normalGaugeData,
                modifier = Modifier.size(60.dp),
            )
        }
        ChartShowcaseSection(title = "Extreme Values (1 ~ 10000)") {
            GaugeChart(
                data = extremeGaugeData,
                modifier = Modifier.size(180.dp),
            )
        }
        ChartShowcaseSection(title = "Invalid Data (NaN / Infinity)") {
            GaugeChart(
                data = invalidGaugeData,
                modifier = Modifier.size(180.dp),
            )
        }
        ChartShowcaseSection(title = "Empty Data") {
            GaugeChart(
                data = emptyGaugeData,
                modifier = Modifier.size(180.dp),
            )
        }
    }
}
