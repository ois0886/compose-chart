package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 라인 차트의 스타일 설정.
 *
 * 기본값: 베지어 곡선, 그라데이션 영역 채우기,
 * 터치 시 툴팁 표시, 부드러운 draw-in 애니메이션.
 *
 * @param lineWidth 라인 두께
 * @param curved 베지어 곡선 스무딩 적용 여부 (false이면 직선 연결)
 * @param showDots 데이터 포인트에 도트 표시 여부
 * @param dotRadius 도트 반지름
 * @param gradientFill 라인 아래 영역에 그라데이션 채우기 적용 여부
 * @param gradientAlpha 그라데이션 채우기의 최대 투명도 (0.0 ~ 1.0)
 * @param showTooltipOnTouch 터치 시 툴팁 표시 여부
 * @param animationDurationMs draw-in 애니메이션 지속 시간 (밀리초)
 * @param chart 공통 차트 스타일
 * @param axis 축 라벨 스타일
 * @param grid 그리드 라인 스타일
 * @param tooltip 툴팁 스타일
 */
data class LineChartStyle(
    val lineWidth: Dp = 2.dp,
    val curved: Boolean = true,
    val showDots: Boolean = false,
    val dotRadius: Dp = 4.dp,
    val gradientFill: Boolean = true,
    val gradientAlpha: Float = 0.15f,
    val showTooltipOnTouch: Boolean = true,
    val animationDurationMs: Int = 800,
    val chart: ChartStyle = ChartStyle(),
    val axis: AxisStyle = AxisStyle(),
    val grid: GridStyle = GridStyle(),
    val tooltip: TooltipStyle = TooltipStyle(),
)
