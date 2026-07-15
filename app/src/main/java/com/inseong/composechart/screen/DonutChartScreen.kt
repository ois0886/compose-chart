package com.inseong.composechart.screen

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.emptyDonutData
import com.inseong.composechart.extremeDonutData
import com.inseong.composechart.invalidDonutData
import com.inseong.composechart.normalDonutData
import com.inseong.composechart.style.DonutChartStyle

private val showcaseDonutStyle = DonutChartStyle(holeRadius = 0.6f)

@Composable
fun DonutChartScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChartShowcaseScreen(
        title = "Donut Chart",
        description = "Inspect proportional slices, label fit, selection spacing, and malformed values.",
        onBack = onBack,
        modifier = modifier,
    ) { sample ->
        val data = when (sample) {
            ChartSample.DEFAULT,
            ChartSample.EXPANDED,
            ChartSample.COMPACT -> normalDonutData
            ChartSample.EXTREME -> extremeDonutData
            ChartSample.INVALID -> invalidDonutData
            ChartSample.EMPTY -> emptyDonutData
        }
        DonutChart(
            data = data,
            modifier = Modifier.size(sample.chartDimension),
            style = showcaseDonutStyle,
            accessibilityLabel = "Donut chart preview",
        )
    }
}
