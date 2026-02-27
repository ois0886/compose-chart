package com.inseong.composechart.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.inseong.composechart.ui.theme.ComposeChartTheme
import org.junit.Rule
import org.junit.Test

class ScatterChartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun scatterChartScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            ComposeChartTheme {
                ScatterChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun scatterChartScreen_showsTitle() {
        composeTestRule.setContent {
            ComposeChartTheme {
                ScatterChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Scatter Chart").assertIsDisplayed()
    }

    @Test
    fun scatterChartScreen_showsNormalSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                ScatterChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun scatterChartScreen_showsLargeSizeSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                ScatterChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Large Size (400dp)").assertIsDisplayed()
    }
}
