package com.inseong.composechart

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
        composeTestRule.onNodeWithText("Line Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun navigation_clickBarChart_showsBarChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Bar Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun navigation_clickDonutChart_showsDonutChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Donut Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun navigation_clickGaugeChart_showsGaugeChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Gauge Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun navigation_clickScatterChart_showsScatterChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Scatter Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun navigation_clickBubbleChart_showsBubbleChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Bubble Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun navigation_clickRadarChart_showsRadarChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Radar Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun navigation_clickPieChart_showsPieChartScreen() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Pie Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun navigation_backFromDetailReturnsToGallery() {
        composeTestRule.setContent {
            ComposeChartTheme { ChartApp() }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Line Chart").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("\u2190").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Compose Chart Gallery").assertIsDisplayed()
    }
}
