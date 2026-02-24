package com.inseong.composechart.data

/**
 * 차트에서 사용하는 개별 데이터 포인트.
 *
 * NaN이나 Infinity 값은 0f로 자동 보정된다.
 *
 * @param x X축 값 (예: 시간, 인덱스)
 * @param y Y축 값 (예: 금액, 수량)
 * @param label 해당 포인트의 라벨 (툴팁 등에 표시, 선택 사항)
 */
data class ChartPoint(
    val x: Float,
    val y: Float,
    val label: String = "",
) {
    /** NaN/Infinity가 보정된 안전한 X값 */
    internal val safeX: Float get() = if (x.isFinite()) x else 0f

    /** NaN/Infinity가 보정된 안전한 Y값 */
    internal val safeY: Float get() = if (y.isFinite()) y else 0f
}
