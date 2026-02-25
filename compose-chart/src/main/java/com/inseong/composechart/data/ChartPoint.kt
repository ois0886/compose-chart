package com.inseong.composechart.data

/**
 * Individual data point used in charts.
 *
 * NaN and Infinity values are automatically clamped to 0f.
 *
 * @param x X-axis value (e.g., time, index)
 * @param y Y-axis value (e.g., amount, quantity)
 * @param label Label for this point (shown in tooltip, optional)
 */
data class ChartPoint(
    val x: Float,
    val y: Float,
    val label: String = "",
) {
    /** Safe X value with NaN/Infinity clamped to 0 */
    internal val safeX: Float get() = if (x.isFinite()) x else 0f

    /** Safe Y value with NaN/Infinity clamped to 0 */
    internal val safeY: Float get() = if (y.isFinite()) y else 0f
}
