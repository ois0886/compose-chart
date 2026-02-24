package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * 라인 차트의 개별 시리즈(선) 데이터.
 *
 * 하나의 라인 차트에 여러 시리즈를 추가하면
 * 멀티 라인 차트가 된다.
 *
 * @param points 시리즈를 구성하는 데이터 포인트 목록. 비어있으면 해당 시리즈는 무시된다.
 * @param label 시리즈 이름 (범례 등에 사용)
 * @param color 시리즈 색상. [Color.Unspecified]이면 기본 팔레트에서 자동 할당
 */
data class LineSeries(
    val points: List<ChartPoint>,
    val label: String = "",
    val color: Color = Color.Unspecified,
)

/**
 * 라인 차트에 전달하는 전체 데이터.
 *
 * series가 비어있거나 유효한 포인트가 없으면 빈 화면이 표시된다.
 *
 * @param series [LineSeries] 목록
 * @param xLabels X축 하단에 표시할 라벨 목록 (선택 사항)
 */
data class LineChartData(
    val series: List<LineSeries>,
    val xLabels: List<String> = emptyList(),
) {
    companion object {
        /**
         * 단일 시리즈 라인 차트를 간편하게 생성한다.
         *
         * ```kotlin
         * LineChartData.single(
         *     points = listOf(ChartPoint(0f, 10f), ChartPoint(1f, 25f)),
         *     xLabels = listOf("1월", "2월"),
         * )
         * ```
         */
        fun single(
            points: List<ChartPoint>,
            xLabels: List<String> = emptyList(),
            label: String = "",
            color: Color = Color.Unspecified,
        ): LineChartData = LineChartData(
            series = listOf(LineSeries(points = points, label = label, color = color)),
            xLabels = xLabels,
        )

        /**
         * Y값 리스트만으로 라인 차트를 생성한다.
         * X값은 0부터 인덱스로 자동 할당된다.
         *
         * ```kotlin
         * LineChartData.fromValues(
         *     values = listOf(10f, 25f, 18f, 32f),
         *     xLabels = listOf("1월", "2월", "3월", "4월"),
         * )
         * ```
         */
        fun fromValues(
            values: List<Float>,
            xLabels: List<String> = emptyList(),
            label: String = "",
            color: Color = Color.Unspecified,
        ): LineChartData {
            val points = values.mapIndexed { index, y ->
                ChartPoint(x = index.toFloat(), y = y)
            }
            return single(points = points, xLabels = xLabels, label = label, color = color)
        }
    }
}
