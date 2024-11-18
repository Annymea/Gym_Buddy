package com.example.gymbuddy.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewScreen
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewUiState
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewViewModelContract
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class ExerciseOverviewScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            ExerciseOverviewScreen(
                viewModel = FakeExerciseOverviewViewModel(),
            )
        }
    }

    @Test
    fun exerciseOverview_showsCorrectTitle() {
        composeTestRule.onNodeWithTag("exercise_overview_screen_title").assertIsDisplayed()
    }

    @Test
    fun exerciseOverview_showsNoExercisesMessage() {
        composeTestRule.onNodeWithTag("no_exercises_message").assertIsDisplayed()
    }

    @Test
    fun exerciseOverview_showsExerciseList() {
        composeTestRule.onNodeWithTag("exercise_list").assertIsDisplayed()
    }

    // TODO: Test needs hilt injections - maybe i read something about custom activities or something like this
    @Ignore("will be improved later")
    @Test
    fun exerciseOverview_opensAddExerciseDialog() {
        composeTestRule.onNodeWithTag("createExerciseButton").performClick()
        composeTestRule.onNodeWithTag("addExerciseDialog").assertIsDisplayed()
    }
}

class FakeExerciseOverviewViewModel : ExerciseOverviewViewModelContract {
    override val exercises: SnapshotStateList<WorkoutExercise>
        get() = mutableStateListOf()
    override val uiState: MutableStateFlow<ExerciseOverviewUiState>
        get() = MutableStateFlow(ExerciseOverviewUiState.NoExercises)

    override fun deleteExercise(exerciseId: Long) {
        TODO("Not yet implemented")
    }
}
