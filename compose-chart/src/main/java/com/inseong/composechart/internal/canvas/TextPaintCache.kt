package com.inseong.composechart.internal.canvas

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.inseong.composechart.style.AxisStyle
import com.inseong.composechart.style.TooltipStyle

@Composable
internal fun rememberAxisLabelPaint(
    style: AxisStyle,
    textAlign: Paint.Align,
): Paint = rememberChartTextPaint(
    color = style.labelColor,
    textSize = style.labelSize,
    fontWeight = style.fontWeight,
    textAlign = textAlign,
)

@Composable
internal fun rememberTooltipTextPaint(style: TooltipStyle): Paint {
    val density = LocalDensity.current
    val textSizePx = with(density) { style.textSize.toPx() }
    val typefaceStyle = style.fontWeight.toTypefaceStyle()

    return remember(textSizePx, typefaceStyle) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = textSizePx
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, typefaceStyle)
        }
    }
}

@Composable
internal fun rememberChartTextPaint(
    color: Color,
    textSize: TextUnit,
    fontWeight: FontWeight,
    textAlign: Paint.Align,
): Paint {
    val density = LocalDensity.current
    val colorArgb = color.toArgb()
    val textSizePx = with(density) { textSize.toPx() }
    val typefaceStyle = fontWeight.toTypefaceStyle()

    return remember(colorArgb, textSizePx, typefaceStyle, textAlign) {
        Paint().apply {
            this.color = colorArgb
            this.textSize = textSizePx
            this.textAlign = textAlign
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, typefaceStyle)
        }
    }
}
