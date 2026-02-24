package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * 차트 축(Axis) 라벨의 스타일 설정.
 *
 * 토스 스타일 기본값: X축 라벨만 표시, Y축 라인 없음.
 *
 * @param showXAxis X축 라벨 표시 여부
 * @param showYAxis Y축 라벨 표시 여부
 * @param labelColor 라벨 텍스트 색상
 * @param labelSize 라벨 텍스트 크기
 * @param yLabelCount Y축 참조 라벨 개수 (균등 분할)
 */
data class AxisStyle(
    val showXAxis: Boolean = true,
    val showYAxis: Boolean = false,
    val labelColor: Color = Color(0xFF9E9E9E),
    val labelSize: TextUnit = 11.sp,
    val yLabelCount: Int = 5,
)
