package com.example.gymbuddy

import android.util.Log
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewUiState
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExerciseOverviewViewModelTest {
    private lateinit var viewModel: ExerciseOverviewViewModel
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var mockRepository: WorkoutRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun exerciseOverview_initialStateIsNoExercisesWithEmptyList() =
        runTest {
            coEvery { mockRepository.getAllExercises() } returns flowOf(emptyList())

            viewModel = ExerciseOverviewViewModel(mockRepository)
            testDispatcher.scheduler.advanceUntilIdle()

            assert(
                viewModel.uiState.value is ExerciseOverviewUiState.NoExercises
            ) {
                "Expected UI state to be NoExercises " +
                    "when the repository returns an empty list"
            }
            assert(
                viewModel.exercises.isEmpty()
            ) {
                "Expected exercises list to be empty, " +
                    "but found ${viewModel.exercises}"
            }
        }

    @Test
    fun exerciseOverview_initialStateIsExercisesWithNonEmptyList() =
        runTest {
            val exerciseList =
                listOf(
                    WorkoutExercise(id = 1, name = "Push-up"),
                    WorkoutExercise(id = 2, name = "Squat")
                )
            coEvery { mockRepository.getAllExercises() } returns flowOf(exerciseList)

            viewModel = ExerciseOverviewViewModel(mockRepository)
            testDispatcher.scheduler.advanceUntilIdle()

            assert(
                viewModel.uiState.value is ExerciseOverviewUiState.Exercises
            ) {
                "Expected UI state to be Exercises, " +
                    "but found ${viewModel.uiState.value}"
            }

            assert(
                viewModel.exercises.size == 2
            ) {
                "Expected exercise length to match 2, " +
                    "but found ${viewModel.exercises.size}"
            }

            assert(
                viewModel.exercises[0].name == "Push-up"
            ) {
                "Expected first exercise name to be Push-up, " +
                    "but found ${viewModel.exercises[0].name}"
            }
        }

    @Test
    fun exerciseOverview_errorClearsExercisesAndSetStateNoExercises() =
        runTest {
            coEvery { mockRepository.getAllExercises() } returns
                flow {
                    throw Exception("Error fetching exercises")
                }

            viewModel = ExerciseOverviewViewModel(mockRepository)
            testDispatcher.scheduler.advanceUntilIdle()

            assert(
                viewModel.uiState.value is ExerciseOverviewUiState.NoExercises
            ) {
                "Expected UI state to be NoExercises after an error, " +
                    "but found ${viewModel.uiState.value}"
            }

            assert(
                viewModel.exercises.isEmpty()
            ) {
                "Expected exercises list to be empty after an error, " +
                    "but found ${viewModel.exercises}"
            }
        }

    @Test
    fun exerciseOverview_removesExercise() =
        runTest {
            val exerciseList =
                mutableListOf(
                    WorkoutExercise(1, name = "Push-up"),
                    WorkoutExercise(2, name = "Squat")
                )
            coEvery { mockRepository.getAllExercises() } returns flowOf(exerciseList)
            coJustRun { mockRepository.deleteExercise(any()) }

            viewModel = ExerciseOverviewViewModel(mockRepository)
            testDispatcher.scheduler.advanceUntilIdle()

            assert(
                viewModel.exercises.size == 2
            ) {
                "Expected initial exercises size to be 2, " +
                    "but found ${viewModel.exercises.size}"
            }

            viewModel.deleteExercise(1)
            testDispatcher.scheduler.advanceUntilIdle()

            coVerify { mockRepository.deleteExercise(1) }
            assert(
                viewModel.exercises.size == 2
            ) {
                "Expected exercises size to remain 2, " +
                    "as delete does not affect UI state directly"
            }
        }

    @Test
    fun exerciseOverview_handlesDeleteError() =
        runTest {
            val exerciseList =
                listOf(
                    WorkoutExercise(1, name = "Push-up"),
                    WorkoutExercise(2, name = "Squat")
                )
            coEvery { mockRepository.getAllExercises() } returns flowOf(exerciseList)
            coEvery { mockRepository.deleteExercise(any()) } throws
                Exception("Error deleting exercise")

            viewModel = ExerciseOverviewViewModel(mockRepository)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.deleteExercise(1)
            testDispatcher.scheduler.advanceUntilIdle()

            coVerify { mockRepository.deleteExercise(1) }
            assert(
                viewModel.exercises.size == 2
            ) {
                "Expected exercise length to still equal 2, " +
                    "after delete error, but found ${viewModel.exercises.size}"
            }
        }
}
