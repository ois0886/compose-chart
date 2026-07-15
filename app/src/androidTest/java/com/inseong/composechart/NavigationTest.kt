package com.inseong.composechart

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.inseong.composechart.ui.theme.ComposeChartTheme
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun navigation_clickLineChart_showsLineChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        openChart(ChartType.LINE)
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
    }

    @Test
    fun navigation_clickBarChart_showsBarChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        openChart(ChartType.BAR)
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
    }

    @Test
    fun navigation_clickDonutChart_showsDonutChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        openChart(ChartType.DONUT)
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
    }

    @Test
    fun navigation_clickGaugeChart_showsGaugeChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        openChart(ChartType.GAUGE)
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
    }

    @Test
    fun navigation_clickRadarChart_showsRadarChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        openChart(ChartType.RADAR)
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
    }

    @Test
    fun navigation_clickPieChart_showsPieChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        openChart(ChartType.PIE)
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
    }

    @Test
    fun navigation_backFromDetailReturnsToGallery() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        openChart(ChartType.LINE)
        composeTestRule.onNodeWithContentDescription("Back to chart gallery").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("COMPOSE CHART").assertIsDisplayed()
    }

    private fun openChart(chartType: ChartType) {
        composeTestRule
            .onNodeWithTag("chart-gallery-grid")
            .performScrollToNode(hasText(chartType.displayName))
        composeTestRule.onNodeWithText(chartType.displayName).performClick()
        composeTestRule.waitForIdle()
    }
}
