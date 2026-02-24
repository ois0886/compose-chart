package com.inseong.composechart.internal.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember

/**
 * 차트 진입 애니메이션을 위한 0→1 프로그레스 값을 제공한다.
 *
 * 첫 컴포지션 시 자동으로 애니메이션이 시작되며,
 * 반환된 [State]를 통해 현재 진행률(0.0~1.0)을 참조할 수 있다.
 *
 * 사용 예시:
 * - Line Chart: progress 값으로 라인을 좌→우로 그리는 데 사용
 * - Bar Chart: progress 값으로 바 높이를 0에서 최종 높이로 성장
 * - Pie Chart: progress 값으로 sweep 각도를 0에서 360으로 확장
 *
 * @param durationMs 애니메이션 지속 시간 (밀리초)
 * @param easing 애니메이션 이징 함수
 * @return 0.0에서 1.0으로 변하는 애니메이션 프로그레스 [State]
 */
@Composable
internal fun rememberChartAnimation(
    durationMs: Int,
    easing: Easing = FastOutSlowInEasing,
): State<Float> {
    val animatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMs, easing = easing),
        )
    }
    return animatable.asState()
}
