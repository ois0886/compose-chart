package com.inseong.composechart

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertSame
import org.junit.Test

class ChartDefaultsTest {

    @Test
    fun resolveColors_emptyPalette_returnsDefaultPalette() {
        val resolved = ChartDefaults.resolveColors(emptyList())

        assertSame(ChartDefaults.colors, resolved)
    }

    @Test
    fun resolveColors_customPalette_returnsSamePalette() {
        val customColors = listOf(Color.Magenta, Color.Cyan)

        val resolved = ChartDefaults.resolveColors(customColors)

        assertSame(customColors, resolved)
    }
}
