package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 파이/도넛 차트의 스타일 설정.
 *
 * [holeRadius]로 파이 차트와 도넛 차트를 전환할 수 있다.
 *
 * @param holeRadius 중앙 빈 영역의 비율 (0.0 = 파이, 0.6 = 도넛). 0.0 ~ 1.0 범위
 * @param sliceSpacing 슬라이스 간 간격
 * @param selectedScale 터치 선택 시 슬라이스 확대 비율 (1.0 = 확대 없음)
 * @param showLabels 슬라이스 라벨 표시 여부
 * @param animationDurationMs sweep 애니메이션 지속 시간 (밀리초)
 * @param startAngle 첫 번째 슬라이스의 시작 각도 (-90 = 12시 방향)
 * @param chart 공통 차트 스타일
 */
data class PieChartStyle(
    val holeRadius: Float = 0f,
    val sliceSpacing: Dp = 2.dp,
    val selectedScale: Float = 1.05f,
    val showLabels: Boolean = true,
    val animationDurationMs: Int = 800,
    val startAngle: Float = -90f,
    val chart: ChartStyle = ChartStyle(),
)
