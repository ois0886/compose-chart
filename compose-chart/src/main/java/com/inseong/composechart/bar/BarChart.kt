package com.inseong.composechart.bar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.canvas.drawGrid
import com.inseong.composechart.internal.canvas.drawTooltip
import com.inseong.composechart.internal.canvas.drawXAxisLabels
import com.inseong.composechart.internal.canvas.drawYAxisLabels
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.style.BarChartStyle

/**
 * 바 차트 Composable.
 *
 * 단일 바, 그룹 바(나란히), 스택 바(누적)를 모두 지원하며,
 * 세로/가로 방향 전환, 둥근 모서리, 성장 애니메이션, 터치 하이라이트를 제공한다.
 *
 * 기본 사용법:
 * ```kotlin
 * BarChart(
 *     data = BarChartData(
 *         groups = listOf(
 *             BarGroup(
 *                 entries = listOf(BarEntry(values = listOf(30f))),
 *                 label = "1월",
 *             ),
 *             BarGroup(
 *                 entries = listOf(BarEntry(values = listOf(45f))),
 *                 label = "2월",
 *             ),
 *         ),
 *     ),
 *     modifier = Modifier.fillMaxWidth().height(200.dp),
 * )
 * ```
 *
 * @param data 차트에 표시할 데이터
 * @param modifier 레이아웃 Modifier (크기 지정 필수)
 * @param style 차트 스타일 설정
 * @param colors 바 색상 팔레트
 * @param onBarSelected 바 터치 시 콜백 (그룹 인덱스, 엔트리 인덱스, 스택 인덱스)
 */
@Composable
fun BarChart(
    data: BarChartData,
    modifier: Modifier = Modifier,
    style: BarChartStyle = BarChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    onBarSelected: ((groupIndex: Int, entryIndex: Int, stackIndex: Int) -> Unit)? = null,
) {
    val progress by rememberChartAnimation(style.animationDurationMs)
    var touchOffset by remember { mutableStateOf<Offset?>(null) }
    var selectedGroupIndex by remember { mutableIntStateOf(-1) }

    // 유효한 그룹 필터링 (엔트리가 있는 그룹만)
    val validGroups = remember(data) {
        data.groups.filter { it.entries.isNotEmpty() }
    }
    if (validGroups.isEmpty()) return

    // 데이터 범위 계산 (safeValues로 NaN/음수 방어)
    val maxValue = validGroups.maxOf { group ->
        group.entries.maxOfOrNull { entry ->
            entry.safeValues.sum()
        } ?: 0f
    }
    val adjustedMax = if (maxValue <= 0f) 1f else maxValue * 1.1f // 상단 10% 여유

    val chartPaddingPx = style.chart.chartPadding

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .chartTouchHandler { offset ->
                touchOffset = offset
                if (offset == null) selectedGroupIndex = -1
            },
    ) {
        val paddingPx = chartPaddingPx.toPx()
        val yAxisWidth = if (style.axis.showYAxis) 40.dp.toPx() else 0f
        val xAxisHeight = if (style.axis.showXAxis) 20.dp.toPx() else 0f

        val chartArea = Rect(
            left = paddingPx + yAxisWidth,
            top = paddingPx,
            right = size.width - paddingPx,
            bottom = size.height - paddingPx - xAxisHeight,
        )

        // 그리드 및 축 그리기
        drawGrid(style.grid, chartArea, style.axis.yLabelCount)

        if (style.axis.showYAxis) {
            drawYAxisLabels(0f, adjustedMax, style.axis, chartArea)
        }

        // X축 라벨 (그룹 라벨 사용)
        if (style.axis.showXAxis) {
            val labels = validGroups.map { it.label }
            if (labels.any { it.isNotEmpty() }) {
                drawXAxisLabels(labels, style.axis, chartArea)
            }
        }

        // 바 레이아웃 계산
        if (chartArea.width <= 0f || chartArea.height <= 0f) return@Canvas

        val groupCount = validGroups.size
        val groupSpacingPx = style.groupSpacing.toPx()
        val barSpacingPx = style.barSpacing.toPx()
        val totalGroupSpacing = groupSpacingPx * (groupCount - 1).coerceAtLeast(0)
        val groupWidth = ((chartArea.width - totalGroupSpacing) / groupCount).coerceAtLeast(1f)

        // 터치된 그룹 인덱스 계산
        val currentTouch = touchOffset
        if (currentTouch != null) {
            val relativeX = currentTouch.x - chartArea.left
            val groupIndex = ((relativeX + groupSpacingPx / 2) / (groupWidth + groupSpacingPx)).toInt()
            selectedGroupIndex = groupIndex.coerceIn(0, groupCount - 1)
        }

        // 바 그리기에 필요한 정보 저장 (툴팁용)
        validGroups.forEachIndexed { groupIndex, group ->
            val groupLeft = chartArea.left + groupIndex * (groupWidth + groupSpacingPx)
            val entryCount = group.entries.size
            val totalBarSpacing = barSpacingPx * (entryCount - 1).coerceAtLeast(0)
            val barWidth = ((groupWidth - totalBarSpacing) / entryCount.coerceAtLeast(1)).coerceAtLeast(1f)

            group.entries.forEachIndexed { entryIndex, entry ->
                val barLeft = groupLeft + entryIndex * (barWidth + barSpacingPx)

                // 하이라이트 처리: 선택되지 않은 바는 투명도 적용
                val isHighlighted = !style.highlightOnTouch ||
                    selectedGroupIndex == -1 ||
                    selectedGroupIndex == groupIndex
                val alpha = if (isHighlighted) 1f else style.highlightAlpha

                if (style.horizontal) {
                    // 가로 바 차트
                    drawHorizontalStackedBar(
                        entry.safeValues,
                        entry.colors.ifEmpty { colors },
                        barLeft, barWidth,
                        chartArea, adjustedMax, progress, alpha,
                        style.cornerRadius.toPx(),
                    )
                } else {
                    // 세로 바 차트
                    drawVerticalStackedBar(
                        entry.safeValues,
                        entry.colors.ifEmpty { colors },
                        barLeft, barWidth,
                        chartArea, adjustedMax, progress, alpha,
                        style.cornerRadius.toPx(),
                    )
                }
            }

            // 선택된 그룹의 툴팁 표시
            if (selectedGroupIndex == groupIndex && group.entries.isNotEmpty()) {
                val firstEntry = group.entries[0]
                val totalValue = firstEntry.safeValues.sum()
                val tooltipText = group.label.ifEmpty {
                    if (totalValue == totalValue.toLong().toFloat()) {
                        totalValue.toLong().toString()
                    } else {
                        String.format("%.1f", totalValue)
                    }
                }

                val barHeight = (totalValue / adjustedMax) * chartArea.height * progress
                val tooltipX = groupLeft + groupWidth / 2
                val tooltipY = chartArea.bottom - barHeight

                drawTooltip(
                    position = Offset(tooltipX, tooltipY),
                    text = tooltipText,
                    style = style.tooltip,
                    lineColor = colors[0],
                    canvasSize = size,
                )

                onBarSelected?.invoke(groupIndex, 0, 0)
            }
        }
    }
}

