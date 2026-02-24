package com.inseong.composechart.internal.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.inseong.composechart.style.GridStyle

/**
 * 차트 영역에 그리드 라인을 그린다.
 *
 * 기본 설정에서는 수평 그리드만 얇은 회색 라인으로 표시된다.
 *
 * @param style 그리드 스타일 설정
 * @param chartArea 차트 데이터가 그려지는 영역 (패딩 제외)
 * @param horizontalCount 수평 그리드 라인 개수
 */
internal fun DrawScope.drawGrid(
    style: GridStyle,
    chartArea: Rect,
    horizontalCount: Int,
) {
    val strokeWidthPx = style.strokeWidth.toPx()
    val pathEffect = style.dashPattern?.let {
        PathEffect.dashPathEffect(it, 0f)
    }

    // 수평 그리드 라인
    if (style.showHorizontalLines && horizontalCount > 0) {
        val step = chartArea.height / horizontalCount
        for (i in 0..horizontalCount) {
            val y = chartArea.top + step * i
            drawLine(
                color = style.lineColor,
                start = Offset(chartArea.left, y),
                end = Offset(chartArea.right, y),
                strokeWidth = strokeWidthPx,
                pathEffect = pathEffect,
            )
        }
    }

    // 수직 그리드 라인
    if (style.showVerticalLines && horizontalCount > 0) {
        val step = chartArea.width / horizontalCount
        for (i in 0..horizontalCount) {
            val x = chartArea.left + step * i
            drawLine(
                color = style.lineColor,
                start = Offset(x, chartArea.top),
                end = Offset(x, chartArea.bottom),
                strokeWidth = strokeWidthPx,
                pathEffect = pathEffect,
            )
        }
    }
}
