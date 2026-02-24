package com.inseong.composechart.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 바 차트의 스타일 설정.
 *
 * 기본값: 둥근 상단 모서리, 세로 방향,
 * 터치 시 하이라이트, 부드러운 성장 애니메이션.
 *
 * @param cornerRadius 바 상단 모서리 둥글기 (세로 바 기준. 가로 바에서는 오른쪽 모서리)
 * @param barSpacing 같은 그룹 내 바 간 간격
 * @param groupSpacing 그룹 간 간격
 * @param horizontal 가로 방향 바 차트 여부 (true이면 좌→우 성장)
 * @param animationDurationMs 성장 애니메이션 지속 시간 (밀리초)
 * @param highlightOnTouch 터치 시 선택된 바 하이라이트 여부
 * @param highlightAlpha 비선택 바의 투명도 (하이라이트 시)
 * @param chart 공통 차트 스타일
 * @param axis 축 라벨 스타일
 * @param grid 그리드 라인 스타일
 * @param tooltip 툴팁 스타일
 */
data class BarChartStyle(
    val cornerRadius: Dp = 6.dp,
    val barSpacing: Dp = 4.dp,
    val groupSpacing: Dp = 12.dp,
    val horizontal: Boolean = false,
    val animationDurationMs: Int = 600,
    val highlightOnTouch: Boolean = true,
    val highlightAlpha: Float = 0.3f,
    val chart: ChartStyle = ChartStyle(),
    val axis: AxisStyle = AxisStyle(),
    val grid: GridStyle = GridStyle(),
    val tooltip: TooltipStyle = TooltipStyle(),
)
