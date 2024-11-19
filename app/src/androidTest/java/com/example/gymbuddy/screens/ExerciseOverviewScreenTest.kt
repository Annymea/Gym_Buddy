package com.example.gymbuddy.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.HiltTestActivity
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewScreen
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewUiState
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewViewModelContract
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ExerciseOverviewScreenTest {
    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var viewModel: FakeExerciseOverviewViewModel

    @Before
    fun setUp() {
        hiltRule.inject()

        viewModel = FakeExerciseOverviewViewModel()

        composeTestRule.setContent {
            ExerciseOverviewScreen(
                viewModel = viewModel
            )
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun exerciseOverview_showsCorrectTitle() {
        composeTestRule.onNodeWithTag("exercise_overview_screen_title").assertIsDisplayed()
    }

    @Test
    fun exerciseOverview_showsNoExercisesMessage() {
        composeTestRule.onNodeWithTag("noExercisesText").assertIsDisplayed()
    }

    @Test
    fun exerciseOverview_showsExerciseList() {
        viewModel.uiState.value = ExerciseOverviewUiState.Exercises
        viewModel.exercises = mutableStateListOf(WorkoutExercise(1, "test", "test", order = 0))
        composeTestRule.onNodeWithTag("exerciseList").assertIsDisplayed()
    }

    @Test
    fun exerciseOverview_opensAndClosesAddExerciseDialog() {
        composeTestRule.onNodeWithTag("createExerciseButton").performClick()
        composeTestRule.onNodeWithTag("addExerciseDialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("cancelExerciseButton").performClick()
        composeTestRule.onNodeWithTag("addExerciseDialog").assertIsNotDisplayed()
    }

    @Test
    fun exerciseOverview_showsCorrectExercises() {
        val listOfExercises =
            mutableStateListOf(
                WorkoutExercise(1, "test", "test", order = 0),
                WorkoutExercise(2, "test2", "test2", order = 1)
            )

        viewModel.exercises = listOfExercises
        viewModel.uiState.value = ExerciseOverviewUiState.Exercises

        composeTestRule.onNodeWithTag("exerciseList").assertIsDisplayed()
        composeTestRule.onNodeWithTag("exerciseList").onChildren().assertCountEquals(2)
    }

    @Test
    fun exerciseOverview_callsDeleteExerciseForCorrectExercise() {
        val listOfExercises =
            mutableStateListOf(
                WorkoutExercise(1, "test1", "test1", order = 0),
                WorkoutExercise(2, "test2", "test2", order = 1)
            )
        viewModel.exercises = listOfExercises
        viewModel.uiState.value = ExerciseOverviewUiState.Exercises

        composeTestRule.onNodeWithTag("exerciseOptionsButton2").performClick()

        composeTestRule.onNodeWithTag("deleteExerciseMenuItem").performClick()

        assert(viewModel.exercises.size == 1)
        assert(viewModel.exercises[0].id == 1L)
    }

    @Test
    fun exerciseOverview_showsNewExercisesAfterAdding() {
        val listOfExercises =
            mutableStateListOf(
                WorkoutExercise(1, "test1", "test1", order = 0),
                WorkoutExercise(2, "test2", "test2", order = 1)
            )
        viewModel.exercises = listOfExercises
        viewModel.uiState.value = ExerciseOverviewUiState.Exercises

        composeTestRule.onNodeWithTag("exerciseList").assertIsDisplayed()
        composeTestRule.onNodeWithTag("exerciseList").onChildren().assertCountEquals(2)

        listOfExercises.add(WorkoutExercise(3, "test3", "test3", order = 0))
        viewModel.exercises = listOfExercises
        viewModel.uiState.value = ExerciseOverviewUiState.Exercises

        composeTestRule.onNodeWithTag("exerciseList").assertIsDisplayed()
        composeTestRule.onNodeWithTag("exerciseList").onChildren().assertCountEquals(3)
    }

    @Test
    fun exerciseOverview_showsSateChangeToNoExercises() {
        val listOfExercises =
            mutableStateListOf(
                WorkoutExercise(1, "test1", "test1", order = 0)
            )
        viewModel.exercises = listOfExercises
        viewModel.uiState.value = ExerciseOverviewUiState.Exercises

        composeTestRule.onNodeWithTag("exerciseOptionsButton1").performClick()

        composeTestRule.onNodeWithTag("deleteExerciseMenuItem").performClick()

        assert(viewModel.exercises.size == 0)
        assert(viewModel.uiState.value == ExerciseOverviewUiState.NoExercises)
        composeTestRule.onNodeWithTag("noExercisesText").assertIsDisplayed()
    }
}

class FakeExerciseOverviewViewModel : ExerciseOverviewViewModelContract {
    override var exercises: SnapshotStateList<WorkoutExercise> = mutableStateListOf()
    override var uiState: MutableStateFlow<ExerciseOverviewUiState> =
        MutableStateFlow(ExerciseOverviewUiState.NoExercises)

    override fun deleteExercise(exerciseId: Long) {
        exercises.removeIf {
            it.id == exerciseId
        }
        if (exercises.size == 0) {
            uiState.value = ExerciseOverviewUiState.NoExercises
        }
    }
}
