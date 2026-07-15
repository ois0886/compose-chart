package com.inseong.composechart

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import com.inseong.composechart.bar.BarChart
import com.inseong.composechart.donut.DonutChart
import com.inseong.composechart.gauge.GaugeChart
import com.inseong.composechart.line.LineChart
import com.inseong.composechart.pie.PieChart
import com.inseong.composechart.radar.RadarChart
import com.inseong.composechart.style.BarChartStyle
import com.inseong.composechart.style.DonutChartStyle
import com.inseong.composechart.style.GaugeChartStyle
import com.inseong.composechart.style.LineChartStyle
import com.inseong.composechart.style.PieChartStyle
import com.inseong.composechart.style.RadarChartStyle
import com.inseong.composechart.ui.theme.ComposeChartTheme
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import org.junit.Rule
import org.junit.Test

private const val SNAPSHOT_TAG = "readme-chart-snapshot"

class ReadmeScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun captureReadmeCharts_whenExplicitlyRequested() {
        val arguments = InstrumentationRegistry.getArguments()
        if (arguments.getString(RECORD_ARGUMENT) != "true") return

        val themeName = requireNotNull(arguments.getString(THEME_ARGUMENT)) {
            "-$THEME_ARGUMENT must be light or dark"
        }
        require(themeName in SUPPORTED_THEMES) {
            "Unsupported screenshot theme: $themeName"
        }
        val darkTheme = themeName == DARK_THEME
        var chartType by mutableStateOf(ChartType.LINE)

        composeTestRule.setContent {
            ComposeChartTheme(darkTheme = darkTheme) {
                ReadmeChartSnapshot(chartType = chartType)
            }
        }

        ChartType.entries.forEach { type ->
            composeTestRule.runOnIdle { chartType = type }
            composeTestRule.waitForIdle()
            val outputFile = screenshotOutputFile(
                chartType = type,
                themeName = themeName,
            )
            FileOutputStream(outputFile).use { output ->
                val wasWritten = composeTestRule
                    .onNodeWithTag(SNAPSHOT_TAG)
                    .captureToImage()
                    .asAndroidBitmap()
                    .compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, output)
                check(wasWritten) { "Failed to write ${outputFile.name}" }
            }
        }
    }

    private fun screenshotOutputFile(chartType: ChartType, themeName: String): File {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val outputDirectory = File(
            requireNotNull(context.getExternalFilesDir(null)),
            OUTPUT_DIRECTORY,
        )
        check(outputDirectory.isDirectory || outputDirectory.mkdirs()) {
            "Failed to create screenshot directory: $outputDirectory"
        }
        val chartName = chartType.name.lowercase(Locale.ROOT)
        return File(outputDirectory, "$chartName-chart-$themeName.png")
    }

    private companion object {
        const val RECORD_ARGUMENT = "recordReadmeScreenshots"
        const val THEME_ARGUMENT = "screenshotTheme"
        const val DARK_THEME = "dark"
        const val OUTPUT_DIRECTORY = "readme-screenshots"
        const val PNG_QUALITY = 100
        val SUPPORTED_THEMES = setOf("light", DARK_THEME)
    }
}

@Composable
private fun ReadmeChartSnapshot(chartType: ChartType) {
    Box(
        modifier = Modifier
            .size(width = 360.dp, height = 280.dp)
            .background(MaterialTheme.colorScheme.background)
            .testTag(SNAPSHOT_TAG)
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        when (chartType) {
            ChartType.LINE -> LineChart(
                data = normalLineData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                style = LineChartStyle(
                    curved = true,
                    gradientFill = true,
                    animationDurationMs = 0,
                ),
            )

            ChartType.BAR -> BarChart(
                data = normalBarData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                style = BarChartStyle(animationDurationMs = 0),
            )

            ChartType.DONUT -> DonutChart(
                data = normalDonutData,
                modifier = Modifier.size(230.dp),
                style = DonutChartStyle(
                    holeRadius = 0.6f,
                    animationDurationMs = 0,
                ),
            )

            ChartType.GAUGE -> GaugeChart(
                data = normalGaugeData,
                modifier = Modifier.size(230.dp),
                style = GaugeChartStyle(animationDurationMs = 0),
            )

            ChartType.RADAR -> RadarChart(
                data = normalRadarData,
                modifier = Modifier.size(230.dp),
                style = RadarChartStyle(animationDurationMs = 0),
            )

            ChartType.PIE -> PieChart(
                data = normalPieData,
                modifier = Modifier.size(230.dp),
                style = PieChartStyle(animationDurationMs = 0),
            )
        }
    }
}
