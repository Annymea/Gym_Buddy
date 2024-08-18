package com.example.GymBuddy

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.GymBuddy.ui.createPlan.CreatePlan
import com.example.GymBuddy.ui.createPlan.CreatePlanViewModelContract
import com.example.GymBuddy.ui.createPlan.SavingPlanState
import com.example.GymBuddy.ui.createPlan.ViewModelExercise
import org.junit.Rule
import org.junit.Test

class CreatePlanScreenTest<CreatePlanViewModel> {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createPlan_displaysUIElements() {
        val fakeViewModel = FakeCreatePlanViewModel()

        composeTestRule.setContent {
            CreatePlan(
                createPlanViewModel = fakeViewModel,
                onPlanSaved = {}
            )
        }

        composeTestRule.onNodeWithText("Plan name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save Plan").assertIsDisplayed()
        composeTestRule.onNodeWithText("Neue Übung").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dein Plan").assertIsDisplayed()
    }

    @Test
    fun createPlan_addExerciseToList() {
        val fakeViewModel = FakeCreatePlanViewModel()
        fakeViewModel.updateExercise(ViewModelExercise("Push Up", 3))

        composeTestRule.setContent {
            CreatePlan(
                createPlanViewModel = fakeViewModel,
                onPlanSaved = {}
            )
        }
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithText("Push Up").assertIsDisplayed()
    }

    @Test
    fun createPlan_savePlanWhenValid() {
        val fakeViewModel = FakeCreatePlanViewModel()
        fakeViewModel.updatePlanName("My Workout Plan")

        var isPlanSaved = false

        composeTestRule.setContent {
            CreatePlan(
                createPlanViewModel = fakeViewModel,
                onPlanSaved = { isPlanSaved = true }
            )
        }
        composeTestRule.onNodeWithText("Save Plan").performClick()
        composeTestRule.waitForIdle()

        assert(isPlanSaved)
    }

    @Test
    fun createPlan_displaysErrorStateWhenNoPlanName() {
        val fakeViewModel = FakeCreatePlanViewModel()

        composeTestRule.setContent {
            CreatePlan(
                createPlanViewModel = fakeViewModel,
                onPlanSaved = {}
            )
        }

        composeTestRule.onNodeWithText("Save Plan").performClick()

        assert(fakeViewModel.saveState.value is SavingPlanState.Error)
    }
}

class FakeCreatePlanViewModel : CreatePlanViewModelContract {
    override val exerciseListToBeSaved: SnapshotStateList<ViewModelExercise> = mutableStateListOf()
    override val exerciseToAdd: MutableState<ViewModelExercise> =
        mutableStateOf(ViewModelExercise(name = "", sets = 0))
    override val planName: MutableState<String> = mutableStateOf("")
    override val saveState: MutableState<SavingPlanState> = mutableStateOf(SavingPlanState.Idle)

    override fun resetErrorState() {
        saveState.value = SavingPlanState.Idle
    }

    override fun addExercise(exercise: ViewModelExercise) {
        exerciseListToBeSaved.add(exercise)
    }

    override fun updateExercise(updatedExercise: ViewModelExercise) {
        exerciseToAdd.value = updatedExercise
    }

    override fun updatePlanName(newPlanName: String) {
        planName.value = newPlanName
    }

    override fun savePlanToDatabase() {
        if (planName.value.isEmpty()) {
            saveState.value = SavingPlanState.Error("Plan name cannot be empty")
        } else {
            saveState.value = SavingPlanState.Saved
        }
    }
}
