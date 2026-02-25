package com.inseong.composechart

import androidx.compose.ui.graphics.Color

/**
 * Default settings for the chart library.
 *
 * When no colors are specified in chart Composables,
 * this default palette is used.
 */
object ChartDefaults {

    /**
     * Default color palette.
     *
     * Assigned in order for multi-series charts.
     * Cycles from the beginning when the number of series exceeds the palette size.
     */
    val colors = listOf(
        Color(0xFF3182F6), // Blue
        Color(0xFF48BB78), // Green
        Color(0xFFED8936), // Orange
        Color(0xFFE53E3E), // Red
        Color(0xFF9F7AEA), // Purple
        Color(0xFF38B2AC), // Teal
    )

    // ── Light/Dark theme default colors ──

    /** Grid line color (light) */
    internal val gridLineColorLight = Color(0xFFEEEEEE)
    /** Grid line color (dark) */
    internal val gridLineColorDark = Color(0xFF333333)

    /** Axis label color (light) */
    internal val axisLabelColorLight = Color(0xFF9E9E9E)
    /** Axis label color (dark) */
    internal val axisLabelColorDark = Color(0xFFBBBBBB)

    /** Gauge track color (light) */
    internal val gaugeTrackColorLight = Color(0xFFF0F0F0)
    /** Gauge track color (dark) */
    internal val gaugeTrackColorDark = Color(0xFF2A2A2A)

    /** Gauge center text color (light) */
    internal val gaugeCenterTextColorLight = Color(0xFF191F28)
    /** Gauge center text color (dark) */
    internal val gaugeCenterTextColorDark = Color(0xFFE8E8E8)

    /**
     * Returns a theme-appropriate default color when [Color.Unspecified] is provided.
     */
    internal fun resolveGridLineColor(color: Color, isDark: Boolean): Color {
        return if (color == Color.Unspecified) {
            if (isDark) gridLineColorDark else gridLineColorLight
        } else {
            color
        }
    }

    internal fun resolveAxisLabelColor(color: Color, isDark: Boolean): Color {
        return if (color == Color.Unspecified) {
            if (isDark) axisLabelColorDark else axisLabelColorLight
        } else {
            color
        }
    }

    internal fun resolveGaugeTrackColor(color: Color, isDark: Boolean): Color {
        return if (color == Color.Unspecified) {
            if (isDark) gaugeTrackColorDark else gaugeTrackColorLight
        } else {
            color
        }
    }

    internal fun reso커밋veGaugeCenterTextColor(color: Color, isDark: Boolean): Color {
        return if (color == Color.Unspecified) {
            if (isDark) gaugeCenterTextColorDark else gaugeCenterTextColorLight
        } else {
            color
        }
    }
}
