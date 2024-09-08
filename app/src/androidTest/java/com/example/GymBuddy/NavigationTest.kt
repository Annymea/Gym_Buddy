package com.example.GymBuddy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.GymBuddy.ui.navigation.AppNavigation
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        navController = TestNavHostController(
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        composeTestRule.setContent {
            AppNavigation(navController = navController)
        }
    }

    @Test
    fun appNavigation_verifyDashboardIsDisplayed() {
        composeTestRule.onNodeWithText("Dashboard Content").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigateToPlanList() {
        composeTestRule.onNodeWithText("Plan erstellen").performClick()

        composeTestRule.onNodeWithText("Hier könnten ihre Listen stehen").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigateToCreatePlan() {
        composeTestRule.onNodeWithText("Plan erstellen").performClick()
        composeTestRule.onNodeWithText("Neuen Plan erstellen").performClick()

        composeTestRule.onNodeWithText("Dein Plan").assertIsDisplayed()
    }
}
