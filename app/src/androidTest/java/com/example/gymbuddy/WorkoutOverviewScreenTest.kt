package com.example.gymbuddy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.gymbuddy.data.localdatabase.Plan
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewScreen
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewViewModelContract
import org.junit.Rule
import org.junit.Test

class WorkoutOverviewScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun workoutOverview_showsAddWorkoutButtonIfNoWorkouts() {
        val workouts = emptyList<Plan>()

        composeTestRule.setContent {
            WorkoutOverviewScreen(
                workoutOverviewViewModel = FakeWorkoutOverviewViewModel(workouts)
            )
        }

        composeTestRule.onNodeWithText("Add a workout").assertIsDisplayed()
    }

    @Test
    fun workoutOverview_showsWorkoutNameIfWorkouts() {
        val workouts = listOf(Plan(planName = "Workout 1"), Plan(planName = "Workout 2"))

        composeTestRule.setContent {
            WorkoutOverviewScreen(
                workoutOverviewViewModel = FakeWorkoutOverviewViewModel(workouts)
            )
        }

        workouts.forEach { workout ->
            composeTestRule.onNodeWithText(workout.planName).assertIsDisplayed()
        }
    }
}

class FakeWorkoutOverviewViewModel(override val workouts: List<Plan>) :
    WorkoutOverviewViewModelContract
