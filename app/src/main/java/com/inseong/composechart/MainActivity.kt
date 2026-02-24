package com.inseong.composechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.BarEntry
import com.inseong.composechart.data.BarGroup
import com.inseong.composechart.data.ChartPoint
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.LineSeries
import com.inseong.composechart.data.PieChartData
import com.inseong.composechart.data.PieSlice
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.pie.PieChart
import com.inseong.composechart.style.LineChartStyle
import com.inseong.composechart.style.PieChartStyle
import com.inseong.composechart.ui.theme.ComposeChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeChartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChartSampleScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ChartSampleScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Line Chart 샘플
        Text(text = "Line Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LineChart(
            data = LineChartData(
                series = listOf(
                    LineSeries(
                        points = listOf(
                            ChartPoint(0f, 15f, "15"),
                            ChartPoint(1f, 28f, "28"),
                            ChartPoint(2f, 22f, "22"),
                            ChartPoint(3f, 35f, "35"),
                            ChartPoint(4f, 30f, "30"),
                            ChartPoint(5f, 42f, "42"),
                        ),
                    ),
                ),
                xLabels = listOf("1월", "2월", "3월", "4월", "5월", "6월"),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            style = LineChartStyle(curved = true, gradientFill = true),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Bar Chart 샘플
        Text(text = "Bar Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        BarChart(
            data = BarChartData(
                groups = listOf(
                    BarGroup(entries = listOf(BarEntry(values = listOf(30f))), label = "1월"),
                    BarGroup(entries = listOf(BarEntry(values = listOf(45f))), label = "2월"),
                    BarGroup(entries = listOf(BarEntry(values = listOf(28f))), label = "3월"),
                    BarGroup(entries = listOf(BarEntry(values = listOf(55f))), label = "4월"),
                    BarGroup(entries = listOf(BarEntry(values = listOf(38f))), label = "5월"),
                ),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Donut Chart 샘플
        Text(text = "Donut Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        PieChart(
            data = PieChartData(
                slices = listOf(
                    PieSlice(40f, "식비"),
                    PieSlice(25f, "교통"),
                    PieSlice(20f, "쇼핑"),
                    PieSlice(15f, "기타"),
                ),
            ),
            modifier = Modifier.size(200.dp),
            style = PieChartStyle(holeRadius = 0.6f),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Gauge Chart 샘플
        Text(text = "Gauge Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        GaugeChart(
            data = GaugeChartData(value = 72f, maxValue = 100f, label = "달성률"),
            modifier = Modifier.size(180.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ChartSamplePreview() {
    ComposeChartTheme {
        ChartSampleScreen()
    }
}
