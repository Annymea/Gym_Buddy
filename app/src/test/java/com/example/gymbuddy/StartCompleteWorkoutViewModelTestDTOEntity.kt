package com.example.gymbuddy

import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.ui.old.startWorkout.StartWorkoutViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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
class StartCompleteWorkoutViewModelTestDTOEntity {
    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var viewModel: StartWorkoutViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        workoutRepository = mockk()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialize list of workouts`() =
        runTest {
            val workouts =
                listOf(
                    WorkoutDetailsEntity(id = 1, workoutName = "Workout 1"),
                    WorkoutDetailsEntity(id = 2, workoutName = "Workout 2"),
                    WorkoutDetailsEntity(id = 3, workoutName = "Workout 3"),
                )
            coEvery { workoutRepository.getAllPlanNames() } returns flow { emit(workouts) }

            viewModel = StartWorkoutViewModel(workoutRepository)

            advanceUntilIdle()

            assertEquals(workouts.size, viewModel.workouts.size)
            assertEquals(workouts, viewModel.workouts)
        }

    @Test
    fun `initialize list of workouts with empty list`() =
        runTest {
            coEvery { workoutRepository.getAllPlanNames() } returns flow { emit(emptyList()) }

            viewModel = StartWorkoutViewModel(workoutRepository)

            advanceUntilIdle()

            assertEquals(0, viewModel.workouts.size)
        }

    @Test
    fun `updates workouts if workoutRepository changes`() =
        runTest {
            val initialWorkouts =
                listOf(
                    WorkoutDetailsEntity(id = 1, workoutName = "Workout 1"),
                    WorkoutDetailsEntity(id = 2, workoutName = "Workout 2"),
                    WorkoutDetailsEntity(id = 3, workoutName = "Workout 3"),
                )
            val updatedWorkouts =
                listOf(
                    WorkoutDetailsEntity(id = 1, workoutName = "Workout 1"),
                    WorkoutDetailsEntity(id = 2, workoutName = "Workout 2"),
                )

            val workoutFlow =
                flow {
                    emit(initialWorkouts)
                    emit(updatedWorkouts)
                }

            coEvery { workoutRepository.getAllPlanNames() } returns workoutFlow

            viewModel = StartWorkoutViewModel(workoutRepository)

            advanceUntilIdle()

            assertEquals(updatedWorkouts.size, viewModel.workouts.size)
            assertEquals(updatedWorkouts, viewModel.workouts)
        }
}
