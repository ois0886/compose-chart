package com.inseong.composechart.internal.canvas

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.inseong.composechart.style.AxisStyle

/**
 * 차트 하단에 X축 라벨을 그린다.
 *
 * 각 라벨은 해당 데이터 포인트의 X 좌표에 가운데 정렬로 표시된다.
 *
 * @param labels 표시할 라벨 텍스트 목록
 * @param style 축 스타일 설정
 * @param chartArea 차트 데이터 영역 (라벨은 이 영역 아래에 표시)
 */
internal fun DrawScope.drawXAxisLabels(
    labels: List<String>,
    style: AxisStyle,
    chartArea: Rect,
) {
    if (labels.isEmpty()) return

    val paint = Paint().apply {
        color = style.labelColor.hashCode()
        textSize = style.labelSize.toPx()
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }

    val y = chartArea.bottom + style.labelSize.toPx() + 8f // 차트 영역 아래 여백

    if (labels.size == 1) {
        drawContext.canvas.nativeCanvas.drawText(
            labels[0],
            chartArea.center.x,
            y,
            paint,
        )
        return
    }

    val step = chartArea.width / (labels.size - 1)
    labels.forEachIndexed { index, label ->
        val x = chartArea.left + step * index
        drawContext.canvas.nativeCanvas.drawText(label, x, y, paint)
    }
}

/**
 * 차트 좌측에 Y축 참조 라벨을 그린다.
 *
 * 최소값과 최대값 사이를 균등 분할하여 라벨을 표시한다.
 *
 * @param minValue Y축 최소값
 * @param maxValue Y축 최대값
 * @param style 축 스타일 설정
 * @param chartArea 차트 데이터 영역 (라벨은 이 영역 왼쪽에 표시)
 */
internal fun DrawScope.drawYAxisLabels(
    minValue: Float,
    maxValue: Float,
    style: AxisStyle,
    chartArea: Rect,
) {
    if (style.yLabelCount <= 0) return

    val paint = Paint().apply {
        color = style.labelColor.hashCode()
        textSize = style.labelSize.toPx()
        textAlign = Paint.Align.RIGHT
        isAntiAlias = true
        typeface = Typeface.DEFAULT
    }

    val range = maxValue - minValue
    val step = chartArea.height / style.yLabelCount
    val valueStep = range / style.yLabelCount

    for (i in 0..style.yLabelCount) {
        val yPos = chartArea.bottom - step * i
        val value = minValue + valueStep * i
        // 정수로 표시 가능하면 정수, 아니면 소수점 1자리
        val text = if (value == value.toLong().toFloat()) {
            value.toLong().toString()
        } else {
            String.format("%.1f", value)
        }
        drawContext.canvas.nativeCanvas.drawText(
            text,
            chartArea.left - 8f, // 차트 영역 왼쪽 여백
            yPos + style.labelSize.toPx() / 3, // 수직 중앙 정렬 보정
            paint,
        )
    }
}
