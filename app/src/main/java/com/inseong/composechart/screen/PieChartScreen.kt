package com.inseong.composechart.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inseong.composechart.emptyPieData
import com.inseong.composechart.extremePieData
import com.inseong.composechart.invalidPieData
import com.inseong.composechart.normalPieData
import com.inseong.composechart.pie.PieChart

@Composable
fun PieChartScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChartShowcaseScreen(
        title = "Pie Chart",
        description = "Examine compact proportional layouts, labels, and safe handling of unusable slices.",
        onBack = onBack,
        modifier = modifier,
    ) { sample ->
        val data = when (sample) {
            ChartSample.DEFAULT,
            ChartSample.EXPANDED,
            ChartSample.COMPACT -> normalPieData
            ChartSample.EXTREME -> extremePieData
            ChartSample.INVALID -> invalidPieData
            ChartSample.EMPTY -> emptyPieData
        }
        PieChart(
            data = data,
            modifier = Modifier.size(sample.chartDimension),
            accessibilityLabel = "Pie chart preview",
        )
    }
}
