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
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.line.LineChart

@Preview(name = "Line - basic", showBackground = true, widthDp = 320, heightDp = 220)
@Composable
private fun LineChartBasicPreview() {
    Box(Modifier.background(Color.White).padding(8.dp)) {
        LineChart(
            data = LineChartData.fromValues(
                values = listOf(10f, 25f, 18f, 32f, 22f, 40f),
                xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
            ),
            modifier = Modifier.fillMaxWidth().height(200.dp),
        )
    }
}

@Preview(
    name = "Line - dark",
    showBackground = true,
    widthDp = 320,
    heightDp = 220,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun LineChartDarkPreview() {
    Box(Modifier.background(Color(0xFF121212)).padding(8.dp)) {
        LineChart(
            data = LineChartData.fromValues(
                values = listOf(10f, 25f, 18f, 32f, 22f, 40f),
                xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
            ),
            modifier = Modifier.fillMaxWidth().height(200.dp),
        )
    }
}
