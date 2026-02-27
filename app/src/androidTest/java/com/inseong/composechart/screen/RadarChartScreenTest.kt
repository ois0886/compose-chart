package com.inseong.composechart.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.inseong.composechart.ui.theme.ComposeChartTheme
import org.junit.Rule
import org.junit.Test

class RadarChartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun radarChartScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            ComposeChartTheme {
                RadarChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun radarChartScreen_showsTitle() {
        composeTestRule.setContent {
            ComposeChartTheme {
                RadarChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Radar Chart").assertIsDisplayed()
    }

    @Test
    fun radarChartScreen_showsNormalSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                RadarChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Normal").assertIsDisplayed()
    }

    @Test
    fun radarChartScreen_showsLargeSizeSection() {
        composeTestRule.setContent {
            ComposeChartTheme {
                RadarChartScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Large Size (400dp)").assertIsDisplayed()
    }
}
