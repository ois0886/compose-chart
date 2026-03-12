package com.inseong.composechart.internal.math

/**
 * Normalized gauge value result.
 */
internal data class GaugeNormalized(
    val safeValue: Float,
    val safeMax: Float,
    val ratio: Float,
)

/**
 * Pure math functions for GaugeChart.
 */
internal object GaugeMath {

    /**
     * Normalizes gauge value, handling NaN/Infinity/negative/zero.
     */
    fun normalizeValue(value: Float, maxValue: Float): GaugeNormalized {
        val safeMax = if (maxValue.isFinite() && maxValue > 0f) maxValue else 1f
        val safeValue = if (value.isFinite()) value.coerceIn(0f, safeMax) else 0f
        val ratio = (safeValue / safeMax).coerceIn(0f, 1f)
        return GaugeNormalized(safeValue, safeMax, ratio)
    }

    /**
     * Calculates start angle so that the gap is centered at the bottom.
     *
     * e.g. sweepAngle=240 -> gap=120, start=150 (gap centered at 6 o'clock)
     */
    fun calculateStartAngle(sweepAngle: Float): Float {
        return 90f + (360f - sweepAngle) / 2
    }
}
