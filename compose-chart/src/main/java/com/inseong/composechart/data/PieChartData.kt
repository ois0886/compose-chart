package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * 파이/도넛 차트의 개별 슬라이스 데이터.
 *
 * 음수나 NaN 값은 자동으로 필터링되어 화면에 표시되지 않는다.
 *
 * @param value 슬라이스의 값 (비율은 전체 합계 대비 자동 계산)
 * @param label 슬라이스 라벨 (범례, 툴팁에 표시)
 * @param color 슬라이스 색상. [Color.Unspecified]이면 기본 팔레트에서 자동 할당
 */
data class PieSlice(
    val value: Float,
    val label: String = "",
    val color: Color = Color.Unspecified,
)

/**
 * 파이/도넛 차트에 전달하는 전체 데이터.
 *
 * slices가 비어있거나 유효한 값이 없으면 빈 화면이 표시된다.
 *
 * @param slices 슬라이스 목록
 */
data class PieChartData(
    val slices: List<PieSlice>,
) {
    companion object {
        /**
         * 라벨-값 맵으로 파이 차트 데이터를 간편하게 생성한다.
         *
         * ```kotlin
         * PieChartData.fromValues(
         *     values = mapOf("식비" to 40f, "교통" to 25f, "쇼핑" to 20f),
         * )
         * ```
         */
        fun fromValues(values: Map<String, Float>): PieChartData = PieChartData(
            slices = values.map { (label, value) -> PieSlice(value = value, label = label) },
        )
    }
}
