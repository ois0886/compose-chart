package com.inseong.composechart.internal.canvas

import android.graphics.Typeface
import androidx.compose.ui.text.font.FontWeight

/**
 * Converts a Compose [FontWeight] to an Android [Typeface] style constant.
 */
internal fun FontWeight.toTypefaceStyle(): Int =
    if (weight >= 700) Typeface.BOLD else Typeface.NORMAL
