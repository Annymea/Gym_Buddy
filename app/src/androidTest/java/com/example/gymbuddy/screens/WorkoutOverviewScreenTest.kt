package com.example.gymbuddy.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
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

    @Test
    fun workoutOverview_showsWorkoutsInCorrectOrder() {
        val workouts =
            listOf(
                Workout(name = "Workout 2"),
                Workout(name = "Workout 1")
            )

        composeTestRule.setContent {
            WorkoutOverviewScreen(
                viewModel = FakeWorkoutOverviewViewModel(workouts),
                onExecuteWorkout = { }
            )
        }

        composeTestRule.onNodeWithTag("draggableLazyColumn")
            .onChildren().onFirst()
            .onChildAt(0) // place where workoutcard has its title
            .assertTextContains("Workout 2")
    }

    @Test
    fun workoutOverview_dragAndDropChangesOrder() {
        val workouts = mutableStateListOf(
            Workout(name = "Workout 1"),
            Workout(name = "Workout 2"),
            Workout(name = "Workout 3")
        )

        composeTestRule.setContent {
            WorkoutOverviewScreen(
                viewModel = FakeWorkoutOverviewViewModel(workouts),
                onExecuteWorkout = { }
            )
        }

        composeTestRule.onNodeWithText("Workout 1")
            .performTouchInput {
                down(0, center)
                advanceEventTime(500)
                // simulates a realistic use of the drag and drop
                repeat(10) {
                    moveBy(Offset(0f, 50f))
                    advanceEventTime(100)
                }
                up()
            }

        composeTestRule.onNodeWithTag("draggableLazyColumn")
            .onChildren().apply {
                get(0)
                    .onChildAt(0)
                    .assertTextContains("Workout 2")
                get(1)
                    .onChildAt(0)
                    .assertTextContains("Workout 3")
                get(2)
                    .onChildAt(0)
                    .assertTextContains("Workout 1")
            }
    }
}

class FakeWorkoutOverviewViewModel(
    override var workouts: List<Workout>
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

    override fun onReorder(newWorkouts: List<Workout>) {
        workouts = newWorkouts.toList()
    }

    override fun onDeleteWorkout(workoutId: Long) {
        val workoutToRemove = workouts.find { it.id == workoutId }
        if (workoutToRemove != null) {
            workouts = workouts.filter { it.id != workoutId }
        }
    }
}
