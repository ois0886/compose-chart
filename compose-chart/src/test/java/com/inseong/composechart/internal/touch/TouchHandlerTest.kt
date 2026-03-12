package com.inseong.composechart.internal.touch

import org.junit.Assert.assertEquals
import org.junit.Test

class TouchHandlerTest {

    @Test
    fun findNearestPointIndex_emptyList_returnsNegativeOne() {
        val index = findNearestPointIndex(50f, emptyList())
        assertEquals(-1, index)
    }

    @Test
    fun findNearestPointIndex_singlePoint_returnsZero() {
        val index = findNearestPointIndex(50f, listOf(100f))
        assertEquals(0, index)
    }

    @Test
    fun findNearestPointIndex_nearestToFirst_returnsZero() {
        val index = findNearestPointIndex(5f, listOf(0f, 50f, 100f))
        assertEquals(0, index)
    }

    @Test
    fun findNearestPointIndex_nearestToLast_returnsLastIndex() {
        val index = findNearestPointIndex(95f, listOf(0f, 50f, 100f))
        assertEquals(2, index)
    }

    @Test
    fun findNearestPointIndex_equidistant_returnsFirstFound() {
        // Touch at 50, equidistant between 0 and 100
        // Implementation iterates forward, first finds 0 (distance=50),
        // then 100 (distance=50, not less) => returns index 0
        val index = findNearestPointIndex(50f, listOf(0f, 100f))
        assertEquals(0, index)
    }
}
