package com.poetpal

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.poetpal.ui.PoetPalApp
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PoetPalApp(navController)
        }
    }

    @Test
    fun appStartsAtHome() {
        composeTestRule
            .onNodeWithText("Home")
            .assertIsDisplayed()
    }

    @Test
    fun navigateToWrite() {
        composeTestRule
            .onNodeWithContentDescription("navigate to writing page")
            .performClick()
        composeTestRule
            .onNodeWithText("Write")
            .assertIsDisplayed()
    }

    @Test
    fun navigateToRead() {
        composeTestRule
            .onNodeWithContentDescription("navigate to reading page")
            .performClick()
        composeTestRule
            .onNodeWithText("Read")
            .assertIsDisplayed()
    }

    @Test
    fun navigateToDict() {
        composeTestRule
            .onNodeWithContentDescription("navigate to rhyming dictionary page")
            .performClick()
        composeTestRule
            .onNodeWithText("Rhyming Dictionary")
            .assertIsDisplayed()
    }

    @Test
    fun openAndCloseSidePanel() {
        composeTestRule
            .onNodeWithContentDescription("Open side panel")
            .performClick()
        composeTestRule
            .onNodeWithText("Help")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Close sidepanel")
            .performClick()
        composeTestRule
            .onNodeWithText("Home")
            .assertIsDisplayed()
    }

    @Test
    fun navigateBackReturnsToHome() {
        composeTestRule
            .onNodeWithContentDescription("navigate to writing page")
            .performClick()
        composeTestRule
            .onNodeWithContentDescription("navigate back")
            .performClick()
        composeTestRule
            .onNodeWithText("Home")
            .assertIsDisplayed()
    }
}
