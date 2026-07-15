package com.inseong.composechart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ChartGalleryScreen(
    onChartSelected: (ChartType) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("chart-gallery-grid"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            GalleryHero()
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            GallerySectionHeader()
        }
        items(
            items = ChartType.entries.toList(),
            key = ChartType::name,
        ) { chartType ->
            ChartTypeCard(
                chartType = chartType,
                onClick = { onChartSelected(chartType) },
            )
        }
    }
}

@Composable
private fun GalleryHero() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 26.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "COMPOSE CHART",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Build clearer stories\nwith data.",
                style = MaterialTheme.typography.displaySmall,
            )
            Text(
                text = "Explore lightweight, theme-aware charts and test how they behave with real-world edge cases.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GalleryBadge(text = "6 chart types")
                GalleryBadge(text = "Compose-first")
            }
        }
    }
}

@Composable
private fun GalleryBadge(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(999.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
        )
    }
}

@Composable
private fun GallerySectionHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "Explore charts",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Choose a chart to open its interactive test bench.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(999.dp),
        ) {
            Text(
                text = ChartType.entries.size.toString(),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun ChartTypeCard(
    chartType: ChartType,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(258.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    color = chartType.accentColor.copy(alpha = 0.12f),
                    contentColor = chartType.accentColor,
                    shape = RoundedCornerShape(999.dp),
                ) {
                    Text(
                        text = chartType.category,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
                Text(
                    text = "↗",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clearAndSetSemantics { },
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                contentAlignment = Alignment.Center,
            ) {
                ChartArtwork(
                    chartType = chartType,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Text(
                text = chartType.displayName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = chartType.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ChartArtwork(
    chartType: ChartType,
    modifier: Modifier = Modifier,
) {
    val trackColor = MaterialTheme.colorScheme.outlineVariant
    val accentColor = chartType.accentColor

    Canvas(
        modifier = modifier
            .clearAndSetSemantics { }
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        val thinStroke = 1.5.dp.toPx()
        val strongStroke = 4.dp.toPx()

        when (chartType) {
            ChartType.LINE -> {
                drawLine(
                    color = trackColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = thinStroke,
                )
                val path = Path().apply {
                    moveTo(0f, size.height * 0.72f)
                    cubicTo(
                        size.width * 0.2f,
                        size.height * 0.86f,
                        size.width * 0.28f,
                        size.height * 0.32f,
                        size.width * 0.44f,
                        size.height * 0.46f,
                    )
                    cubicTo(
                        size.width * 0.62f,
                        size.height * 0.62f,
                        size.width * 0.72f,
                        size.height * 0.14f,
                        size.width,
                        size.height * 0.24f,
                    )
                }
                drawPath(
                    path = path,
                    color = accentColor,
                    style = Stroke(
                        width = strongStroke,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                    ),
                )
            }

            ChartType.BAR -> {
                val values = listOf(0.42f, 0.74f, 0.58f, 0.9f, 0.66f)
                val gap = size.width * 0.06f
                val barWidth = (size.width - gap * (values.size - 1)) / values.size
                values.forEachIndexed { index, value ->
                    val left = index * (barWidth + gap)
                    drawRoundRect(
                        color = accentColor.copy(alpha = 0.55f + index * 0.08f),
                        topLeft = Offset(left, size.height * (1f - value)),
                        size = androidx.compose.ui.geometry.Size(barWidth, size.height * value),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                    )
                }
            }

            ChartType.DONUT -> {
                val diameter = minOf(size.width, size.height) * 0.82f
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = 12.dp.toPx()),
                )
                drawArc(
                    color = accentColor,
                    startAngle = -90f,
                    sweepAngle = 258f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round),
                )
            }

            ChartType.GAUGE -> {
                val diameter = minOf(size.width, size.height) * 0.86f
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
                drawArc(
                    color = trackColor,
                    startAngle = 150f,
                    sweepAngle = 240f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round),
                )
                drawArc(
                    color = accentColor,
                    startAngle = 150f,
                    sweepAngle = 174f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round),
                )
            }

            ChartType.RADAR -> {
                val webPoints = listOf(
                    Offset(size.width * 0.5f, size.height * 0.05f),
                    Offset(size.width * 0.95f, size.height * 0.4f),
                    Offset(size.width * 0.78f, size.height * 0.94f),
                    Offset(size.width * 0.22f, size.height * 0.94f),
                    Offset(size.width * 0.05f, size.height * 0.4f),
                )
                val dataPoints = listOf(
                    Offset(size.width * 0.5f, size.height * 0.16f),
                    Offset(size.width * 0.83f, size.height * 0.43f),
                    Offset(size.width * 0.67f, size.height * 0.79f),
                    Offset(size.width * 0.31f, size.height * 0.82f),
                    Offset(size.width * 0.22f, size.height * 0.44f),
                )
                val webPath = webPoints.toClosedPath()
                val dataPath = dataPoints.toClosedPath()
                drawPath(path = webPath, color = trackColor, style = Stroke(width = thinStroke))
                drawPath(path = dataPath, color = accentColor.copy(alpha = 0.2f), style = Fill)
                drawPath(
                    path = dataPath,
                    color = accentColor,
                    style = Stroke(width = strongStroke, join = StrokeJoin.Round),
                )
            }

            ChartType.PIE -> {
                val diameter = minOf(size.width, size.height) * 0.84f
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
                drawArc(
                    color = accentColor.copy(alpha = 0.35f),
                    startAngle = -90f,
                    sweepAngle = 110f,
                    useCenter = true,
                    topLeft = topLeft,
                    size = arcSize,
                )
                drawArc(
                    color = accentColor.copy(alpha = 0.65f),
                    startAngle = 20f,
                    sweepAngle = 85f,
                    useCenter = true,
                    topLeft = topLeft,
                    size = arcSize,
                )
                drawArc(
                    color = accentColor,
                    startAngle = 105f,
                    sweepAngle = 135f,
                    useCenter = true,
                    topLeft = topLeft,
                    size = arcSize,
                )
                drawArc(
                    color = accentColor.copy(alpha = 0.82f),
                    startAngle = 240f,
                    sweepAngle = 30f,
                    useCenter = true,
                    topLeft = topLeft,
                    size = arcSize,
                )
            }
        }
    }
}

private fun List<Offset>.toClosedPath(): Path = Path().also { path ->
    forEachIndexed { index, point ->
        if (index == 0) {
            path.moveTo(point.x, point.y)
        } else {
            path.lineTo(point.x, point.y)
        }
    }
    path.close()
}
