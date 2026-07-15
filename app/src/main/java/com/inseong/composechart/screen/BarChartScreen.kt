package com.inseong.composechart.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.emptyBarData
import com.inseong.composechart.extremeBarData
import com.inseong.composechart.invalidBarData
import com.inseong.composechart.normalBarData

@Composable
fun BarChartScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChartShowcaseScreen(
        title = "Bar Chart",
        description = "Compare categories while testing bar spacing, scale extremes, and constrained layouts.",
        onBack = onBack,
        modifier = modifier,
    ) { sample ->
        val data = when (sample) {
            ChartSample.DEFAULT,
            ChartSample.EXPANDED,
            ChartSample.COMPACT -> normalBarData
            ChartSample.EXTREME -> extremeBarData
            ChartSample.INVALID -> invalidBarData
            ChartSample.EMPTY -> emptyBarData
        }
        BarChart(
            data = data,
            modifier = Modifier
                .fillMaxWidth()
                .height(sample.chartDimension),
            accessibilityLabel = "Bar chart preview",
        )
    }
}
