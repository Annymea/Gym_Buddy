package com.example.gymbuddy.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewScreen
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewUiState
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewViewModelContract
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class WorkoutOverviewScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun workoutOverview_showsAddWorkoutButtonIfNoWorkouts() {
        val workouts = emptyList<Workout>()

        composeTestRule.setContent {
            WorkoutOverviewScreen(
                viewModel = FakeWorkoutOverviewViewModel(workouts),
                onExecuteWorkout = { }
            )
        }

        composeTestRule.onNodeWithText("Add a workout").assertIsDisplayed()
    }

    @Test
    fun workoutOverview_showsWorkoutNameIfWorkouts() {
        val workouts =
            listOf(
                Workout(name = "Workout 1"),
                Workout(name = "Workout 2")
            )

        composeTestRule.setContent {
            WorkoutOverviewScreen(
                viewModel = FakeWorkoutOverviewViewModel(workouts),
                onExecuteWorkout = { }
            )
        }

        workouts.forEach { workout ->
            composeTestRule.onNodeWithText(workout.name).assertIsDisplayed()
        }
    }
}

class FakeWorkoutOverviewViewModel(
    override val workouts: List<Workout>
) : WorkoutOverviewViewModelContract {
    override val uiState: MutableStateFlow<WorkoutOverviewUiState> =
        MutableStateFlow(WorkoutOverviewUiState.NoWorkouts)

    init {
        if (workouts.isNotEmpty()) {
            uiState.value = WorkoutOverviewUiState.Workouts
        } else {
            uiState.value = WorkoutOverviewUiState.NoWorkouts
        }
    }
}
