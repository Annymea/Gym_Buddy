package com.example.gymbuddy

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.common.addExerciseDialog.AddExerciseDialog
import com.example.gymbuddy.ui.common.addExerciseDialog.AddExerciseDialogViewModelContract
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddExerciseDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeViewModel: FakeAddExerciseViewModel

    @Before
    fun setUp() {
        fakeViewModel = FakeAddExerciseViewModel()

        composeTestRule.setContent {
            AddExerciseDialog(
                viewModel = fakeViewModel,
                onDismissRequest = { fakeViewModel.onDismissCalled = true }
            )
        }
    }

    @Test
    fun addExerciseDialog_noInitialExerciseName() {
        composeTestRule.onNodeWithTag("nameInput").assertIsNotFocused()
        composeTestRule.onNodeWithTag("saveExerciseButton").assertIsNotEnabled()
    }

    @Test
    fun addExerciseDialog_onSaveRequestIsCalledWithCorrectExerciseName() {
        composeTestRule.onNodeWithTag("nameInput").performTextInput("testExercise")
        composeTestRule.onNodeWithTag("saveExerciseButton").assertIsEnabled()
        composeTestRule.onNodeWithTag("saveExerciseButton").performClick()

        assert(fakeViewModel.exercise?.name == "testExercise")
    }

    @Test
    fun addExerciseDialog_onDismissRequestIsCalled() {
        composeTestRule.onNodeWithTag("cancelExerciseButton").performClick()

        assert(fakeViewModel.onDismissCalled)
    }
}

class FakeAddExerciseViewModel : AddExerciseDialogViewModelContract {
    var onDismissCalled = false

    var exercise: WorkoutExercise? = null

    override fun onSaveExercise(newExercise: WorkoutExercise) {
        exercise = newExercise
    }
}
