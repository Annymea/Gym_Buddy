package com.example.gymbuddy

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutSet
import com.example.gymbuddy.ui.workouts.executor.WorkoutExecutorScreen
import com.example.gymbuddy.ui.workouts.executor.WorkoutExecutorViewModelContract
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WorkoutExecutorScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        val workoutId = "1"

        val testWorkout =
            Workout(
                name = "Test Workout",
                exercises =
                mutableStateListOf(
                    WorkoutExercise(
                        name = "Test Exercise",
                        order = 0,
                        setCount = 3,
                        sets =
                        mutableStateListOf(
                            WorkoutSet(
                                order = 0,
                                reps = 10,
                                weight = 50F
                            ),
                            WorkoutSet(
                                order = 1,
                                reps = 10,
                                weight = 50F
                            ),
                            WorkoutSet(
                                order = 2,
                                reps = 10,
                                weight = 50F
                            )
                        )
                    )
                )
            )

        composeTestRule.setContent {
            WorkoutExecutorScreen(
                workoutId = workoutId,
                viewModel = FakeWorkoutExecutorViewModel(testWorkout)
            )
        }
    }

    @Test
    fun workoutExecutor_initialUiIsCorrect() {
        composeTestRule
            .onNodeWithTag("screenTitle_Executor")
            .assertIsDisplayed()
            .assertTextContains("Test Workout")

        composeTestRule
            .onNodeWithTag("exerciseCard")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("exerciseName")
            .assertTextContains("Test Exercise")

        composeTestRule
            .onAllNodesWithTag("executionRow")
            .assertCountEquals(3)

        composeTestRule
            .onNodeWithTag("addSetButton")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("cancelButton")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("finishButton")
            .assertIsDisplayed()
    }

    @Test
    fun workoutExecutor_deleteExercise() {
        composeTestRule
            .onNodeWithTag("moreVertButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("deleteExerciseMenuItem")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("exerciseCard")
            .assertIsNotDisplayed()
    }

    @Test
    fun workoutExecutor_addSet() {
        composeTestRule
            .onNodeWithTag("addSetButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onAllNodesWithTag("executionRow")
            .assertCountEquals(4)
    }

    @Test
    fun workoutExecutor_cancelDialog() {
        composeTestRule
            .onNodeWithTag("cancelButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("confirmationDialog")
            .assertIsDisplayed()
    }

    @Test
    fun workoutExecutor_saveDialog() {
        composeTestRule
            .onNodeWithTag("finishButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("finishWorkoutDialog")
            .assertIsDisplayed()
    }
}

class FakeWorkoutExecutorViewModel(
    testWorkout: Workout
) : WorkoutExecutorViewModelContract {
    override val workout: MutableState<Workout?> = mutableStateOf(testWorkout)

    override fun saveExecutions() {
    }

    override fun updateWorkout() {
    }

    override fun addSet(exercise: WorkoutExercise) {
        exercise.sets.add(
            WorkoutSet(
                order = exercise.sets.size + 1,
                reps = 0,
                weight = 0f
            )
        )
    }

    override fun updateSet(
        set: WorkoutSet,
        exerciseIndex: Int
    ) {
    }

    override fun deleteExercise(exerciseIndex: Int) {
        workout.value?.exercises?.removeAt(exerciseIndex)
    }
}
