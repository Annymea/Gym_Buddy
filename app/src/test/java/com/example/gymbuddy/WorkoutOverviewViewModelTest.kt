package com.example.gymbuddy

import android.util.Log
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewUiState
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
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
            val workouts = listOf(
                Workout(name = "Workout1"),
                Workout(name = "Workout2")
            )
            val updatedWorkouts = listOf(
                Workout(name = "Workout3"),
                Workout(name = "Workout4")
            )

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

    @Test
    fun onReorder_updatesWorkoutListCorrectly() = runTest {
        val initialWorkouts = listOf(
            Workout(id = 1, name = "Workout1"),
            Workout(id = 2, name = "Workout2"),
            Workout(id = 3, name = "Workout3")
        )
        val reorderedWorkouts = listOf(
            Workout(id = 2, name = "Workout2"),
            Workout(id = 3, name = "Workout3"),
            Workout(id = 1, name = "Workout1")
        )

        coEvery { workoutRepository.getAllWorkoutDetails() } returns flow { emit(initialWorkouts) }
        coEvery { workoutRepository.updateWorkoutOrder(any()) } returns Unit

        viewModel = WorkoutOverviewViewModel(workoutRepository)

        advanceUntilIdle()

        viewModel.onReorder(reorderedWorkouts)

        assertEquals(
            "Workouts have correct length after reorder",
            3,
            viewModel.workouts.size
        )
        assertEquals(
            "First workout is correct after reorder",
            "Workout2",
            viewModel.workouts[0].name
        )
        assertEquals(
            "Second workout is correct after reorder",
            "Workout3",
            viewModel.workouts[1].name
        )
        assertEquals(
            "Third workout is correct after reorder",
            "Workout1",
            viewModel.workouts[2].name
        )
    }

    @Test
    fun onReorder_callsRepositoryWithCorrectIds() = runTest {
        val initialWorkouts = listOf(
            Workout(id = 1, name = "Workout1"),
            Workout(id = 2, name = "Workout2"),
            Workout(id = 3, name = "Workout3")
        )
        val reorderedWorkouts = listOf(
            Workout(id = 2, name = "Workout2"),
            Workout(id = 3, name = "Workout3"),
            Workout(id = 1, name = "Workout1")
        )
        val expectedIds = listOf(2L, 3L, 1L)

        coEvery { workoutRepository.getAllWorkoutDetails() } returns flow { emit(initialWorkouts) }
        coEvery { workoutRepository.updateWorkoutOrder(any()) } returns Unit

        viewModel = WorkoutOverviewViewModel(workoutRepository)

        advanceUntilIdle()

        viewModel.onReorder(reorderedWorkouts)

        advanceUntilIdle()

        coVerify {
            workoutRepository.updateWorkoutOrder(expectedIds)
        }
    }

    @Test
    fun onReorder_handlesEmptyList() = runTest {
        val initialWorkouts = listOf(
            Workout(id = 1, name = "Workout1"),
            Workout(id = 2, name = "Workout2"),
            Workout(id = 3, name = "Workout3")
        )
        val reorderedWorkouts = emptyList<Workout>()

        coEvery { workoutRepository.getAllWorkoutDetails() } returns flow { emit(initialWorkouts) }
        coEvery { workoutRepository.updateWorkoutOrder(any()) } returns Unit

        viewModel = WorkoutOverviewViewModel(workoutRepository)

        advanceUntilIdle()

        viewModel.onReorder(reorderedWorkouts)

        advanceUntilIdle()

        assertEquals("Workouts are empty after reorder with empty list", 0, viewModel.workouts.size)

        coVerify {
            workoutRepository.updateWorkoutOrder(emptyList())
        }
    }

    @Test
    fun onDeleteWorkout_callsRepositoryToDelete() = runTest {
        val workoutIdToDelete = 1L
        coEvery { workoutRepository.deleteWorkout(workoutIdToDelete) } returns Unit
        val initialWorkouts = listOf(
            Workout(id = 1, name = "Workout1"),
            Workout(id = 2, name = "Workout2")
        )
        coEvery { workoutRepository.getAllWorkoutDetails() } returns flow { emit(initialWorkouts) }

        viewModel = WorkoutOverviewViewModel(workoutRepository)

        advanceUntilIdle()

        viewModel.onDeleteWorkout(workoutIdToDelete)

        advanceUntilIdle()

        coVerify { workoutRepository.deleteWorkout(workoutIdToDelete) }
    }

    @Test
    fun onDeleteWorkout_updatesWorkoutsList() = runTest {
        val workoutIdToDelete = 1L
        val initialWorkouts = listOf(
            Workout(id = 1, name = "Workout1"),
            Workout(id = 2, name = "Workout2")
        )
        val remainingWorkouts = listOf(
            Workout(id = 2, name = "Workout2")
        )

        coEvery { workoutRepository.getAllWorkoutDetails() } returns flow {
            emit(initialWorkouts)
            emit(remainingWorkouts)
        }
        coEvery { workoutRepository.deleteWorkout(workoutIdToDelete) } returns Unit

        viewModel = WorkoutOverviewViewModel(workoutRepository)

        advanceUntilIdle()

        viewModel.onDeleteWorkout(workoutIdToDelete)

        advanceUntilIdle()

        assertEquals(
            "Workouts list is updated after deletion",
            remainingWorkouts.size,
            viewModel.workouts.size
        )
        assertEquals(
            "Remaining workout is correct",
            "Workout2",
            viewModel.workouts[0].name
        )
    }
}
