package com.inseong.composechart.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.inseong.composechart.ui.theme.ComposeChartTheme
import org.junit.Rule
import org.junit.Test

class DonutChartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun donutChartScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            ComposeChartTheme {
                DonutChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun donutChartScreen_showsTitle() {
        composeTestRule.setContent {
            ComposeChartTheme {
                DonutChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Donut Chart").assertIsDisplayed()
    }

    @Test
    fun donutChartScreen_showsDefaultScenario() {
        composeTestRule.setContent {
            ComposeChartTheme {
                DonutChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
    }

    @Test
    fun donutChartScreen_showsExpandedScenario() {
        composeTestRule.setContent {
            ComposeChartTheme {
                DonutChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Expanded").assertIsDisplayed()
    }
}
