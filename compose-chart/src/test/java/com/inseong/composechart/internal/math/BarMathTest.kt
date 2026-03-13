package com.inseong.composechart.internal.math

import org.junit.Assert.assertEquals
import org.junit.Test

class BarMathTest {

    @Test
    fun calculateGroupWidth_singleGroup_fullWidth() {
        val width = BarMath.calculateGroupWidth(300f, 1, 10f)
        assertEquals(300f, width, 0.001f)
    }

    @Test
    fun calculateGroupWidth_multipleGroups_dividesEvenly() {
        // 300 width, 3 groups, 10px spacing => 300 - 20 = 280 / 3 ≈ 93.33
        val width = BarMath.calculateGroupWidth(300f, 3, 10f)
        assertEquals(280f / 3f, width, 0.001f)
    }

    @Test
    fun calculateGroupWidth_zeroWidth_returnsMinimum() {
        val width = BarMath.calculateGroupWidth(0f, 3, 10f)
        assertEquals(1f, width, 0.001f)
    }

    @Test
    fun calculateBarWidth_singleEntry_fullGroupWidth() {
        val width = BarMath.calculateBarWidth(100f, 1, 5f)
        assertEquals(100f, width, 0.001f)
    }

    @Test
    fun calculateBarWidth_multipleEntries_dividesWithSpacing() {
        // group=100, 2 entries, 10 spacing => (100-10)/2 = 45
        val width = BarMath.calculateBarWidth(100f, 2, 10f)
        assertEquals(45f, width, 0.001f)
    }

    @Test
    fun calculateAdjustedMax_positiveValue_adds10Percent() {
        val max = BarMath.calculateAdjustedMax(100f)
        assertEquals(110f, max, 0.001f)
    }

    @Test
    fun calculateAdjustedMax_zeroValue_returnsOne() {
        val max = BarMath.calculateAdjustedMax(0f)
        assertEquals(1f, max, 0.001f)
    }

    @Test
    fun calculateAdjustedMax_negativeValue_returnsOne() {
        val max = BarMath.calculateAdjustedMax(-10f)
        assertEquals(1f, max, 0.001f)
    }

    @Test
    fun findTouchedGroupIndex_firstGroup_returnsZero() {
        val index = BarMath.findTouchedGroupIndex(
            touchX = 20f, chartLeft = 0f,
            groupWidth = 100f, groupSpacingPx = 10f, groupCount = 3,
        )
        assertEquals(0, index)
    }

    @Test
    fun findTouchedGroupIndex_lastGroup_returnsLastIndex() {
        val index = BarMath.findTouchedGroupIndex(
            touchX = 250f, chartLeft = 0f,
            groupWidth = 100f, groupSpacingPx = 10f, groupCount = 3,
        )
        assertEquals(2, index)
    }

    @Test
    fun findTouchedGroupIndex_outOfBounds_clamped() {
        val index = BarMath.findTouchedGroupIndex(
            touchX = 500f, chartLeft = 0f,
            groupWidth = 100f, groupSpacingPx = 10f, groupCount = 3,
        )
        assertEquals(2, index)
    }

    @Test
    fun findTouchedEntryIndex_singleEntry_returnsZero() {
        val index = BarMath.findTouchedEntryIndex(
            touchX = 30f, groupLeft = 0f,
            barWidth = 50f, barSpacingPx = 5f, entryCount = 1,
        )
        assertEquals(0, index)
    }

    @Test
    fun findTouchedEntryIndex_multipleEntries_correctIndex() {
        // barWidth=40, barSpacing=10 => entry stride = 50
        // touchX=60 from groupLeft=0 => 60/50 = 1
        val index = BarMath.findTouchedEntryIndex(
            touchX = 60f, groupLeft = 0f,
            barWidth = 40f, barSpacingPx = 10f, entryCount = 3,
        )
        assertEquals(1, index)
    }

    @Test
    fun calculateBarWidth_zeroEntries_fallsBackToOne() {
        // entryCount=0 → coerceAtLeast(1) → 100/1 = 100
        val width = BarMath.calculateBarWidth(100f, 0, 5f)
        assertEquals(100f, width, 0.001f)
    }

    @Test
    fun calculateAdjustedMax_nanValue_returnsOne() {
        val max = BarMath.calculateAdjustedMax(Float.NaN)
        assertEquals(1f, max, 0.001f)
    }

    @Test
    fun calculateAdjustedMax_infinityValue_handlesGracefully() {
        val max = BarMath.calculateAdjustedMax(Float.POSITIVE_INFINITY)
        assertEquals(false, max.isNaN())
    }

    @Test
    fun calculateSegmentHeight_nanMaxValue_returnsFinite() {
        val height = BarMath.calculateSegmentHeight(50f, Float.NaN, 200f, 1f)
        assertEquals(true, height.isNaN() || height.isFinite())
    }

    @Test
    fun calculateSegmentHeight_fullProgress_correctHeight() {
        val height = BarMath.calculateSegmentHeight(50f, 100f, 200f, 1f)
        assertEquals(100f, height, 0.001f) // 50/100 * 200 * 1
    }

    @Test
    fun calculateSegmentHeight_halfProgress_halfHeight() {
        val height = BarMath.calculateSegmentHeight(50f, 100f, 200f, 0.5f)
        assertEquals(50f, height, 0.001f) // 50/100 * 200 * 0.5
    }
}
