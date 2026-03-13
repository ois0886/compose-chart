package com.inseong.composechart

import androidx.compose.runtime.saveable.SaverScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChartZoomStateTest {

    @Test
    fun applyZoom_scalesWithinBounds() {
        val state = ChartZoomState()
        state.applyZoom(2f, 100f, 100f)
        assertEquals(2f, state.scale, 0.001f)

        // Should not exceed maxScale
        state.applyZoom(10f, 100f, 100f)
        assertEquals(5f, state.scale, 0.001f)
    }

    @Test
    fun applyZoom_doesNotGoBelowMinScale() {
        val state = ChartZoomState(initialScale = 2f)
        state.applyZoom(0.1f, 100f, 100f)
        assertEquals(1f, state.scale, 0.001f)
    }

    @Test
    fun applyPan_updatesOffsets() {
        val state = ChartZoomState()
        state.applyPan(10f, 20f)
        assertEquals(10f, state.offsetX, 0.001f)
        assertEquals(20f, state.offsetY, 0.001f)

        state.applyPan(-5f, -10f)
        assertEquals(5f, state.offsetX, 0.001f)
        assertEquals(10f, state.offsetY, 0.001f)
    }

    @Test
    fun clampOffset_limitsWithinBounds() {
        val state = ChartZoomState(initialScale = 2f, initialOffsetX = 1000f, initialOffsetY = 1000f)
        state.clampOffset(200f, 200f)

        // maxOffsetX = (200 * 2 - 200) / 2 = 100
        assertEquals(100f, state.offsetX, 0.001f)
        assertEquals(100f, state.offsetY, 0.001f)
    }

    @Test
    fun reset_restoresDefaults() {
        val state = ChartZoomState()
        state.applyZoom(3f, 50f, 50f)
        state.applyPan(100f, 200f)

        state.reset()
        assertEquals(1f, state.scale, 0.001f)
        assertEquals(0f, state.offsetX, 0.001f)
        assertEquals(0f, state.offsetY, 0.001f)
    }

    @Test
    fun isZoomed_reflectsScaleState() {
        val state = ChartZoomState()
        assertFalse(state.isZoomed)

        state.applyZoom(2f, 0f, 0f)
        assertTrue(state.isZoomed)

        state.reset()
        assertFalse(state.isZoomed)
    }

    @Test
    fun saver_preservesAndRestoresState() {
        val original = ChartZoomState(
            initialScale = 2.5f,
            initialOffsetX = 10f,
            initialOffsetY = 20f,
            minScale = 0.5f,
            maxScale = 8f,
        )
        val scope = SaverScope { true }
        val saved = with(ChartZoomState.Saver) { scope.save(original) }!!
        val restored = ChartZoomState.Saver.restore(saved)!!

        assertEquals(original.scale, restored.scale, 0.001f)
        assertEquals(original.offsetX, restored.offsetX, 0.001f)
        assertEquals(original.offsetY, restored.offsetY, 0.001f)
        assertEquals(original.minScale, restored.minScale, 0.001f)
        assertEquals(original.maxScale, restored.maxScale, 0.001f)
    }
}
