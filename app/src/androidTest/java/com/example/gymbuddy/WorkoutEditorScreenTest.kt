package com.example.gymbuddy

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.workouts.editor.SavingWorkoutState
import com.example.gymbuddy.ui.workouts.editor.WorkoutEditorScreen
import com.example.gymbuddy.ui.workouts.editor.WorkoutEditorViewModelContract
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WorkoutEditorScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            WorkoutEditorScreen(
                workoutEditorViewModel = FakeWorkoutEditorViewModel()
            )
        }
    }

    @Test
    fun workoutEditor_CorrectInitialUi() {
        composeTestRule
            .onNodeWithTag("screenTitle_Editor")
            .assertIsDisplayed()
            .assertTextContains("New Workout")

        composeTestRule
            .onNodeWithTag("workoutNameInput")
            .assertIsDisplayed()
            .assertTextContains("Workout Title")

        composeTestRule
            .onNodeWithTag("addExerciseButton")
            .assertIsDisplayed()
            .assertTextContains("Add Exercise")

        composeTestRule
            .onNodeWithTag("cancelButton")
            .assertIsDisplayed()
            .assertTextContains("Cancel")

        composeTestRule
            .onNodeWithTag("saveButton")
            .assertIsDisplayed()
            .assertTextContains("Save")
    }

    @Test
    fun workoutEditor_AddExercise() {
        composeTestRule
            .onNodeWithTag("addExerciseButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("addExerciseCard")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("exerciseNameInput")
            .assertIsDisplayed()
            .assertTextContains("Exercise Name")
    }

    @Test
    fun workoutEditor_CancelDialog() {
        composeTestRule
            .onNodeWithTag("cancelButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("confirmationDialog")
            .assertIsDisplayed()
    }

    @Test
    fun workoutEditor_SaveDialog() {
        composeTestRule
            .onNodeWithTag("saveButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("confirmationDialog")
            .assertIsDisplayed()
    }

    @Test
    fun workoutEditor_DeleteWorkout() {
        composeTestRule
            .onNodeWithTag("addExerciseButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("moreButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("deleteExerciseButton")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("exerciseNameInput")
            .assertIsNotDisplayed()
    }
}

class FakeWorkoutEditorViewModel : WorkoutEditorViewModelContract {
    override val saveState: State<SavingWorkoutState> =
        mutableStateOf<SavingWorkoutState>(SavingWorkoutState.Idle)
    override val workout: MutableState<Workout?> = mutableStateOf(null)

    init {
        workout.value =
            Workout(
                name = "Workout",
                exercises = mutableStateListOf()
            )
    }

    override fun addExercise(exercise: WorkoutExercise) {
        workout.value?.exercises?.add(exercise)
    }

    override fun updateExercise(
        index: Int,
        exercise: WorkoutExercise
    ) {
    }

    override fun removeExercise(index: Int) {
        workout.value?.exercises?.removeAt(index)
    }

    override fun updateWorkoutName(newName: String) {
    }

    override fun saveWorkout() {
    }
}
