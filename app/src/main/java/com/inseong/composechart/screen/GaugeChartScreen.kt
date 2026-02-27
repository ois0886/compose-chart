package com.inseong.composechart.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inseong.composechart.emptyGaugeData
import com.inseong.composechart.extremeGaugeData
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.invalidGaugeData
import com.inseong.composechart.normalGaugeData

@Composable
fun GaugeChartScreen(onBack: () -> Unit) {
    BackHandler(onBack = onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(onClick = onBack) { Text("\u2190") }
            Text(text = "Gauge Chart", style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Normal", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        GaugeChart(
            data = normalGaugeData,
            modifier = Modifier.size(180.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Large Size (400dp)", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        GaugeChart(
            data = normalGaugeData,
            modifier = Modifier.size(350.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Small Size (60dp)", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        GaugeChart(
            data = normalGaugeData,
            modifier = Modifier.size(60.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Extreme Values (1 ~ 10000)", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        GaugeChart(
            data = extremeGaugeData,
            modifier = Modifier.size(180.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Invalid Data (NaN / Infinity)", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        GaugeChart(
            data = invalidGaugeData,
            modifier = Modifier.size(180.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Empty Data", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        GaugeChart(
            data = emptyGaugeData,
            modifier = Modifier.size(180.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}
