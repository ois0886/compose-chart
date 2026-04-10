package com.inseong.composechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.inseong.composechart.screen.BarChartScreen
import com.inseong.composechart.screen.DonutChartScreen
import com.inseong.composechart.screen.GaugeChartScreen
import com.inseong.composechart.screen.LineChartScreen
import com.inseong.composechart.screen.PieChartScreen
import com.inseong.composechart.screen.RadarChartScreen
import com.inseong.composechart.ui.theme.ComposeChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeChartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChartApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ChartApp(modifier: Modifier = Modifier) {
    var currentScreenName by rememberSaveable { mutableStateOf<String?>(null) }
    val currentScreen = currentScreenName?.let(ChartType::valueOf)

    when (currentScreen) {
        null -> ChartGalleryScreen(
            onChartSelected = { currentScreenName = it.name },
            modifier = modifier,
        )
        ChartType.LINE -> LineChartScreen(onBack = { currentScreenName = null })
        ChartType.BAR -> BarChartScreen(onBack = { currentScreenName = null })
        ChartType.DONUT -> DonutChartScreen(onBack = { currentScreenName = null })
        ChartType.GAUGE -> GaugeChartScreen(onBack = { currentScreenName = null })
        ChartType.RADAR -> RadarChartScreen(onBack = { currentScreenName = null })
        ChartType.PIE -> PieChartScreen(onBack = { currentScreenName = null })
    }
}

@Preview(showBackground = true)
@Composable
fun ChartAppPreview() {
    ComposeChartTheme {
        ChartApp()
    }
}
