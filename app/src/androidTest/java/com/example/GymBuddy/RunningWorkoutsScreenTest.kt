package com.example.GymBuddy

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.GymBuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.GymBuddy.data.localdatabase.Plan
import com.example.GymBuddy.ui.runningWorkout.ExerciseExecution
import com.example.GymBuddy.ui.runningWorkout.RunningWorkout
import com.example.GymBuddy.ui.runningWorkout.RunningWorkoutViewModelContract
import org.junit.Rule
import org.junit.Test

class RunningWorkoutsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun runningWorkout_displayNoExercisesIfWorkoutIsEmpty() {
        val workout = Plan(planName = "Running", id = 1)
        val exercises = emptyList<ExecutablePlanWithDetails>()

        setContent(workout, exercises)

        composeTestRule.onNodeWithText("Running").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("exerciseCard").assertCountEquals(0)
    }

    @Test
    fun runningWorkout_displayExercisesIfWorkoutIsNotEmpty() {
        val workout = Plan(planName = "Running", id = 1)
        val exercises = listOf(
            ExecutablePlanWithDetails(
                executablePlanId = 1,
                exerciseName = "Exercise 1",
                exerciseId = 1,
                planName = "Running",
                sets = 3,
                planId = 1,
                order = 0
            ),
            ExecutablePlanWithDetails(
                executablePlanId = 2,
                exerciseName = "Exercise 2",
                exerciseId = 2,
                planName = "Running",
                sets = 3,
                planId = 1,
                order = 1
            )
        )

        setContent(workout, exercises)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Running").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("exerciseCard").assertCountEquals(2)
    }

    @Test
    fun runningWorkout_SaveButtonTriggersSaveWorkout() {
        val workout = Plan(planName = "Running", id = 1)

        var saveWorkoutCalled = false

        setContent(workout, emptyList(), saveWorkout = { saveWorkoutCalled = true })

        composeTestRule.onNodeWithText("Save").performClick()

        assert(saveWorkoutCalled)
    }

    private fun setContent(
        workout: Plan,
        exercises: List<ExecutablePlanWithDetails>,
        saveWorkout: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            RunningWorkout(
                workoutId = "1",
                planListViewModel = FakeRunningWorkoutViewModel(
                    mockedExercises = exercises,
                    mockedWorkoutId = workout.planName
                ),
                saveWorkout = { saveWorkout() }
            )
        }
    }
}

class FakeRunningWorkoutViewModel(
    mockedExercises: List<ExecutablePlanWithDetails> = emptyList(),
    mockedWorkoutId: String = "1",
    mockedPlanName: String = "Running"
) : RunningWorkoutViewModelContract {
    override val exercises: List<ExecutablePlanWithDetails> = mockedExercises
    override val planName: MutableState<String> = mutableStateOf(mockedPlanName)

    private val executions = mutableListOf<ExerciseExecution>()

    override fun getExecutions(exerciseId: Long): List<ExerciseExecution> {
        return executions.filter { it.exerciseId == exerciseId }
    }

    override fun addOrUpdateExecution(execution: ExerciseExecution) {}

    override fun saveExecutionsToRepository() {}
}
