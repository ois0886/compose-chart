package com.inseong.composechart.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.inseong.composechart.emptyLineData
import com.inseong.composechart.extremeLineData
import com.inseong.composechart.invalidLineData
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.normalLineData
import com.inseong.composechart.style.LineChartStyle

private val showcaseLineStyle = LineChartStyle(
    curved = true,
    gradientFill = true,
)

@Composable
fun LineChartScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChartShowcaseScreen(
        title = "Line Chart",
        description = "Explore smooth trends, gradient fills, scaling behavior, and resilient input handling.",
        onBack = onBack,
        modifier = modifier,
    ) { sample ->
        val data = when (sample) {
            ChartSample.DEFAULT,
            ChartSample.EXPANDED,
            ChartSample.COMPACT -> normalLineData
            ChartSample.EXTREME -> extremeLineData
            ChartSample.INVALID -> invalidLineData
            ChartSample.EMPTY -> emptyLineData
        }
        LineChart(
            data = data,
            modifier = Modifier
                .fillMaxWidth()
                .height(sample.chartDimension),
            style = showcaseLineStyle,
            accessibilityLabel = "Line chart preview",
        )
    }
}
