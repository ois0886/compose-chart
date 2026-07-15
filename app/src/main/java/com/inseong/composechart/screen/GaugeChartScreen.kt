package com.inseong.composechart.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inseong.composechart.emptyGaugeData
import com.inseong.composechart.extremeGaugeData
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.invalidGaugeData
import com.inseong.composechart.normalGaugeData

@Composable
fun GaugeChartScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChartShowcaseScreen(
        title = "Gauge Chart",
        description = "Review progress communication, center-label scaling, and invalid target handling.",
        onBack = onBack,
        modifier = modifier,
    ) { sample ->
        val data = when (sample) {
            ChartSample.DEFAULT,
            ChartSample.EXPANDED,
            ChartSample.COMPACT -> normalGaugeData
            ChartSample.EXTREME -> extremeGaugeData
            ChartSample.INVALID -> invalidGaugeData
            ChartSample.EMPTY -> emptyGaugeData
        }
        GaugeChart(
            data = data,
            modifier = Modifier.size(sample.chartDimension),
            accessibilityLabel = "Gauge chart preview",
        )
    }
}
