package com.inseong.composechart.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.inseong.composechart.ui.theme.ComposeChartTheme
import org.junit.Rule
import org.junit.Test

class GaugeChartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun gaugeChartScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            ComposeChartTheme {
                GaugeChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun gaugeChartScreen_showsTitle() {
        composeTestRule.setContent {
            ComposeChartTheme {
                GaugeChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Gauge Chart").assertIsDisplayed()
    }

    @Test
    fun gaugeChartScreen_showsNormalSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                GaugeChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun gaugeChartScreen_showsLargeSizeSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                GaugeChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Large Size (400dp)").assertIsDisplayed()
    }
}
