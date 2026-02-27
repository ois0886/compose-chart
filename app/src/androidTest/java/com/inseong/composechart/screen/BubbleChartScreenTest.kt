package com.inseong.composechart.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.inseong.composechart.ui.theme.ComposeChartTheme
import org.junit.Rule
import org.junit.Test

class BubbleChartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bubbleChartScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            ComposeChartTheme {
                BubbleChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun bubbleChartScreen_showsTitle() {
        composeTestRule.setContent {
            ComposeChartTheme {
                BubbleChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Bubble Chart").assertIsDisplayed()
    }

    @Test
    fun bubbleChartScreen_showsNormalSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                BubbleChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun bubbleChartScreen_showsLargeSizeSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                BubbleChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Large Size (400dp)").assertIsDisplayed()
    }
}
