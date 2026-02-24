package com.inseong.composechart.gauge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.internal.animation.rememberChartAnimation
import com.inseong.composechart.style.GaugeChartStyle
import kotlin.math.min

/**
 * 게이지/프로그레스 차트 Composable.
 *
 * 원형 프로그레스 또는 반원형 게이지를 표시하며,
 * 중앙에 값과 라벨 텍스트를 오버레이할 수 있다.
 *
 * 원형 프로그레스 (360도):
 * ```kotlin
 * GaugeChart(
 *     data = GaugeChartData(value = 72f, maxValue = 100f, label = "점수"),
 *     modifier = Modifier.size(180.dp),
 *     style = GaugeChartStyle(sweepAngle = 360f),
 * )
 * ```
 *
 * 반원 게이지 (240도, 기본값):
 * ```kotlin
 * GaugeChart(
 *     data = GaugeChartData(value = 72f, maxValue = 100f, label = "달성률"),
 *     modifier = Modifier.size(180.dp),
 * )
 * ```
 *
 * @param data 게이지에 표시할 데이터 (현재값, 최대값, 라벨)
 * @param modifier 레이아웃 Modifier (크기 지정 필수)
 * @param style 게이지 스타일 설정
 * @param centerContent 중앙에 표시할 커스텀 Composable. null이면 기본 텍스트 표시.
 *                      기본 텍스트는 [GaugeChartStyle.showCenterText]가 true일 때만 표시.
 */
@Composable
fun GaugeChart(
    data: GaugeChartData,
    modifier: Modifier = Modifier,
    style: GaugeChartStyle = GaugeChartStyle(),
    centerContent: (@Composable (animatedValue: Float) -> Unit)? = null,
) {
    val progress by rememberChartAnimation(style.animationDurationMs)

    // 음수/NaN/Infinity/0 방어: 안전한 값으로 보정
    val safeMax = if (data.maxValue.isFinite() && data.maxValue > 0f) data.maxValue else 1f
    val safeValue = if (data.value.isFinite()) data.value.coerceIn(0f, safeMax) else 0f
    val ratio = (safeValue / safeMax).coerceIn(0f, 1f)

    // 애니메이션 적용된 현재 값
    val animatedValue = safeValue * progress
    val animatedRatio = ratio * progress

    // 시작 각도: 갭이 하단 중앙에 오도록 계산
    // sweepAngle=240이면 갭=120도, 시작은 150도 (12시 = -90, 6시 = 90)
    val startAngle = 90f + (360f - style.sweepAngle) / 2

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        // 게이지 호(arc) 그리기
        Canvas(modifier = Modifier.matchParentSize()) {
            val paddingPx = style.chart.chartPadding.toPx()
            val strokeWidthPx = style.strokeWidth.toPx()

            val diameter = min(size.width, size.height) - paddingPx * 2 - strokeWidthPx
            if (diameter <= 0f) return@Canvas

            val topLeft = Offset(
                x = (size.width - diameter) / 2,
                y = (size.height - diameter) / 2,
            )
            val arcSize = Size(diameter, diameter)
            val cap = if (style.roundCap) StrokeCap.Round else StrokeCap.Butt

            // 배경 트랙 호
            drawArc(
                color = style.trackColor,
                startAngle = startAngle,
                sweepAngle = style.sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = cap),
            )

            // 프로그레스 호 (애니메이션 적용)
            val progressSweep = style.sweepAngle * animatedRatio
            if (progressSweep > 0f) {
                drawArc(
                    color = style.progressColor,
                    startAngle = startAngle,
                    sweepAngle = progressSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = cap),
                )
            }
        }

        // 중앙 컨텐츠 (Text 오버레이)
        if (centerContent != null) {
            centerContent(animatedValue)
        } else if (style.showCenterText) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 값 텍스트
                val valueText = if (animatedValue == animatedValue.toLong().toFloat()) {
                    animatedValue.toLong().toString()
                } else {
                    String.format("%.1f", animatedValue)
                }
                androidx.compose.foundation.text.BasicText(
                    text = valueText,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = style.centerTextSize,
                        color = style.centerTextColor,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    ),
                )

                // 라벨 텍스트
                if (data.label.isNotEmpty()) {
                    androidx.compose.foundation.text.BasicText(
                        text = data.label,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = style.centerTextSize * 0.5f,
                            color = style.centerTextColor.copy(alpha = 0.6f),
                        ),
                    )
                }
            }
        }
    }
}
