package com.inseong.composechart.legend

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
 * @param onItemClick Callback when a legend item is clicked. Receives the item index.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChartLegend(
    items: List<LegendItem>,
    modifier: Modifier = Modifier,
    style: LegendStyle = LegendStyle(),
    onItemClick: ((index: Int) -> Unit)? = null,
) {
    if (items.isEmpty()) return

    val isDark = isSystemInDarkTheme()
    val resolvedTextColor = if (style.textColor == Color.Unspecified) {
        ChartDefaults.resolveAxisLabelColor(style.textColor, isDark)
    } else {
        style.textColor
    }

    val content: @Composable () -> Unit = {
        items.forEachIndexed { index, item ->
            LegendItemRow(
                item = item,
                index = index,
                style = style,
                textColor = resolvedTextColor,
                onItemClick = onItemClick,
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
    index: Int,
    style: LegendStyle,
    textColor: Color,
    onItemClick: ((index: Int) -> Unit)?,
) {
    val alpha = if (item.enabled) 1f else style.disabledAlpha
    val rowModifier = if (onItemClick != null) {
        Modifier.clickable { onItemClick(index) }
    } else {
        Modifier
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(style.iconSize)
                .background(
                    color = item.color.copy(alpha = alpha),
                    shape = RoundedCornerShape(style.iconCornerRadius),
                ),
        )
        Spacer(modifier = Modifier.width(style.iconTextSpacing))
        BasicText(
            text = item.label,
            style = TextStyle(
                fontSize = style.textSize,
                fontWeight = style.fontWeight,
                color = textColor.copy(alpha = alpha),
            ),
        )
    }
}
