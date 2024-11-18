package com.example.gymbuddy.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.gymbuddy.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DashboardScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    // mainactivity is used because it is annotated with @AndroidEntryPoint
    // If other usecases are needed I need to think about a test activity
    // this annotation is needed for tests marked with HiltAndroidTest
    // HiltAndroidTests are needed if I need DI within the composable
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun trainingsCalender_showsHighlightedDays() {
        composeTestRule
            .onNodeWithTag("dashboard_title")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("trainings_calender")
            .assertIsDisplayed()
    }
}
