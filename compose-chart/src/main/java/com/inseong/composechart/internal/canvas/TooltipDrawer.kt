package com.inseong.composechart.internal.canvas

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.inseong.composechart.style.TooltipStyle

/**
 * 터치한 데이터 포인트 위에 툴팁 버블을 그린다.
 *
 * 툴팁은 데이터 포인트 위에 둥근 사각형으로 표시되며,
 * 캔버스 경계를 벗어나지 않도록 자동으로 위치가 조정된다.
 *
 * @param position 데이터 포인트의 캔버스 좌표
 * @param text 툴팁에 표시할 텍스트
 * @param style 툴팁 스타일 설정
 * @param lineColor 해당 시리즈의 색상 (인디케이터 테두리에 사용)
 * @param canvasSize 캔버스 전체 크기 (경계 제한용)
 */
internal fun DrawScope.drawTooltip(
    position: Offset,
    text: String,
    style: TooltipStyle,
    lineColor: Color,
    canvasSize: Size,
) {
    val paddingH = style.paddingHorizontal.toPx()
    val paddingV = style.paddingVertical.toPx()
    val cornerRadius = style.cornerRadius.toPx()
    val textSizePx = style.textSize.toPx()

    // 텍스트 크기 측정
    val textPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = textSizePx
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    val textWidth = textPaint.measureText(text)
    val textHeight = textSizePx

    // 툴팁 버블 크기
    val bubbleWidth = textWidth + paddingH * 2
    val bubbleHeight = textHeight + paddingV * 2
    val arrowHeight = 6f

    // 기본 위치: 포인트 위
    var bubbleLeft = position.x - bubbleWidth / 2
    var bubbleTop = position.y - bubbleHeight - arrowHeight - style.indicatorRadius.toPx() - 4f

    // 캔버스 경계 보정
    if (bubbleLeft < 0f) bubbleLeft = 0f
    if (bubbleLeft + bubbleWidth > canvasSize.width) {
        bubbleLeft = canvasSize.width - bubbleWidth
    }
    if (bubbleTop < 0f) {
        // 위에 공간이 없으면 아래에 표시
        bubbleTop = position.y + style.indicatorRadius.toPx() + arrowHeight + 4f
    }

    // 툴팁 배경 그리기
    val bubblePath = Path().apply {
        addRoundRect(
            RoundRect(
                left = bubbleLeft,
                top = bubbleTop,
                right = bubbleLeft + bubbleWidth,
                bottom = bubbleTop + bubbleHeight,
                cornerRadius = CornerRadius(cornerRadius),
            )
        )
    }
    drawPath(path = bubblePath, color = style.backgroundColor, style = Fill)

    // 툴팁 텍스트 그리기
    drawContext.canvas.nativeCanvas.drawText(
        text,
        bubbleLeft + paddingH,
        bubbleTop + paddingV + textHeight * 0.85f, // baseline 보정
        textPaint,
    )

    // 데이터 포인트 인디케이터 (도트) 그리기
    val indicatorBorderColor = if (style.indicatorBorderColor == Color.Unspecified) {
        lineColor
    } else {
        style.indicatorBorderColor
    }

    // 바깥 원 (테두리)
    drawCircle(
        color = indicatorBorderColor,
        radius = style.indicatorRadius.toPx() + style.indicatorBorderWidth.toPx(),
        center = position,
    )
    // 안쪽 원 (채우기)
    drawCircle(
        color = style.indicatorColor,
        radius = style.indicatorRadius.toPx(),
        center = position,
    )
}

/**
 * 터치 지점에서 차트 영역 전체를 관통하는 수직 인디케이터 라인을 그린다.
 *
 * @param x 수직선의 X 좌표
 * @param topY 수직선 상단 Y 좌표
 * @param bottomY 수직선 하단 Y 좌표
 * @param color 수직선 색상
 */
internal fun DrawScope.drawVerticalIndicatorLine(
    x: Float,
    topY: Float,
    bottomY: Float,
    color: Color = Color(0xFFDDDDDD),
) {
    drawLine(
        color = color,
        start = Offset(x, topY),
        end = Offset(x, bottomY),
        strokeWidth = 1f,
    )
}
