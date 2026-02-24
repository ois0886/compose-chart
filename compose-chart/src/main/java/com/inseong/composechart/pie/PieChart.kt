package com.inseong.composechart.pie

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.data.PieChartData
import com.inseong.composechart.data.PieSlice
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.internal.touch.chartTouchHandler
import com.inseong.composechart.style.PieChartStyle
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 파이/도넛 차트 Composable.
 *
 * [PieChartStyle.holeRadius]를 조절하여 파이 차트(0)와 도넛 차트(0.6 등)를 전환할 수 있다.
 * 시계 방향 sweep 애니메이션, 터치 시 슬라이스 확대를 지원한다.
 *
 * 파이 차트:
 * ```kotlin
 * PieChart(
 *     data = PieChartData(
 *         slices = listOf(
 *             PieSlice(40f, "식비"),
 *             PieSlice(25f, "교통"),
 *             PieSlice(35f, "기타"),
 *         ),
 *     ),
 *     modifier = Modifier.size(200.dp),
 * )
 * ```
 *
 * 도넛 차트:
 * ```kotlin
 * PieChart(
 *     data = PieChartData.fromValues(mapOf("식비" to 40f, "교통" to 25f)),
 *     style = PieChartStyle(holeRadius = 0.6f),
 * )
 * ```
 *
 * @param data 차트에 표시할 데이터
 * @param modifier 레이아웃 Modifier (크기 지정 필수)
 * @param style 차트 스타일 설정
 * @param colors 슬라이스 색상 팔레트
 * @param onSliceSelected 슬라이스 터치 시 콜백
 */
@Composable
fun PieChart(
    data: PieChartData,
    modifier: Modifier = Modifier,
    style: PieChartStyle = PieChartStyle(),
    colors: List<Color> = ChartDefaults.colors,
    onSliceSelected: ((index: Int, slice: PieSlice) -> Unit)? = null,
) {
    val progress by rememberChartAnimation(style.animationDurationMs)
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var touchOffset by remember { mutableStateOf<Offset?>(null) }

    // 유효한 슬라이스만 필터링 (value > 0)
    val validSlices = remember(data) { data.slices.filter { it.value > 0f } }
    if (validSlices.isEmpty()) return

    val total = validSlices.sumOf { it.value.toDouble() }.toFloat()
    if (total == 0f) return

    // 선택된 슬라이스의 확대 애니메이션
    val selectedScales = validSlices.indices.map { index ->
        animateFloatAsState(
            targetValue = if (index == selectedIndex) style.selectedScale else 1f,
            animationSpec = tween(200),
            label = "sliceScale$index",
        )
    }

    Canvas(
        modifier = modifier.chartTouchHandler { offset ->
            touchOffset = offset
            if (offset == null) selectedIndex = -1
        },
    ) {
        val paddingPx = style.chart.chartPadding.toPx()
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = min(size.width, size.height) / 2 - paddingPx
        if (radius <= 0f) return@Canvas

        val holeRadiusPx = radius * style.holeRadius.coerceIn(0f, 0.95f)

        // 슬라이스 간 간격 (각도) — 단일 슬라이스면 간격 없음
        val spacingAngle = if (validSlices.size > 1) {
            val circumference = 2 * Math.PI.toFloat() * radius
            if (circumference > 0f) {
                (style.sliceSpacing.toPx() / circumference) * 360f
            } else {
                0f
            }
        } else {
            0f
        }

        // 터치 감지: 각도와 거리로 슬라이스 판별
        val currentTouch = touchOffset
        if (currentTouch != null) {
            val dx = currentTouch.x - centerX
            val dy = currentTouch.y - centerY
            val distance = sqrt(dx * dx + dy * dy)

            if (distance <= radius && distance >= holeRadiusPx) {
                var touchAngle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                touchAngle = (touchAngle - style.startAngle + 720f) % 360f

                var accumulatedAngle = 0f
                validSlices.forEachIndexed { index, slice ->
                    val sweepAngle = (slice.value / total) * 360f
                    if (touchAngle >= accumulatedAngle && touchAngle < accumulatedAngle + sweepAngle) {
                        if (selectedIndex != index) {
                            selectedIndex = index
                            onSliceSelected?.invoke(index, slice)
                        }
                        return@forEachIndexed
                    }
                    accumulatedAngle += sweepAngle
                }
            } else {
                selectedIndex = -1
            }
        }

        // 슬라이스 그리기
        var currentAngle = style.startAngle

        validSlices.forEachIndexed { index, slice ->
            val sweepAngle = (slice.value / total) * 360f * progress
            val actualSweep = (sweepAngle - spacingAngle).coerceAtLeast(0.1f)

            val sliceColor = if (slice.color == Color.Unspecified) {
                colors[index % colors.size]
            } else {
                slice.color
            }

            val scale = selectedScales[index].value

            // 선택된 슬라이스는 중심에서 약간 바깥으로 이동
            val midAngle = currentAngle + sweepAngle / 2
            val midAngleRad = Math.toRadians(midAngle.toDouble())
            val offsetDistance = if (scale > 1f) radius * (scale - 1f) * 2 else 0f
            val offsetX = (cos(midAngleRad) * offsetDistance).toFloat()
            val offsetY = (sin(midAngleRad) * offsetDistance).toFloat()

            val arcCenterX = centerX + offsetX
            val arcCenterY = centerY + offsetY

            if (style.holeRadius > 0f) {
                // 도넛 모드
                val strokeWidth = radius - holeRadiusPx
                val arcRadius = holeRadiusPx + strokeWidth / 2

                drawArc(
                    color = sliceColor,
                    startAngle = currentAngle + spacingAngle / 2,
                    sweepAngle = actualSweep,
                    useCenter = false,
                    topLeft = Offset(arcCenterX - arcRadius, arcCenterY - arcRadius),
                    size = Size(arcRadius * 2, arcRadius * 2),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth),
                )
            } else {
                // 파이 모드
                drawArc(
                    color = sliceColor,
                    startAngle = currentAngle + spacingAngle / 2,
                    sweepAngle = actualSweep,
                    useCenter = true,
                    topLeft = Offset(arcCenterX - radius, arcCenterY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Fill,
                )
            }

            // 슬라이스 라벨 그리기
            // 너무 작은 슬라이스(15도 미만)는 라벨 생략
            val rawSweep = (slice.value / total) * 360f
            if (style.showLabels && slice.label.isNotEmpty() && progress > 0.8f && rawSweep >= 15f) {
                // 도넛: hole과 외곽의 중간, 파이: 반지름의 65%
                val labelRadius = if (style.holeRadius > 0f) {
                    (holeRadiusPx + radius) / 2
                } else {
                    radius * 0.65f
                }

                drawSliceLabel(
                    label = slice.label,
                    midAngle = midAngle,
                    centerX = arcCenterX,
                    centerY = arcCenterY,
                    labelRadius = labelRadius,
                    chartRadius = radius,
                    canvasWidth = size.width,
                    canvasHeight = size.height,
                )
            }

            currentAngle += sweepAngle
        }
    }
}