/**
 * 세로 방향 스택 바를 그린다.
 * 최상단 세그먼트에만 둥근 모서리를 적용한다.
 */
private fun DrawScope.drawVerticalStackedBar(
    values: List<Float>,
    colors: List<Color>,
    barLeft: Float,
    barWidth: Float,
    chartArea: Rect,
    maxValue: Float,
    progress: Float,
    alpha: Float,
    cornerRadius: Float,
) {
    var currentBottom = chartArea.bottom
    val totalSegments = values.size

    values.forEachIndexed { index, value ->
        val segmentHeight = (value / maxValue) * chartArea.height * progress
        val segmentTop = currentBottom - segmentHeight
        val color = colors[index % colors.size].copy(alpha = alpha)

        val isTopSegment = index == totalSegments - 1

        if (isTopSegment && cornerRadius > 0f) {
            // 최상단 세그먼트: 상단만 둥근 모서리
            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = barLeft,
                        top = segmentTop,
                        right = barLeft + barWidth,
                        bottom = currentBottom,
                        topLeftCornerRadius = CornerRadius(cornerRadius),
                        topRightCornerRadius = CornerRadius(cornerRadius),
                        bottomLeftCornerRadius = CornerRadius.Zero,
                        bottomRightCornerRadius = CornerRadius.Zero,
                    )
                )
            }
            drawPath(path = path, color = color, style = Fill)
        } else {
            // 하단 세그먼트: 직각 모서리
            drawRect(
                color = color,
                topLeft = Offset(barLeft, segmentTop),
                size = Size(barWidth, segmentHeight),
            )
        }

        currentBottom = segmentTop
    }
}

/**
 * 가로 방향 스택 바를 그린다.
 * 최우측 세그먼트에만 둥근 모서리를 적용한다.
 */
private fun DrawScope.drawHorizontalStackedBar(
    values: List<Float>,
    colors: List<Color>,
    barTop: Float,
    barHeight: Float,
    chartArea: Rect,
    maxValue: Float,
    progress: Float,
    alpha: Float,
    cornerRadius: Float,
) {
    var currentLeft = chartArea.left
    val totalSegments = values.size

    values.forEachIndexed { index, value ->
        val segmentWidth = (value / maxValue) * chartArea.width * progress
        val color = colors[index % colors.size].copy(alpha = alpha)

        val isRightSegment = index == totalSegments - 1

        if (isRightSegment && cornerRadius > 0f) {
            // 최우측 세그먼트: 오른쪽만 둥근 모서리
            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = currentLeft,
                        top = barTop,
                        right = currentLeft + segmentWidth,
                        bottom = barTop + barHeight,
                        topLeftCornerRadius = CornerRadius.Zero,
                        topRightCornerRadius = CornerRadius(cornerRadius),
                        bottomLeftCornerRadius = CornerRadius.Zero,
                        bottomRightCornerRadius = CornerRadius(cornerRadius),
                    )
                )
            }
            drawPath(path = path, color = color, style = Fill)
        } else {
            drawRect(
                color = color,
                topLeft = Offset(currentLeft, barTop),
                size = Size(segmentWidth, barHeight),
            )
        }

        currentLeft += segmentWidth
    }
}
