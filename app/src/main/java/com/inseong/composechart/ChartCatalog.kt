package com.inseong.composechart

import androidx.compose.ui.graphics.Color

enum class ChartType(
    val displayName: String,
    val category: String,
    val description: String,
    val accentColor: Color,
) {
    LINE(
        displayName = "Line Chart",
        category = "Trend",
        description = "Track change and movement over time.",
        accentColor = ChartDefaults.colors[0],
    ),
    BAR(
        displayName = "Bar Chart",
        category = "Comparison",
        description = "Compare categories at a glance.",
        accentColor = ChartDefaults.colors[4],
    ),
    DONUT(
        displayName = "Donut Chart",
        category = "Composition",
        description = "Show how each part contributes to a whole.",
        accentColor = ChartDefaults.colors[2],
    ),
    GAUGE(
        displayName = "Gauge Chart",
        category = "Progress",
        description = "Communicate progress toward a target.",
        accentColor = ChartDefaults.colors[1],
    ),
    RADAR(
        displayName = "Radar Chart",
        category = "Profile",
        description = "Compare a profile across several dimensions.",
        accentColor = ChartDefaults.colors[3],
    ),
    PIE(
        displayName = "Pie Chart",
        category = "Composition",
        description = "Present a compact proportional breakdown.",
        accentColor = ChartDefaults.colors[5],
    ),
}