/**
 * 슬라이스 중간 지점에 라벨 텍스트를 그린다.
 *
 * 차트 크기에 비례하여 텍스트 크기를 조정하고,
 * 텍스트가 캔버스 밖으로 벗어나면 표시하지 않는다.
 */
private fun DrawScope.drawSliceLabel(
    label: String,
    midAngle: Float,
    centerX: Float,
    centerY: Float,
    labelRadius: Float,
    chartRadius: Float,
    canvasWidth: Float,
    canvasHeight: Float,
) {
    val midAngleRad = Math.toRadians(midAngle.toDouble())
    val labelX = centerX + (cos(midAngleRad) * labelRadius).toFloat()
    val labelY = centerY + (sin(midAngleRad) * labelRadius).toFloat()

    // 차트 크기에 비례하는 텍스트 크기 (최소 8dp, 최대 14dp 상당)
    val scaledTextSize = (chartRadius * 0.12f).coerceIn(8f * density, 14f * density)

    val paint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = scaledTextSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }

    // 텍스트 크기 측정
    val textWidth = paint.measureText(label)
    val textHeight = scaledTextSize

    // 텍스트가 캔버스 경계를 벗어나면 표시하지 않음
    val margin = 4f
    if (labelX - textWidth / 2 < margin || labelX + textWidth / 2 > canvasWidth - margin) return
    if (labelY - textHeight < margin || labelY + textHeight / 2 > canvasHeight - margin) return

    drawContext.canvas.nativeCanvas.drawText(
        label,
        labelX,
        labelY + textHeight / 3, // 수직 중앙 보정
        paint,
    )
}
