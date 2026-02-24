package com.inseong.composechart.data

import androidx.compose.ui.graphics.Color

/**
 * 바 차트의 개별 바 데이터.
 *
 * [values]가 하나이면 단일 바, 여러 개이면 스택(누적) 바가 된다.
 * 음수나 NaN 값은 0으로 자동 보정된다.
 *
 * @param values 바의 값 목록. 스택 바의 경우 아래부터 위로 쌓인다.
 * @param label 바의 라벨 (툴팁에 표시)
 * @param colors 각 스택 세그먼트의 색상. 비어있으면 기본 팔레트에서 자동 할당
 */
data class BarEntry(
    val values: List<Float>,
    val label: String = "",
    val colors: List<Color> = emptyList(),
) {
    /** 음수/NaN/Infinity를 0으로 보정한 안전한 값 목록 */
    internal val safeValues: List<Float>
        get() = values.map { if (it.isFinite()) it.coerceAtLeast(0f) else 0f }
}

/**
 * 바 차트의 그룹 데이터.
 *
 * [entries]가 하나이면 단일 바, 여러 개이면 그룹(나란히) 바가 된다.
 *
 * @param entries 그룹 내 바 목록
 * @param label 그룹 라벨 (X축에 표시)
 */
data class BarGroup(
    val entries: List<BarEntry>,
    val label: String = "",
)

/**
 * 바 차트에 전달하는 전체 데이터.
 *
 * groups가 비어있거나 유효한 값이 없으면 빈 화면이 표시된다.
 *
 * @param groups 바 그룹 목록
 */
data class BarChartData(
    val groups: List<BarGroup>,
) {
    companion object {
        /**
         * 값 리스트와 라벨 리스트로 단순 바 차트를 간편하게 생성한다.
         *
         * ```kotlin
         * BarChartData.simple(
         *     values = listOf(30f, 45f, 28f),
         *     labels = listOf("1월", "2월", "3월"),
         * )
         * ```
         */
        fun simple(
            values: List<Float>,
            labels: List<String> = emptyList(),
        ): BarChartData = BarChartData(
            groups = values.mapIndexed { index, value ->
                BarGroup(
                    entries = listOf(BarEntry(values = listOf(value))),
                    label = labels.getOrElse(index) { "" },
                )
            },
        )
    }
}
