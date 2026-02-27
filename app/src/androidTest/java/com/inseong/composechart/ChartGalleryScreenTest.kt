package com.inseong.composechart

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.inseong.composechart.ui.theme.ComposeChartTheme
import org.junit.Rule
import org.junit.Test

class ChartGalleryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun galleryScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            ComposeChartTheme {
                ChartGalleryScreen(onChartSelected = {})
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun galleryScreen_showsAllChartButtons() {
        composeTestRule.setContent {
            ComposeChartTheme {
                ChartGalleryScreen(onChartSelected = {})
            }
        }
        composeTestRule.waitForIdle()

        ChartType.entries.forEach { chartType ->
            composeTestRule.onNodeWithText(chartType.displayName).assertIsDisplayed()
        }
    }

    @Test
    fun galleryScreen_showsTitle() {
        composeTestRule.setContent {
            ComposeChartTheme {
                ChartGalleryScreen(onChartSelected = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Compose Chart Gallery").assertIsDisplayed()
    }
}
