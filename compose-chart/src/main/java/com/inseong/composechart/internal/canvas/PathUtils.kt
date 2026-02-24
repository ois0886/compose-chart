package com.inseong.composechart.internal.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill

/**
 * 데이터 포인트 목록을 부드러운 베지어 곡선 [Path]로 변환한다.
 *
 * Catmull-Rom 스플라인을 큐빅 베지어로 변환하는 방식을 사용하며,
 * tension 값으로 곡선의 부드러움을 조절한다.
 *
 * @param tension 곡선 장력 (0.0 = 직선에 가까움, 1.0 = 매우 부드러움). 기본값 0.3
 * @return 베지어 곡선이 적용된 [Path]
 */
internal fun List<Offset>.toBezierPath(tension: Float = 0.3f): Path {
    val path = Path()
    if (isEmpty()) return path

    path.moveTo(first().x, first().y)

    if (size == 1) return path
    if (size == 2) {
        path.lineTo(last().x, last().y)
        return path
    }

    for (i in 0 until size - 1) {
        val p0 = if (i > 0) this[i - 1] else this[i]
        val p1 = this[i]
        val p2 = this[i + 1]
        val p3 = if (i < size - 2) this[i + 2] else this[i + 1]

        // 제어점 계산 (Catmull-Rom → Cubic Bezier)
        val cp1x = p1.x + (p2.x - p0.x) * tension
        val cp1y = p1.y + (p2.y - p0.y) * tension
        val cp2x = p2.x - (p3.x - p1.x) * tension
        val cp2y = p2.y - (p3.y - p1.y) * tension

        path.cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
    }

    return path
}

/**
 * 직선으로 연결된 데이터 포인트 [Path]를 생성한다.
 *
 * @return 직선 연결 [Path]
 */
internal fun List<Offset>.toLinearPath(): Path {
    val path = Path()
    if (isEmpty()) return path

    path.moveTo(first().x, first().y)
    for (i in 1 until size) {
        path.lineTo(this[i].x, this[i].y)
    }
    return path
}

/**
 * 라인 [Path] 아래 영역을 그라데이션으로 채운다.
 *
 * 라인 경로를 복제한 뒤, 하단 경계까지 닫힌 영역을 만들어
 * 위에서 아래로 투명해지는 그라데이션 브러시로 채운다.
 *
 * @param linePath 라인 경로 (닫히지 않은 상태)
 * @param color 그라데이션의 시작 색상 (상단)
 * @param alpha 그라데이션 시작 투명도
 * @param bottomY 채울 영역의 하단 Y 좌표 (차트 영역 바닥)
 * @param startX 라인의 시작 X 좌표
 * @param endX 라인의 끝 X 좌표
 */
internal fun DrawScope.drawGradientFill(
    linePath: Path,
    color: Color,
    alpha: Float,
    bottomY: Float,
    startX: Float,
    endX: Float,
) {
    val fillPath = Path().apply {
        addPath(linePath)
        // 라인 끝에서 바닥으로 내려간 뒤, 시작점 바닥으로 돌아가서 닫기
        lineTo(endX, bottomY)
        lineTo(startX, bottomY)
        close()
    }

    drawPath(
        path = fillPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = alpha),
                Color.Transparent,
            ),
        ),
        style = Fill,
    )
}
