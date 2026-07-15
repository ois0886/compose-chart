package com.inseong.composechart.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal enum class ChartSample(
    val displayName: String,
    val description: String,
    val chartDimension: Dp,
) {
    DEFAULT(
        displayName = "Default",
        description = "Balanced defaults for a typical production layout.",
        chartDimension = 220.dp,
    ),
    EXPANDED(
        displayName = "Expanded",
        description = "A larger canvas for checking spacing, labels, and visual rhythm.",
        chartDimension = 360.dp,
    ),
    COMPACT(
        displayName = "Compact",
        description = "A constrained canvas that reveals minimum-size behavior.",
        chartDimension = 80.dp,
    ),
    EXTREME(
        displayName = "Extreme values",
        description = "A wide numerical range for validating scale and normalization.",
        chartDimension = 220.dp,
    ),
    INVALID(
        displayName = "Invalid input",
        description = "NaN, infinity, and negative values exercise defensive handling.",
        chartDimension = 220.dp,
    ),
    EMPTY(
        displayName = "Empty data",
        description = "An empty dataset confirms the chart remains stable without content.",
        chartDimension = 220.dp,
    );

    val previewHeight: Dp
        get() = if (this == COMPACT) 176.dp else chartDimension + 48.dp
}

@Composable
internal fun ChartShowcaseScreen(
    title: String,
    description: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(ChartSample) -> Unit,
) {
    BackHandler(onBack = onBack)

    var selectedSampleName by rememberSaveable(title) { mutableStateOf(ChartSample.DEFAULT.name) }
    val selectedSample = ChartSample.entries.firstOrNull { it.name == selectedSampleName }
        ?: ChartSample.DEFAULT

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        ShowcaseTopBar(
            title = title,
            onBack = onBack,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Test scenario",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    LazyRow(
                        modifier = Modifier.testTag("chart-scenario-picker"),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            items = ChartSample.entries.toList(),
                            key = ChartSample::name,
                        ) { sample ->
                            FilterChip(
                                selected = sample == selectedSample,
                                onClick = { selectedSampleName = sample.name },
                                label = { Text(sample.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                ),
                            )
                        }
                    }
                }
            }
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.large,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(999.dp),
                            ) {
                                Text(
                                    text = "LIVE PREVIEW",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                )
                            }
                            Row(
                                modifier = Modifier.clearAndSetSemantics { },
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = CircleShape,
                                        ),
                                )
                                Text(
                                    text = "Ready",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        Text(
                            text = selectedSample.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(selectedSample.previewHeight)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                    shape = MaterialTheme.shapes.medium,
                                )
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            content(selectedSample)
                        }
                    }
                }
            }
            item {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            text = "i",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clearAndSetSemantics { },
                        )
                        Text(
                            text = "Switch scenarios to inspect resizing and defensive input handling without rendering every chart at once.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowcaseTopBar(
    title: String,
    onBack: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.semantics {
                    contentDescription = "Back to chart gallery"
                },
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    modifier = Modifier.clearAndSetSemantics { },
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}
