package com.inseong.composechart.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inseong.composechart.emptyRadarData
import com.inseong.composechart.extremeRadarData
import com.inseong.composechart.invalidRadarData
import com.inseong.composechart.normalRadarData
import com.inseong.composechart.radar.RadarChart

@Composable
fun RadarChartScreen(onBack: () -> Unit) {
    ChartShowcaseScreen(
        title = "Radar Chart",
        onBack = onBack,
    ) {
        ChartShowcaseSection(title = "Normal") {
            RadarChart(
                data = normalRadarData,
                modifier = Modifier.size(200.dp),
            )
        }
        ChartShowcaseSection(title = "Large Size (400dp)") {
            RadarChart(
                data = normalRadarData,
                modifier = Modifier.size(350.dp),
            )
        }
        ChartShowcaseSection(title = "Small Size (60dp)") {
            RadarChart(
                data = normalRadarData,
                modifier = Modifier.size(60.dp),
            )
        }
        ChartShowcaseSection(title = "Extreme Values (1 ~ 10000)") {
            RadarChart(
                data = extremeRadarData,
                modifier = Modifier.size(200.dp),
            )
        }
        ChartShowcaseSection(title = "Invalid Data (NaN / Infinity)") {
            RadarChart(
                data = invalidRadarData,
                modifier = Modifier.size(200.dp),
            )
        }
        ChartShowcaseSection(title = "Empty Data") {
            RadarChart(
                data = emptyRadarData,
                modifier = Modifier.size(200.dp),
            )
        }
    }
}
