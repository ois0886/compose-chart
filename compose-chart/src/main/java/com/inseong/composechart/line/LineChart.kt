package com.inseong.composechart.line

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.ChartPoint
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.canvas.drawGradientFill
import com.inseong.composechart.internal.canvas.drawGrid
import com.inseong.composechart.internal.canvas.drawTooltip
import com.inseong.composechart.internal.canvas.drawVerticalIndicatorLine
import com.inseong.composechart.internal.canvas.drawXAxisLabels
import com.inseong.composechart.internal.canvas.drawYAxisLabels
import com.inseong.composechart.internal.canvas.toBezierPath
import com.inseong.composechart.internal.canvas.toLinearPath
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.internal.touch.findNearestPointIndex
import com.inseong.composechart.style.LineChartStyle

/**
 * 토스 스타일의 라인 차트 Composable.
 *
 * 하나 이상의 데이터 시리즈를 부드러운 곡선(또는 직선)으로 표시하며,
 * 그라데이션 영역 채우기, 진입 애니메이션, 터치 인터랙션을 지원한다.
 *
 * 기본 사용법:
 * ```kotlin
 * LineChart(
 *     data = LineChartData(
 *         series = listOf(
 *             LineSeries(
 *                 points = listOf(
 *                     ChartPoint(0f, 10f, "1월"),
 *                     ChartPoint(1f, 25f, "2월"),
 *                     ChartPoint(2f, 18f, "3월"),
 *                 )
 *             )
 *         ),
 *         xLabels = listOf("1월", "2월", "3월"),
 *     ),
 *     modifier = Modifier.fillMaxWidth().height(200.dp),
 * )
 * ```
 *
 * @param data 차트에 표시할 데이터
 * @param modifier 레이아웃 Modifier (크기 지정 필수)
 * @param style 차트 스타일 설정
 * @param colors 시리즈 색상 팔레트. 시리즈에 색상이 지정되지 않은 경우 순서대로 사용
 * @param onPointSelected 데이터 포인트 터치 시 콜백. null이면 콜백 없음
 */
