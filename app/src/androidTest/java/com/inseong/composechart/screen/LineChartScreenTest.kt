package com.inseong.composechart.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
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
    fun lineChartScreen_showsNormalSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                LineChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun lineChartScreen_showsLargeSizeSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                LineChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Large Size (400dp)").assertIsDisplayed()
    }
}
