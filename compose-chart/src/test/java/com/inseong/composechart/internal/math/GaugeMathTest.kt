package com.inseong.composechart.internal.math

import org.junit.Assert.assertEquals
import org.junit.Test

class GaugeMathTest {

    @Test
    fun normalizeValue_normalInput_correctRatio() {
        val result = GaugeMath.normalizeValue(72f, 100f)
        assertEquals(72f, result.safeValue, 0.001f)
        assertEquals(100f, result.safeMax, 0.001f)
        assertEquals(0.72f, result.ratio, 0.001f)
    }

    @Test
    fun normalizeValue_nanValue_returnsZero() {
        val result = GaugeMath.normalizeValue(Float.NaN, 100f)
        assertEquals(0f, result.safeValue, 0.001f)
        assertEquals(0f, result.ratio, 0.001f)
    }

    @Test
    fun normalizeValue_infinityValue_returnsZero() {
        val result = GaugeMath.normalizeValue(Float.POSITIVE_INFINITY, 100f)
        assertEquals(0f, result.safeValue, 0.001f)
    }

    @Test
    fun normalizeValue_negativeValue_clampedToZero() {
        val result = GaugeMath.normalizeValue(-10f, 100f)
        assertEquals(0f, result.safeValue, 0.001f)
        assertEquals(0f, result.ratio, 0.001f)
    }

    @Test
    fun normalizeValue_exceedsMax_clampedToMax() {
        val result = GaugeMath.normalizeValue(150f, 100f)
        assertEquals(100f, result.safeValue, 0.001f)
        assertEquals(1f, result.ratio, 0.001f)
    }

    @Test
    fun normalizeValue_zeroMax_safeMaxIsOne() {
        val result = GaugeMath.normalizeValue(5f, 0f)
        assertEquals(1f, result.safeMax, 0.001f)
    }

    @Test
    fun normalizeValue_nanMax_safeMaxIsOne() {
        val result = GaugeMath.normalizeValue(5f, Float.NaN)
        assertEquals(1f, result.safeMax, 0.001f)
    }

    @Test
    fun calculateStartAngle_240sweep_returns150() {
        val angle = GaugeMath.calculateStartAngle(240f)
        assertEquals(150f, angle, 0.001f)
    }

    @Test
    fun calculateStartAngle_360sweep_returns90() {
        val angle = GaugeMath.calculateStartAngle(360f)
        assertEquals(90f, angle, 0.001f)
    }
}