@Composable
fun LineChart(
    data: LineChartData,
    modifier: Modifier = Modifier,
    style: LineChartStyle = LineChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    onPointSelected: ((seriesIndex: Int, pointIndex: Int, point: ChartPoint) -> Unit)? = null,
) {
    // 애니메이션 프로그레스 (0→1)
    val progress by rememberChartAnimation(style.animationDurationMs)

    // 터치 상태
    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    // 유효한 시리즈만 필터링 (포인트가 있는 시리즈)
    val validSeries = remember(data) { data.series.filter { it.points.isNotEmpty() } }
    if (validSeries.isEmpty()) return

    // 데이터 범위 계산 (safeX/safeY로 NaN/Infinity 방어)
    val allPoints = validSeries.flatMap { it.points }
    val minX = allPoints.minOf { it.safeX }
    val maxX = allPoints.maxOf { it.safeX }
    val minY = allPoints.minOf { it.safeY }
    val maxY = allPoints.maxOf { it.safeY }
    // Y축 여유 공간 (상하 10%)
    val yRange = (maxY - minY).let { if (it == 0f) 1f else it }
    val yPadding = yRange * 0.1f
    val adjustedMinY = minY - yPadding
    val adjustedMaxY = maxY + yPadding
    val xRange = (maxX - minX).let { if (it == 0f) 1f else it }

    val chartPaddingPx = style.chart.chartPadding

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .chartTouchHandler { offset -> touchOffset = offset },
    ) {
        val paddingPx = chartPaddingPx.toPx()

        // Y축 라벨 영역 폭 계산
        val yAxisWidth = if (style.axis.showYAxis) 40.dp.toPx() else 0f
        // X축 라벨 영역 높이 계산
        val xAxisHeight = if (style.axis.showXAxis) 20.dp.toPx() else 0f

        // 차트 데이터 영역 (축 라벨 및 패딩 제외)
        val chartArea = Rect(
            left = paddingPx + yAxisWidth,
            top = paddingPx,
            right = size.width - paddingPx,
            bottom = size.height - paddingPx - xAxisHeight,
        )

        // 그리드 그리기
        drawGrid(style.grid, chartArea, style.axis.yLabelCount)

        // Y축 라벨 그리기
        if (style.axis.showYAxis) {
            drawYAxisLabels(adjustedMinY, adjustedMaxY, style.axis, chartArea)
        }

        // X축 라벨 그리기
        if (style.axis.showXAxis && data.xLabels.isNotEmpty()) {
            drawXAxisLabels(data.xLabels, style.axis, chartArea)
        }

        if (chartArea.width <= 0f || chartArea.height <= 0f) return@Canvas

        // 각 시리즈 그리기
        validSeries.forEachIndexed { seriesIndex, series ->

            // 시리즈 색상 결정
            val seriesColor = if (series.color == Color.Unspecified) {
                colors[seriesIndex % colors.size]
            } else {
                series.color
            }

            // 데이터 포인트를 캔버스 좌표로 변환 (safeX/safeY 사용)
            val mappedPoints = series.points.map { point ->
                Offset(
                    x = chartArea.left + ((point.safeX - minX) / xRange) * chartArea.width,
                    y = chartArea.bottom - ((point.safeY - adjustedMinY) / (adjustedMaxY - adjustedMinY)) * chartArea.height,
                )
            }

            // 라인 경로 생성 (곡선 또는 직선)
            val linePath = if (style.curved) {
                mappedPoints.toBezierPath()
            } else {
                mappedPoints.toLinearPath()
            }

            // 애니메이션: progress에 따라 좌→우로 클리핑
            val clipRight = chartArea.left + chartArea.width * progress

            clipRect(
                left = chartArea.left,
                top = chartArea.top,
                right = clipRight,
                bottom = chartArea.bottom,
            ) {
                // 그라데이션 영역 채우기
                if (style.gradientFill && mappedPoints.size >= 2) {
                    drawGradientFill(
                        linePath = linePath,
                        color = seriesColor,
                        alpha = style.gradientAlpha,
                        bottomY = chartArea.bottom,
                        startX = mappedPoints.first().x,
                        endX = mappedPoints.last().x,
                    )
                }

                // 라인 그리기
                drawPath(
                    path = linePath,
                    color = seriesColor,
                    style = Stroke(width = style.lineWidth.toPx()),
                )

                // 데이터 포인트 도트 그리기
                if (style.showDots) {
                    mappedPoints.forEach { point ->
                        drawCircle(
                            color = seriesColor,
                            radius = style.dotRadius.toPx(),
                            center = point,
                        )
                    }
                }
            }

            // 터치 인터랙션 처리
            val currentTouch = touchOffset
            if (currentTouch != null && style.showTooltipOnTouch) {
                val pointXPositions = mappedPoints.map { it.x }
                val nearestIndex = findNearestPointIndex(currentTouch.x, pointXPositions)

                if (nearestIndex >= 0 && nearestIndex < mappedPoints.size) {
                    val nearestPoint = mappedPoints[nearestIndex]
                    val dataPoint = series.points[nearestIndex]

                    // 첫 번째 시리즈일 때만 수직 인디케이터 라인 그리기 (중복 방지)
                    if (seriesIndex == 0) {
                        drawVerticalIndicatorLine(
                            x = nearestPoint.x,
                            topY = chartArea.top,
                            bottomY = chartArea.bottom,
                        )
                    }

                    // 툴팁 텍스트 결정
                    val tooltipText = dataPoint.label.ifEmpty {
                        if (dataPoint.y == dataPoint.y.toLong().toFloat()) {
                            dataPoint.y.toLong().toString()
                        } else {
                            String.format("%.1f", dataPoint.y)
                        }
                    }

                    // 툴팁 그리기
                    drawTooltip(
                        position = nearestPoint,
                        text = tooltipText,
                        style = style.tooltip,
                        lineColor = seriesColor,
                        canvasSize = size,
                    )

                    // 콜백 호출
                    onPointSelected?.invoke(seriesIndex, nearestIndex, dataPoint)
                }
            }
        }
    }
}
