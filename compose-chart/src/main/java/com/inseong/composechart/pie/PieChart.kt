package com.inseong.composechart.pie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.ChartSelection
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
 * @param data Data to display (reuses [DonutChartData]). Non-positive slices are filtered out.
 * @param modifier Layout Modifier. A finite size must be provided — the chart will not size itself.
 * @param style Pie style (slice spacing, selected scale, labels, animation duration, start angle).
 * @param colors Slice palette used when slices omit a color. An empty list falls back to
 *   [ChartDefaults.colors].
 * @param accessibilityLabel Prefix used in the chart's semantics `contentDescription`. Default
 *   `"파이 차트"`; override when localizing or using a more specific label.
 * @param onClickLabel Screen reader click action label. When non-null, an onClick semantics node
 *   is added so TalkBack announces the action.
 * @param onSelectionChanged Callback emitting [ChartSelection.Donut] on slice touch. Prefer this
 *   over [onSliceSelected] for a signature shared across all charts.
 * @param onSliceSelected Positional-argument callback kept for source compatibility. Will be
 *   removed in v2.0 — migrate to [onSelectionChanged].
 *
 * @see ChartSelection.Donut
 * @see DonutChart
 */
@Composable
fun PieChart(
    data: DonutChartData,
    modifier: Modifier = Modifier,
    style: PieChartStyle = PieChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    accessibilityLabel: String = "파이 차트",
    onClickLabel: String? = null,
    onSelectionChanged: ((ChartSelection.Donut) -> Unit)? = null,
    onSliceSelected: ((index: Int, slice: DonutSlice) -> Unit)? = null,
) {
    val donutStyle = remember(style) {
        DonutChartStyle(
            holeRadius = 0f,
            sliceSpacing = style.sliceSpacing,
            selectedScale = style.selectedScale,
            showLabels = style.showLabels,
            animationDurationMs = style.animationDurationMs,
            startAngle = style.startAngle,
            chart = style.chart,
        )
    }

    DonutChart(
        data = data,
        modifier = modifier,
        style = donutStyle,
        colors = colors,
        accessibilityLabel = accessibilityLabel,
        onClickLabel = onClickLabel,
        onSelectionChanged = onSelectionChanged,
        onSliceSelected = onSliceSelected,
    )
}
