package com.inseong.composechart.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inseong.composechart.emptyRadarData
import com.inseong.composechart.extremeRadarData
import com.inseong.composechart.invalidRadarData
import com.inseong.composechart.normalRadarData
import com.inseong.composechart.radar.RadarChart

@Composable
fun RadarChartScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChartShowcaseScreen(
        title = "Radar Chart",
        description = "Compare multi-axis profiles and verify web geometry across difficult datasets.",
        onBack = onBack,
        modifier = modifier,
    ) { sample ->
        val data = when (sample) {
            ChartSample.DEFAULT,
            ChartSample.EXPANDED,
            ChartSample.COMPACT -> normalRadarData
            ChartSample.EXTREME -> extremeRadarData
            ChartSample.INVALID -> invalidRadarData
            ChartSample.EMPTY -> emptyRadarData
        }
        RadarChart(
            data = data,
            modifier = Modifier.size(sample.chartDimension),
            accessibilityLabel = "Radar chart preview",
        )
    }
}
