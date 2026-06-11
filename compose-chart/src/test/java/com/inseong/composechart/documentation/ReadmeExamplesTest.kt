package com.inseong.composechart.documentation

import androidx.compose.ui.unit.dp
import com.inseong.composechart.data.BarChartData
import com.inseong.composechart.data.DonutChartData
import com.inseong.composechart.data.DonutSlice
import com.inseong.composechart.data.GaugeChartData
import com.inseong.composechart.data.LineChartData
import com.inseong.composechart.data.RadarChartData
import com.inseong.composechart.style.AxisStyle
import com.inseong.composechart.style.GridStyle
import com.inseong.composechart.style.LineChartStyle
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * README quick start snippets are mirrored here so doc examples stay compile-safe.
 */
class ReadmeExamplesTest {

    @Test
    fun readme_lineChartExample_compilesWithCurrentFactorySignature() {
        val data = LineChartData.fromValues(
            values = listOf(10f, 25f, 18f, 32f, 22f),
            xLabels = listOf("1월", "2월", "3월", "4월", "5월"),
        )

        assertEquals(5, data.series.first().points.size)
    }

    @Test
    fun readme_barChartExample_compiles() {
        val data = BarChartData.simple(
            values = listOf(30f, 45f, 25f, 60f),
            labels = listOf("1분기", "2분기", "3분기", "4분기"),
        )

        assertEquals(4, data.groups.size)
    }

    @Test
    fun readme_donutAndPieExamples_compile() {
        val donutData = DonutChartData(
            slices = listOf(
                DonutSlice(40f, "식비"),
                DonutSlice(25f, "교통"),
                DonutSlice(35f, "기타"),
            ),
        )
        val pieData = DonutChartData.fromValues(30f, 25f, 45f)

        assertEquals(3, donutData.slices.size)
        assertEquals(3, pieData.slices.size)
    }

    @Test
    fun readme_gaugeAndRadarExamples_compile() {
        val gaugeData = GaugeChartData(value = 72f, maxValue = 100f, label = "점수")
        val radarData = RadarChartData.single(
            values = listOf(80f, 65f, 90f, 70f, 85f),
            axisLabels = listOf("STR", "DEX", "INT", "WIS", "CHA"),
        )

        assertEquals("점수", gaugeData.label)
        assertEquals(5, radarData.axisLabels.size)
    }

    @Test
    fun readme_manualYAxisRangeExample_compiles() {
        val style = LineChartStyle(
            axis = AxisStyle(
                showYAxis = true,
                yAxisMin = 0f,
                yAxisMax = 100f,
            ),
            lineWidth = 2.dp,
        )

        assertEquals(0f, style.axis.yAxisMin)
        assertEquals(100f, style.axis.yAxisMax)
    }

    @Test
    fun readme_styleExample_compilesWithCurrentGridDashPatternType() {
        val style = LineChartStyle(
            grid = GridStyle(
                showHorizontalLines = true,
                dashPattern = listOf(10f, 5f),
            ),
        )

        assertEquals(listOf(10f, 5f), style.grid.dashPattern)
    }
}
