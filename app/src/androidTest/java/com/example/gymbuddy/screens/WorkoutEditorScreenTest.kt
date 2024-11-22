package com.example.gymbuddy.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.HiltTestActivity
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.workouts.editor.SavingWorkoutState
import com.example.gymbuddy.ui.workouts.editor.WorkoutEditorScreen
import com.example.gymbuddy.ui.workouts.editor.WorkoutEditorViewModelContract
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class WorkoutEditorScreenTest {
    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var viewModel: FakeWorkoutEditorViewModel

    @Before
    fun setUp() {
        hiltRule.inject()

        viewModel = FakeWorkoutEditorViewModel()

        composeTestRule.setContent {
            WorkoutEditorScreen(
                workoutEditorViewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()
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
    fun workoutEditor_AddExercises() {
        composeTestRule
            .onNodeWithTag("addExerciseButton")
            .performClick()

        composeTestRule
            .onAllNodesWithTag("addExerciseListItem")
            .onFirst()
            .performClick()

        composeTestRule
            .onNodeWithTag("addDialogSubmitButton")
            .performClick()

        composeTestRule.onNodeWithTag("addExerciseDialog").isNotDisplayed()

        composeTestRule.onAllNodesWithTag("addExerciseCard").assertCountEquals(1)
    }

    @Test
    fun workoutEditor_AddExerciseDialogShowsCorrectExercises() {
        composeTestRule
            .onNodeWithTag("addExerciseButton")
            .performClick()

        composeTestRule
            .onAllNodesWithTag("addExerciseListItem")
            .assertCountEquals(3)

        composeTestRule
            .onAllNodesWithTag("addExerciseListItem")[0]
            .assertTextContains("Exercise 1")

        composeTestRule
            .onAllNodesWithTag("addExerciseListItem")[1]
            .assertTextContains("Exercise 2")

        composeTestRule
            .onAllNodesWithTag("addExerciseListItem")[2]
            .assertTextContains("Exercise 3")
    }

    @Test
    fun workoutEditor_AddExerciseDialogCanAddExercises() {
        composeTestRule
            .onNodeWithTag("addExerciseButton")
            .performClick()

        composeTestRule
            .onAllNodesWithTag("addNewExerciseButton")
            .onFirst()
            .performClick()

        composeTestRule
            .onNodeWithTag("addExerciseDialog")
            .assertIsDisplayed()
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
    fun workoutEditor_DeleteExercise() {
        viewModel.workout.value?.exercises?.add(
            WorkoutExercise(id = 1, name = "Exercise", order = 0)
        )

        composeTestRule
            .onNodeWithTag("moreButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("deleteExerciseButton")
            .performClick()

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

    override fun addAllSelectedExercises(exercises: List<WorkoutExercise>) {
        workout.value?.exercises?.addAll(exercises)
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

    override fun getExistingExercises(): List<WorkoutExercise> {
        val exercises =
            mutableListOf(
                WorkoutExercise(
                    id = 0,
                    name = "Exercise 1",
                    order = 0
                ),
                WorkoutExercise(
                    id = 1,
                    name = "Exercise 2",
                    order = 1
                ),
                WorkoutExercise(
                    id = 2,
                    name = "Exercise 3",
                    order = 2
                )
            )

        return exercises
    }
}
