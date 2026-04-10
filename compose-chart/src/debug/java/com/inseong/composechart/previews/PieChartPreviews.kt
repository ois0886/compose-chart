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
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.pie.PieChart

private fun samplePieData() = DonutChartData(
    slices = listOf(
        DonutSlice(50f, "Yes"),
        DonutSlice(35f, "No"),
        DonutSlice(15f, "Maybe"),
    ),
)

@Preview(name = "Pie - basic", showBackground = true, widthDp = 240, heightDp = 240)
@Composable
private fun PieChartBasicPreview() {
    Box(Modifier.background(Color.White).padding(8.dp)) {
        PieChart(
            data = samplePieData(),
            modifier = Modifier.size(220.dp),
        )
    }
}

@Preview(
    name = "Pie - dark",
    showBackground = true,
    widthDp = 240,
    heightDp = 240,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PieChartDarkPreview() {
    Box(Modifier.background(Color(0xFF121212)).padding(8.dp)) {
        PieChart(
            data = samplePieData(),
            modifier = Modifier.size(220.dp),
        )
    }
}
