package com.inseong.composechart.legend

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.inseong.composechart.ChartDefaults
import com.inseong.composechart.style.LegendItem
import com.inseong.composechart.style.LegendOrientation
import com.inseong.composechart.style.LegendStyle

/**
 * Chart legend Composable.
 *
 * Displays a list of color-label pairs to identify chart data series.
 * Uses Foundation only (no Material3 dependency).
 *
 * Place alongside any chart to provide context:
 * ```kotlin
 * Column {
 *     LineChart(data = data, colors = colors)
 *     ChartLegend(
 *         items = data.series.mapIndexed { i, s ->
 *             LegendItem(color = colors[i % colors.size], label = s.label)
 *         },
 *     )
 * }
 * ```
 *
 * @param items List of legend items to display
 * @param modifier Layout modifier
 * @param style Legend style configuration
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChartLegend(
    items: List<LegendItem>,
    modifier: Modifier = Modifier,
    style: LegendStyle = LegendStyle(),
) {
    if (items.isEmpty()) return

    val isDark = isSystemInDarkTheme()
    val resolvedTextColor = if (style.textColor == Color.Unspecified) {
        ChartDefaults.resolveAxisLabelColor(style.textColor, isDark)
    } else {
        style.textColor
    }

    val content: @Composable () -> Unit = {
        items.forEach { item ->
            LegendItemRow(
                item = item,
                style = style,
                textColor = resolvedTextColor,
            )
        }
    }

    when (style.orientation) {
        LegendOrientation.HORIZONTAL -> {
            FlowRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(style.spacing),
                verticalArrangement = Arrangement.spacedBy(style.spacing / 2),
            ) {
                content()
            }
        }
        LegendOrientation.VERTICAL -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(style.spacing / 2),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun LegendItemRow(
    item: LegendItem,
    style: LegendStyle,
    textColor: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(style.iconSize)
                .background(
                    color = item.color,
                    shape = RoundedCornerShape(style.iconCornerRadius),
                ),
        )
        Spacer(modifier = Modifier.width(style.iconTextSpacing))
        BasicText(
            text = item.label,
            style = TextStyle(
                fontSize = style.textSize,
                color = textColor,
            ),
        )
    }
}
