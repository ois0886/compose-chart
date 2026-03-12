package com.inseong.composechart.internal.math

/**
 * Pure math functions for BarChart layout and touch detection.
 */
internal object BarMath {

    /**
     * Calculates bar group width.
     *
     * @param chartWidth total chart area width
     * @param groupCount number of bar groups
     * @param groupSpacingPx spacing between groups in pixels
     * @return group width in pixels (minimum 1f)
     */
    fun calculateGroupWidth(
        chartWidth: Float,
        groupCount: Int,
        groupSpacingPx: Float,
    ): Float {
        val totalGroupSpacing = groupSpacingPx * (groupCount - 1).coerceAtLeast(0)
        return ((chartWidth - totalGroupSpacing) / groupCount).coerceAtLeast(1f)
    }

    /**
     * Calculates bar width within a group.
     *
     * @param groupWidth width of one group
     * @param entryCount number of entries in the group
     * @param barSpacingPx spacing between bars in pixels
     * @return bar width in pixels (minimum 1f)
     */
    fun calculateBarWidth(
        groupWidth: Float,
        entryCount: Int,
        barSpacingPx: Float,
    ): Float {
        val totalBarSpacing = barSpacingPx * (entryCount - 1).coerceAtLeast(0)
        return ((groupWidth - totalBarSpacing) / entryCount.coerceAtLeast(1)).coerceAtLeast(1f)
    }

    /**
     * Determines which group index was touched.
     *
     * @param touchX touch X coordinate
     * @param chartLeft left edge of chart area
     * @param groupWidth width of one group
     * @param groupSpacingPx spacing between groups
     * @param groupCount total number of groups
     * @return group index (clamped to valid range)
     */
    fun findTouchedGroupIndex(
        touchX: Float,
        chartLeft: Float,
        groupWidth: Float,
        groupSpacingPx: Float,
        groupCount: Int,
    ): Int {
        val relativeX = touchX - chartLeft
        val groupIndex = ((relativeX + groupSpacingPx / 2) / (groupWidth + groupSpacingPx)).toInt()
        return groupIndex.coerceIn(0, groupCount - 1)
    }

    /**
     * Determines which entry index was touched within a group.
     *
     * @param touchX touch X coordinate
     * @param groupLeft left edge of the group
     * @param barWidth width of one bar
     * @param barSpacingPx spacing between bars
     * @param entryCount number of entries in the group
     * @return entry index (clamped to valid range)
     */
    fun findTouchedEntryIndex(
        touchX: Float,
        groupLeft: Float,
        barWidth: Float,
        barSpacingPx: Float,
        entryCount: Int,
    ): Int {
        val relativeInGroup = touchX - groupLeft
        return (relativeInGroup / (barWidth + barSpacingPx)).toInt().coerceIn(0, entryCount - 1)
    }

    /**
     * Calculates segment height for a stacked bar segment.
     */
    fun calculateSegmentHeight(
        value: Float,
        maxValue: Float,
        chartHeight: Float,
        progress: Float,
    ): Float {
        return (value / maxValue) * chartHeight * progress
    }

    /**
     * Calculates adjusted max value with 10% top margin.
     * Returns 1f if maxValue is 0 or negative.
     */
    fun calculateAdjustedMax(maxValue: Float): Float {
        return if (maxValue <= 0f) 1f else maxValue * 1.1f
    }
}
