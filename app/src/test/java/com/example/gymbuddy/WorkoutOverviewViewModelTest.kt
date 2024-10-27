package com.example.gymbuddy

import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewUiState
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutOverviewViewModelTest {
    private lateinit var viewModel: WorkoutOverviewViewModel
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun workoutOverviewViewModel_returnsListOfWorkouts() =
        runTest {
            val workouts = listOf(Workout(name = "Workout1"), Workout(name = "Workout2"))
            coEvery { workoutRepository.getAllWorkoutDetails() } returns flow { emit(workouts) }

            viewModel = WorkoutOverviewViewModel(workoutRepository)

            advanceUntilIdle()

            assertEquals(
                "ViewModel is in correct state",
                WorkoutOverviewUiState.Workouts,
                viewModel.uiState.value
            )

            assertEquals("Workouts have correct length", 2, viewModel.workouts.size)
            assertEquals("Workouts have correct name", "Workout1", viewModel.workouts[0].name)
            assertEquals("Workouts have correct name", "Workout2", viewModel.workouts[1].name)
        }

    @Test
    fun workoutOverviewViewModel_returnsEmptyList() =
        runTest {
            val workout = mutableListOf<Workout>()
            coEvery { workoutRepository.getAllWorkoutDetails() } returns flow { emit(workout) }

            viewModel = WorkoutOverviewViewModel(workoutRepository)

            advanceUntilIdle()

            assertEquals(
                "ViewModel is in correct state",
                WorkoutOverviewUiState.NoWorkouts,
                viewModel.uiState.value
            )

            assertEquals("Workouts have correct length", 0, viewModel.workouts.size)
        }

    @Test
    fun workoutOverviewViewModel_updatesNewData() =
        runTest {
            val workouts = listOf(Workout(name = "Workout1"), Workout(name = "Workout2"))
            val updatedWorkouts = listOf(Workout(name = "Workout3"), Workout(name = "Workout4"))

            coEvery { workoutRepository.getAllWorkoutDetails() } returns
                flow {
                    emit(workouts)
                    emit(updatedWorkouts)
                }

            viewModel = WorkoutOverviewViewModel(workoutRepository)

            advanceUntilIdle()

            assertEquals(
                "ViewModel is in correct state",
                WorkoutOverviewUiState.Workouts,
                viewModel.uiState.value
            )

            assertEquals("Workouts have correct length", 2, viewModel.workouts.size)
            assertEquals("Workouts have correct name", "Workout3", viewModel.workouts[0].name)
            assertEquals("Workouts have correct name", "Workout4", viewModel.workouts[1].name)
        }
}
