package com.example.gymbuddy

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.WorkoutSet
import com.example.gymbuddy.ui.workouts.executor.WorkoutExecutorViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutExecutorViewModelTest {
    private lateinit var viewModel: WorkoutExecutorViewModel
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

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
    fun workoutExecutorViewModel_initializeWithWorkout() =
        runTest {
            val workout = setupWithValidWorkout()

            advanceUntilIdle()

            assertEquals(workout.name, viewModel.workout.value?.name)
            assertEquals(workout.note, viewModel.workout.value?.note)
        }

    @Test
    @Ignore("I don't know yet what to do here")
    fun workoutExecutorViewModel_emptyWorkoutCausesSomething() =
        runTest {
            val workoutId = 1L
            coEvery { workoutRepository.getWorkout(workoutId) } returns null

            advanceUntilIdle()

            viewModel = WorkoutExecutorViewModel(workoutRepository, savedStateHandle)

            // assertEquals(null, viewModel.workout.value)
        }

    @Test
    fun workoutExecutorViewModel_addsSetsToExercises() =
        runTest {
            val exercise =
                WorkoutExercise(
                    name = "Test Exercise",
                    setCount = 0,
                    order = 0,
                    sets = mutableStateListOf()
                )
            setupWithValidWorkout(mutableStateListOf(exercise))

            advanceUntilIdle()

            viewModel.addSet(exercise)

            advanceUntilIdle()

            assertEquals("Setcount is higher", 1, exercise.setCount)
            assertEquals("Set is added", 1, exercise.sets.size)
        }

    @Test
    fun workoutExecutorViewModel_removesExerciseFromWorkout() =
        runTest {
            val exercise =
                WorkoutExercise(
                    name = "Test Exercise",
                    setCount = 1,
                    order = 0,
                    sets = mutableStateListOf()
                )
            setupWithValidWorkout(mutableStateListOf(exercise))

            advanceUntilIdle()

            viewModel.deleteExercise(0)

            advanceUntilIdle()

            assertEquals(
                "Exercise is removed",
                0,
                viewModel.workout.value
                    ?.exercises
                    ?.size
            )
        }

    @Test
    fun workoutExecutorViewModel_updatesSet() =
        runTest {
            val set = WorkoutSet(weight = 100f, reps = 10, order = 0)
            val exercise =
                WorkoutExercise(
                    name = "Test Exercise",
                    setCount = 1,
                    order = 0,
                    sets = mutableStateListOf(set)
                )
            setupWithValidWorkout(mutableStateListOf(exercise))

            val updatedSet = WorkoutSet(weight = 200f, reps = 20, order = 0)

            advanceUntilIdle()

            viewModel.updateSet(updatedSet, exerciseIndex = 0)

            advanceUntilIdle()

            assertEquals("Set is updated", updatedSet, exercise.sets[0])
        }

    @Test
    fun workoutExecutorViewModel_saveExecutions_callsRepository() =
        runTest {
            val exercise =
                WorkoutExercise(
                    name = "Test Exercise",
                    setCount = 1,
                    order = 0,
                    sets = mutableStateListOf()
                )
            setupWithValidWorkout(mutableStateListOf(exercise))

            advanceUntilIdle()

            viewModel.saveExecutions()

            advanceUntilIdle()

            coVerify {
                workoutRepository.saveWorkoutExecution(
                    viewModel.workout.value!!.exercises,
                    any()
                )
            }
        }

    @Test
    fun workoutExecutorViewModel_updateWorkout_callsRepository() =
        runTest {
            val workout = setupWithValidWorkout()

            advanceUntilIdle()

            viewModel.updateWorkout()

            advanceUntilIdle()

            coVerify { workoutRepository.updateWorkout(workout) }
        }

    private fun setupWithValidWorkout(
        exerciseList: SnapshotStateList<WorkoutExercise> = mutableStateListOf()
    ): Workout {
        val workout =
            Workout(
                id = 1,
                name = "Test Workout",
                note = "This is a test workout",
                exercises = exerciseList
            )
        val workoutId = "1"

        coEvery { workoutRepository.getWorkout(workoutId.toLong()) } returns workout
        every { savedStateHandle.get<String>("workoutId") } returns workoutId

        viewModel = WorkoutExecutorViewModel(workoutRepository, savedStateHandle)

        return workout
    }
}
