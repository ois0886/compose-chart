package com.inseong.composechart.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.inseong.composechart.ui.theme.ComposeChartTheme
import org.junit.Rule
import org.junit.Test

class LineChartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lineChartScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            ComposeChartTheme {
                LineChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun lineChartScreen_showsTitle() {
        composeTestRule.setContent {
            ComposeChartTheme {
                LineChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Line Chart").assertIsDisplayed()
    }

    @Test
    fun lineChartScreen_showsDefaultScenario() {
        composeTestRule.setContent {
            ComposeChartTheme {
                LineChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Default").assertIsDisplayed()
    }

    @Test
    fun lineChartScreen_showsExpandedScenario() {
        composeTestRule.setContent {
            ComposeChartTheme {
                LineChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Expanded").assertIsDisplayed()
    }

    @Test
    fun lineChartScreen_selectExtremeValues_updatesScenarioDescription() {
        composeTestRule.setContent {
            ComposeChartTheme {
                LineChartScreen(onBack = {})
            }
        }

        composeTestRule
            .onNodeWithTag("chart-scenario-picker")
            .performScrollToNode(hasText("Extreme values"))
        composeTestRule.onNodeWithText("Extreme values").performClick()

        composeTestRule
            .onNodeWithText("A wide numerical range for validating scale and normalization.")
            .assertIsDisplayed()
    }
}
