package com.inseong.composechart.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 차트 그리드 라인의 스타일 설정.
 *
 * 기본값: 수평 그리드만 표시, 얇은 회색 실선.
 *
 * @param showHorizontalLines 수평 그리드 라인 표시 여부
 * @param showVerticalLines 수직 그리드 라인 표시 여부
 * @param lineColor 그리드 라인 색상. [Color.Unspecified]이면 라이트/다크 테마에 맞게 자동 적용
 * @param strokeWidth 그리드 라인 두께
 * @param dashPattern 점선 패턴 (null이면 실선). 예: floatArrayOf(10f, 5f)
 */
data class GridStyle(
    val showHorizontalLines: Boolean = true,
    val showVerticalLines: Boolean = false,
    val lineColor: Color = Color.Unspecified,
    val strokeWidth: Dp = 0.5.dp,
    val dashPattern: FloatArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GridStyle) return false
        return showHorizontalLines == other.showHorizontalLines &&
            showVerticalLines == other.showVerticalLines &&
            lineColor == other.lineColor &&
            strokeWidth == other.strokeWidth &&
            dashPattern.contentEquals(other.dashPattern)
    }

    override fun hashCode(): Int {
        var result = showHorizontalLines.hashCode()
        result = 31 * result + showVerticalLines.hashCode()
        result = 31 * result + lineColor.hashCode()
        result = 31 * result + strokeWidth.hashCode()
        result = 31 * result + (dashPattern?.contentHashCode() ?: 0)
        return result
    }
}
