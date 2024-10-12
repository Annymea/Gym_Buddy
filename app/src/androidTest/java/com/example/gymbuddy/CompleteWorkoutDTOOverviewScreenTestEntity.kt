package com.example.gymbuddy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewScreen
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewUiState
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewViewModelContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

class CompleteWorkoutDTOOverviewScreenTestEntity {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun workoutOverview_showsAddWorkoutButtonIfNoWorkouts() {
        val workouts = emptyList<WorkoutDetailsEntity>()

        composeTestRule.setContent {
            WorkoutOverviewScreen(
                workoutOverviewViewModel = FakeWorkoutOverviewViewModel(workouts),
                onExecuteWorkout = { },
            )
        }

        composeTestRule.onNodeWithText("Add a workout").assertIsDisplayed()
    }

    @Test
    fun workoutOverview_showsWorkoutNameIfWorkouts() {
        val workouts =
            listOf(
                WorkoutDetailsEntity(workoutName = "Workout 1"),
                WorkoutDetailsEntity(workoutName = "Workout 2"),
            )

        composeTestRule.setContent {
            WorkoutOverviewScreen(
                workoutOverviewViewModel = FakeWorkoutOverviewViewModel(workouts),
                onExecuteWorkout = { },
            )
        }

        workouts.forEach { workout ->
            composeTestRule.onNodeWithText(workout.workoutName).assertIsDisplayed()
        }
    }
}

class FakeWorkoutOverviewViewModel(
    override val workouts: List<WorkoutDetailsEntity>,
) : WorkoutOverviewViewModelContract {
    override val uiState: StateFlow<WorkoutOverviewUiState>
        get() = _uiState

    private val _uiState =
        MutableStateFlow<WorkoutOverviewUiState>(WorkoutOverviewUiState.NoWorkouts)

    init {
        if (workouts.isNotEmpty()) {
            _uiState.value = WorkoutOverviewUiState.Workouts
        } else {
            _uiState.value = WorkoutOverviewUiState.NoWorkouts
        }
    }
}
