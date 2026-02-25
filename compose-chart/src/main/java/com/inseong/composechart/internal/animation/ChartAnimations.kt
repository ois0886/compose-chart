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
 * Provides a 0-to-1 progress value for chart entry animations.
 *
 * The animation starts automatically on first composition,
 * and the current progress (0.0 to 1.0) can be observed via the returned [State].
 *
 * Usage examples:
 * - Line Chart: draws the line left-to-right using progress
 * - Bar Chart: grows bar height from 0 to final using progress
 * - Donut Chart: expands sweep angle from 0 to 360 using progress
 *
 * @param durationMs Animation duration in milliseconds
 * @param easing Animation easing function
 * @return Animation progress [State] transitioning from 0.0 to 1.0
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
