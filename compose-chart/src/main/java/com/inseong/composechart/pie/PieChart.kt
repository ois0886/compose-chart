package com.inseong.composechart.pie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.style.DonutChartStyle
import com.inseong.composechart.style.PieChartStyle

/**
 * Pie chart Composable.
 *
 * A filled circle chart (no center hole). Internally delegates to [DonutChart]
 * with [DonutChartStyle.holeRadius] fixed at 0.
 *
 * For a donut chart with a center hole, use [DonutChart] directly.
 *
 * Basic usage:
 * ```kotlin
 * PieChart(
 *     data = DonutChartData.fromValues(
 *         values = mapOf("Food" to 40f, "Transport" to 25f, "Other" to 35f),
 *     ),
 *     modifier = Modifier.size(200.dp),
 * )
 * ```
 *
 * @param data Data to display in the chart (reuses [DonutChartData])
 * @param modifier Layout Modifier (size must be specified)
 * @param style Chart style configuration
 * @param colors Slice color palette
 * @param onSliceSelected Callback on slice touch
 */
@Composable
fun PieChart(
    data: DonutChartData,
    modifier: Modifier = Modifier,
    style: PieChartStyle = PieChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    onSliceSelected: ((index: Int, slice: DonutSlice) -> Unit)? = null,
) {
    DonutChart(
        data = data,
        modifier = modifier,
        style = DonutChartStyle(
            holeRadius = 0f,
            sliceSpacing = style.sliceSpacing,
            selectedScale = style.selectedScale,
            showLabels = style.showLabels,
            animationDurationMs = style.animationDurationMs,
            startAngle = style.startAngle,
            chart = style.chart,
        ),
        colors = colors,
        onSliceSelected = onSliceSelected,
    )
}
