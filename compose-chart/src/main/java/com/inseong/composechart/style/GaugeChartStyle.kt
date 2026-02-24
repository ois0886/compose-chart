package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 게이지/프로그레스 차트의 스타일 설정.
 *
 * [sweepAngle]로 원형 프로그레스(360)와 반원 게이지(240)를 전환할 수 있다.
 *
 * @param trackColor 배경 트랙(미채워진 영역)의 색상
 * @param progressColor 채워진 영역의 색상 (토스 블루 기본)
 * @param strokeWidth 게이지 호(arc)의 두께
 * @param roundCap 호 끝을 둥글게 처리할지 여부
 * @param sweepAngle 전체 호의 각도. 360이면 원형, 240이면 하단이 열린 게이지
 * @param showCenterText 중앙에 값/라벨 텍스트 표시 여부
 * @param centerTextSize 중앙 값 텍스트 크기
 * @param centerTextColor 중앙 값 텍스트 색상
 * @param animationDurationMs 채우기 애니메이션 지속 시간 (밀리초)
 * @param chart 공통 차트 스타일
 */
data class GaugeChartStyle(
    val trackColor: Color = Color(0xFFF0F0F0),
    val progressColor: Color = Color(0xFF3182F6),
    val strokeWidth: Dp = 12.dp,
    val roundCap: Boolean = true,
    val sweepAngle: Float = 240f,
    val showCenterText: Boolean = true,
    val centerTextSize: TextUnit = 24.sp,
    val centerTextColor: Color = Color(0xFF191F28),
    val animationDurationMs: Int = 1000,
    val chart: ChartStyle = ChartStyle(),
)
