package com.inseong.composechart.previews

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.BarEntry
import com.inseong.composechart.data.BarGroup

private fun sampleBarData() = BarChartData(
    groups = listOf(
        BarGroup(entries = listOf(BarEntry(values = listOf(30f))), label = "Mon"),
        BarGroup(entries = listOf(BarEntry(values = listOf(45f))), label = "Tue"),
        BarGroup(entries = listOf(BarEntry(values = listOf(28f))), label = "Wed"),
        BarGroup(entries = listOf(BarEntry(values = listOf(60f))), label = "Thu"),
        BarGroup(entries = listOf(BarEntry(values = listOf(38f))), label = "Fri"),
    ),
)

@Preview(name = "Bar - basic", showBackground = true, widthDp = 320, heightDp = 220)
@Composable
private fun BarChartBasicPreview() {
    Box(Modifier.background(Color.White).padding(8.dp)) {
        BarChart(
            data = sampleBarData(),
            modifier = Modifier.fillMaxWidth().height(200.dp),
        )
    }
}

@Preview(
    name = "Bar - dark",
    showBackground = true,
    widthDp = 320,
    heightDp = 220,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun BarChartDarkPreview() {
    Box(Modifier.background(Color(0xFF121212)).padding(8.dp)) {
        BarChart(
            data = sampleBarData(),
            modifier = Modifier.fillMaxWidth().height(200.dp),
        )
    }
}
