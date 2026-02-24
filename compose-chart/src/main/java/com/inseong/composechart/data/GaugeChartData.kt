package com.inseong.composechart.data

/**
 * 게이지/프로그레스 차트 데이터.
 *
 * 잘못된 값(음수, NaN 등)은 차트 내부에서 안전하게 보정된다.
 *
 * @param value 현재 값
 * @param maxValue 최대 값
 * @param label 중앙에 표시할 라벨 텍스트 (예: "Score", "달성률")
 */
data class GaugeChartData(
    val value: Float,
    val maxValue: Float,
    val label: String = "",
)
