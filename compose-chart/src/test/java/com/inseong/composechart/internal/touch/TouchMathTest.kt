package com.inseong.composechart.internal.touch

import org.junit.Assert.assertEquals
import org.junit.Test

class TouchMathTest {

    @Test
    fun findNearest2DPointIndex_emptyList_returnsNegativeOne() {
        val index = TouchMath.findNearest2DPointIndex(
            touchX = 50f, touchY = 50f,
            pointXPositions = emptyList(),
            pointYPositions = emptyList(),
        )
        assertEquals(-1, index)
    }

    @Test
    fun findNearest2DPointIndex_singlePoint_returnsZero() {
        val index = TouchMath.findNearest2DPointIndex(
            touchX = 50f, touchY = 50f,
            pointXPositions = listOf(100f),
            pointYPositions = listOf(100f),
        )
        assertEquals(0, index)
    }

    @Test
    fun findNearest2DPointIndex_nearestByDistance_correctIndex() {
        // Point 0 at (0, 0), Point 1 at (10, 10), touch at (8, 8) => nearest to 1
        val index = TouchMath.findNearest2DPointIndex(
            touchX = 8f, touchY = 8f,
            pointXPositions = listOf(0f, 10f),
            pointYPositions = listOf(0f, 10f),
        )
        assertEquals(1, index)
    }

    @Test
    fun findNearest2DPointIndex_diagonallyCloser_prefersNearest() {
        // Point 0 at (0, 100), Point 1 at (100, 0), touch at (90, 10) => nearest to 1
        val index = TouchMath.findNearest2DPointIndex(
            touchX = 90f, touchY = 10f,
            pointXPositions = listOf(0f, 100f),
            pointYPositions = listOf(100f, 0f),
        )
        assertEquals(1, index)
    }
}
