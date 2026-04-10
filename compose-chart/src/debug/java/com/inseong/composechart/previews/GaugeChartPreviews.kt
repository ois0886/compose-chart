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
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.style.GaugeChartStyle

@Preview(name = "Gauge - semicircle", showBackground = true, widthDp = 240, heightDp = 200)
@Composable
private fun GaugeChartSemicirclePreview() {
    Box(Modifier.background(Color.White).padding(8.dp)) {
        GaugeChart(
            data = GaugeChartData(value = 72f, maxValue = 100f, label = "Progress"),
            modifier = Modifier.size(200.dp),
        )
    }
}

@Preview(name = "Gauge - full circle", showBackground = true, widthDp = 240, heightDp = 240)
@Composable
private fun GaugeChartFullCirclePreview() {
    Box(Modifier.background(Color.White).padding(8.dp)) {
        GaugeChart(
            data = GaugeChartData(value = 45f, maxValue = 100f, label = "Score"),
            modifier = Modifier.size(200.dp),
            style = GaugeChartStyle(sweepAngle = 360f),
        )
    }
}

@Preview(
    name = "Gauge - dark",
    showBackground = true,
    widthDp = 240,
    heightDp = 240,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun GaugeChartDarkPreview() {
    Box(Modifier.background(Color(0xFF121212)).padding(8.dp)) {
        GaugeChart(
            data = GaugeChartData(value = 72f, maxValue = 100f, label = "Progress"),
            modifier = Modifier.size(200.dp),
        )
    }
}
