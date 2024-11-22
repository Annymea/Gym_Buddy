package com.example.gymbuddy

import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.ui.workouts.editor.SavingWorkoutState
import com.example.gymbuddy.ui.workouts.editor.WorkoutEditorViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
class WorkoutEditorViewModelTest {
    private lateinit var viewModel: WorkoutEditorViewModel
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = WorkoutEditorViewModel(workoutRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun workoutEditorViewModel_initializesWithWorkoutTemplate() {
        assertEquals("Workout", viewModel.workout.value?.name)
    }

    @Test
    fun workoutEditorViewModel_addsExercisesToWorkout() =
        runTest {
            val exercises =
                listOf(
                    WorkoutExercise(
                        name = "Test Exercise",
                        setCount = 0,
                        order = 0,
                        sets = mutableStateListOf()
                    ),
                    WorkoutExercise(
                        name = "Test Exercise 2",
                        setCount = 0,
                        order = 1,
                        sets = mutableStateListOf()
                    )
                )

            viewModel.addAllSelectedExercises(exercises)

            advanceUntilIdle()

            assertEquals(
                2,
                viewModel.workout.value
                    ?.exercises
                    ?.size
            )
            assertEquals(
                "Test Exercise",
                viewModel.workout.value
                    ?.exercises
                    ?.get(0)
                    ?.name
            )

            assertEquals(
                "Test Exercise 2",
                viewModel.workout.value
                    ?.exercises
                    ?.get(1)
                    ?.name
            )
        }

    @Test
    fun workoutEditorViewModel_updatesExerciseInWorkout() =
        runTest {
            val exercises =
                listOf(
                    WorkoutExercise(
                        name = "Test Exercise",
                        setCount = 0,
                        order = 0,
                        sets = mutableStateListOf()
                    ),
                    WorkoutExercise(
                        name = "Test Exercise 2",
                        setCount = 0,
                        order = 1,
                        sets = mutableStateListOf()
                    )
                )

            viewModel.addAllSelectedExercises(exercises)

            val updatedExercise =
                WorkoutExercise(
                    name = "Updated Exercise",
                    setCount = 1,
                    order = 0,
                    sets = mutableStateListOf()
                )
            viewModel.updateExercise(0, updatedExercise)

            advanceUntilIdle()

            assertEquals(
                "Updated Exercise",
                viewModel.workout.value
                    ?.exercises
                    ?.get(0)
                    ?.name
            )

            assertEquals(
                2,
                viewModel.workout.value
                    ?.exercises
                    ?.size
            )
        }

    @Test
    fun workoutEditorViewModel_removesExerciseFromWorkout() =
        runTest {
            val exercises =
                listOf(
                    WorkoutExercise(
                        name = "Test Exercise",
                        setCount = 0,
                        order = 0,
                        sets = mutableStateListOf()
                    ),
                    WorkoutExercise(
                        name = "Test Exercise 2",
                        setCount = 0,
                        order = 1,
                        sets = mutableStateListOf()
                    )
                )

            viewModel.addAllSelectedExercises(exercises)

            viewModel.removeExercise(0)

            advanceUntilIdle()

            assertEquals(
                1,
                viewModel.workout.value
                    ?.exercises
                    ?.size
            )
        }

    @Test
    fun workoutEditorViewModel_updatesWorkoutName() =
        runTest {
            val newName = "Updated Workout Name"

            viewModel.updateWorkoutName(newName)

            advanceUntilIdle()

            assertEquals(newName, viewModel.workout.value?.name)
        }

    @Test
    fun workoutEditorViewModel_callsRepositoryAndUpdatesState() =
        runTest {
            coEvery { workoutRepository.createNewWorkout(any()) } returns Unit

            viewModel.saveWorkout()

            advanceUntilIdle()

            coVerify { workoutRepository.createNewWorkout(viewModel.workout.value!!) }
            assertEquals(SavingWorkoutState.Saved, viewModel.saveState.value)
        }

    @Test
    fun workoutEditorViewModel_callsRepositoryAndGetsAllExistingExercises() =
        runTest {
            val exercises =
                listOf(
                    WorkoutExercise(
                        name = "Test Exercise",
                        setCount = 0,
                        order = 0,
                        sets = mutableStateListOf()
                    ),
                    WorkoutExercise(
                        name = "Test Exercise 2",
                        setCount = 0,
                        order = 1,
                        sets = mutableStateListOf()
                    )
                )

            coEvery { workoutRepository.getAllExercises() } returns flow { emit(exercises) }

            val result = viewModel.getExistingExercises()

            advanceUntilIdle()

            coVerify { workoutRepository.getAllExercises() }

            assertEquals(
                2,
                result.size
            )
            assertEquals(
                "Test Exercise",
                result[0].name
            )

            assertEquals(
                "Test Exercise 2",
                result[1].name
            )
        }
}
