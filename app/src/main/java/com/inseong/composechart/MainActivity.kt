package com.inseong.composechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.PieChartData
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.pie.PieChart
import com.inseong.composechart.style.GaugeChartStyle
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

private val sampleLineData = LineChartData.fromValues(
    values = listOf(15f, 28f, 22f, 35f, 30f, 42f),
    xLabels = listOf("1월", "2월", "3월", "4월", "5월", "6월"),
)
private val sampleBarData = BarChartData.simple(
    values = listOf(30f, 45f, 28f, 55f, 38f),
    labels = listOf("1월", "2월", "3월", "4월", "5월"),
)
private val samplePieData = PieChartData.fromValues(
    values = mapOf("식비" to 40f, "교통" to 25f, "쇼핑" to 20f, "기타" to 15f),
)
private val sampleGaugeData = GaugeChartData(value = 72f, maxValue = 100f, label = "달성률")

@Composable
fun ChartSampleScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ── 기본 크기 샘플 ──
        Text(text = "Line Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LineChart(
            data = sampleLineData,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            style = LineChartStyle(curved = true, gradientFill = true),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Bar Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        BarChart(
            data = sampleBarData,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Donut Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        PieChart(
            data = samplePieData,
            modifier = Modifier.size(200.dp),
            style = PieChartStyle(holeRadius = 0.6f),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Gauge Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        GaugeChart(
            data = sampleGaugeData,
            modifier = Modifier.size(180.dp),
        )

        Spacer(modifier = Modifier.height(48.dp))

        // ── 작은 크기 샘플 ──
        Text(
            text = "Small Size (60dp)",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LineChart(
                data = sampleLineData,
                modifier = Modifier.size(60.dp),
                style = LineChartStyle(curved = true, gradientFill = true),
            )
            Spacer(modifier = Modifier.width(8.dp))
            BarChart(
                data = sampleBarData,
                modifier = Modifier.size(60.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            PieChart(
                data = samplePieData,
                modifier = Modifier.size(60.dp),
                style = PieChartStyle(holeRadius = 0.6f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            GaugeChart(
                data = sampleGaugeData,
                modifier = Modifier.size(60.dp),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── 큰 크기 샘플 ──
        Text(
            text = "Large Size",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LineChart(
            data = sampleLineData,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            style = LineChartStyle(curved = true, gradientFill = true),
        )
        Spacer(modifier = Modifier.height(16.dp))
        BarChart(
            data = sampleBarData,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        PieChart(
            data = samplePieData,
            modifier = Modifier.size(350.dp),
            style = PieChartStyle(holeRadius = 0.6f),
        )
        Spacer(modifier = Modifier.height(16.dp))
        GaugeChart(
            data = sampleGaugeData,
            modifier = Modifier.size(350.dp),
            style = GaugeChartStyle(sweepAngle = 360f),
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
