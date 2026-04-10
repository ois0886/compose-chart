package com.inseong.composechart.previews

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.radar.RadarChart

private fun sampleRadarData() = RadarChartData.single(
    values = listOf(80f, 65f, 90f, 70f, 85f),
    axisLabels = listOf("STR", "DEX", "INT", "WIS", "CHA"),
)

@Preview(name = "Radar - basic", showBackground = true, widthDp = 260, heightDp = 260)
@Composable
private fun RadarChartBasicPreview() {
    Box(Modifier.background(Color.White).padding(8.dp)) {
        RadarChart(
            data = sampleRadarData(),
            modifier = Modifier.size(240.dp),
        )
    }
}

@Preview(
    name = "Radar - dark",
    showBackground = true,
    widthDp = 260,
    heightDp = 260,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun RadarChartDarkPreview() {
    Box(Modifier.background(Color(0xFF121212)).padding(8.dp)) {
        RadarChart(
            data = sampleRadarData(),
            modifier = Modifier.size(240.dp),
        )
    }
}
