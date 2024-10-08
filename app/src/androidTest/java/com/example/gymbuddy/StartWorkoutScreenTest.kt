package com.example.gymbuddy

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.data.localdatabase.Plan
import com.example.gymbuddy.ui.old.startWorkout.StartWorkout
import com.example.gymbuddy.ui.old.startWorkout.StartWorkoutViewModelContract
import org.junit.Rule
import org.junit.Test

class StartWorkoutScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun startWorkout_displaysNoButtonWhenNoWorkouts() {
        val emptyWorkouts = listOf<Plan>()

        setContent(emptyWorkouts)

        composeTestRule.onNodeWithText("Training starten!!").assertIsDisplayed()
        composeTestRule.onNodeWithTag("workoutButton").assertIsNotDisplayed()
    }

    @Test
    fun startWorkout_displaysButtonWhenWorkoutsAreAvailable() {
        val workouts = listOf(
            Plan(planName = "Test 1", id = 1),
            Plan(planName = "Test 2", id = 2)
        )

        setContent(workouts)

        composeTestRule.onNodeWithText("Training starten!!").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("workoutButton").assertCountEquals(2)
        composeTestRule.onNodeWithText("Test 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test 2").assertIsDisplayed()
    }

    @Test
    fun startWorkout_ClickOnWorkoutIsRegistered() {
        val workouts = listOf(
            Plan(planName = "Test 1", id = 1),
            Plan(planName = "Test 2", id = 2)
        )

        var isButtonClicked = false

        setContent(workouts, onSelect = { isButtonClicked = true })

        composeTestRule.onNodeWithText("Test 1").performClick()

        assert(isButtonClicked)
    }

    private fun setContent(workouts: List<Plan>, onSelect: () -> Unit = {}) {
        composeTestRule.setContent {
            StartWorkout(
                startWorkoutViewModel = FakeStartWorkoutViewModel(workouts),
                onSelectWorkout = { onSelect() }
            )
        }
    }
}

class FakeStartWorkoutViewModel(givenWorkouts: List<Plan>) : StartWorkoutViewModelContract {
    override val workouts: List<Plan> = givenWorkouts
}
