package com.example.gymbuddy

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.ui.old.runningWorkout.ExerciseExecution
import com.example.gymbuddy.ui.old.runningWorkout.RunningWorkout
import com.example.gymbuddy.ui.old.runningWorkout.RunningWorkoutViewModelContract
import org.junit.Rule
import org.junit.Test

class RunningWorkoutsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun runningWorkout_displayNoExercisesIfWorkoutIsEmpty() {
        val workout = WorkoutDetailsEntity(workoutName = "Running", id = 1)
        val exercises = emptyList<ExecutablePlanWithDetails>()

        setContent(workout, exercises)

        composeTestRule.onNodeWithText("Running").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("exerciseCard").assertCountEquals(0)
    }

    @Test
    fun runningWorkout_SaveButtonTriggersSaveWorkout() {
        val workout = WorkoutDetailsEntity(workoutName = "Running", id = 1)

        var saveWorkoutCalled = false

        setContent(workout, emptyList(), saveWorkout = { saveWorkoutCalled = true })

        composeTestRule.onNodeWithText("Save").performClick()

        assert(saveWorkoutCalled)
    }

    private fun setContent(
        workout: WorkoutDetailsEntity,
        exercises: List<ExecutablePlanWithDetails>,
        saveWorkout: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            RunningWorkout(
                workoutId = "1",
                planListViewModel =
                    FakeRunningWorkoutViewModel(
                        mockedExercises = exercises,
                    ),
                saveWorkout = { saveWorkout() },
            )
        }
    }
}

class FakeRunningWorkoutViewModel(
    mockedExercises: List<ExecutablePlanWithDetails> = emptyList(),
    mockedPlanName: String = "Running",
) : RunningWorkoutViewModelContract {
    override val exercises: List<ExecutablePlanWithDetails> = mockedExercises
    override val planName: MutableState<String> = mutableStateOf(mockedPlanName)

    private val executions = mutableListOf<ExerciseExecution>()

    override fun getExecutions(exerciseId: Long): List<ExerciseExecution> =
        executions.filter {
            it.exerciseId == exerciseId
        }

    override fun addOrUpdateExecution(execution: ExerciseExecution) {}

    override fun saveExecutionsToRepository() {}
}
