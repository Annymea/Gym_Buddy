package com.example.gymbuddy

import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.gymbuddy.data.localdatabase.Plan
import com.example.gymbuddy.ui.old.runningWorkout.ExerciseExecution
import com.example.gymbuddy.ui.old.runningWorkout.RunningWorkoutViewModel
import io.mockk.coEvery
import io.mockk.coVerify
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
class RunningWorkoutViewModelTest {
    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var viewModel: RunningWorkoutViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        workoutRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `gets exercises and planName by workoutId`() =
        runTest {
            val exercises = getExerciseList()
            val workout = Plan(planName = "Plan 1", id = 1)

            val workoutId = 1L

            coEvery { workoutRepository.getPlanWithDetailsBy(workoutId) } returns
                flow { emit(exercises) }
            coEvery { workoutRepository.getPlanById(workoutId) } returns flow { emit(workout) }

            viewModel =
                RunningWorkoutViewModel(
                    workoutRepository = workoutRepository,
                    workoutId = workoutId.toString()
                )

            advanceUntilIdle()

            assertEquals(exercises, viewModel.exercises)
            assertEquals(workout.planName, viewModel.planName.value)
        }

    @Test
    fun `gets correct executions`() =
        runTest {
            val executions =
                listOf(
                    ExerciseExecution(exerciseId = 1, set = 1, reps = 10, weight = 50),
                    ExerciseExecution(exerciseId = 1, set = 2, reps = 8, weight = 55),
                    ExerciseExecution(exerciseId = 2, set = 1, reps = 12, weight = 60)
                )

            viewModel = createViewModel()

            executions.forEach {
                viewModel.addOrUpdateExecution(it)
            }

            val result = viewModel.getExecutions(1L)

            assert(result.size == 2)
            assert(result[0].reps == 10)
            assert(result[1].weight == 55)
        }

    @Test
    fun `update existing execution`() =
        runTest {
            viewModel = createViewModel()

            val initialExecution = ExerciseExecution(
                exerciseId = 1,
                set = 1,
                reps = 10,
                weight = 50
            )
            viewModel.addOrUpdateExecution(initialExecution)

            val updatedExecution = ExerciseExecution(
                exerciseId = 1,
                set = 1,
                reps = 12,
                weight = 55
            )
            viewModel.addOrUpdateExecution(updatedExecution)

            val result = viewModel.getExecutions(1L)

            assert(result.size == 1)
            assert(result[0].reps == 12)
            assert(result[0].weight == 55)
        }

    @Test
    fun `add new execution`() =
        runTest {
            viewModel = createViewModel()

            val initialExecution = ExerciseExecution(
                exerciseId = 1,
                set = 1,
                reps = 10,
                weight = 50
            )
            viewModel.addOrUpdateExecution(initialExecution)

            val newExecution = ExerciseExecution(exerciseId = 1, set = 2, reps = 12, weight = 55)
            viewModel.addOrUpdateExecution(newExecution)

            val result = viewModel.getExecutions(1L)

            assert(result.size == 2)
            assert(result[1].reps == 12)
            assert(result[1].weight == 55)
        }

    @Test
    fun `save all executions`() =
        runTest {
            val executions =
                listOf(
                    ExerciseExecution(exerciseId = 1, set = 1, reps = 10, weight = 50),
                    ExerciseExecution(exerciseId = 1, set = 2, reps = 8, weight = 55)
                )

            viewModel = createViewModel()

            executions.forEach {
                viewModel.addOrUpdateExecution(it)
            }

            coEvery { workoutRepository.insertExecution(any()) } returns 1L

            viewModel.saveExecutionsToRepository()

            advanceUntilIdle()

            coVerify(exactly = 2) { workoutRepository.insertExecution(any()) }
        }

    private fun createViewModel(): RunningWorkoutViewModel {
        val exercises = getExerciseList()
        val workout = Plan(planName = "Plan 1", id = 1)
        val workoutId = 1L

        coEvery { workoutRepository.getPlanWithDetailsBy(workoutId) } returns
            flow { emit(exercises) }
        coEvery { workoutRepository.getPlanById(workoutId) } returns flow { emit(workout) }

        val viewModel = RunningWorkoutViewModel(workoutRepository, workoutId.toString())

        return viewModel
    }

    private fun getExerciseList(): List<ExecutablePlanWithDetails> =
        listOf(
            ExecutablePlanWithDetails(
                planId = 1,
                exerciseName = "Exercise 1",
                planName = "Plan 1",
                sets = 3,
                order = 0,
                exerciseId = 1,
                executablePlanId = 0
            ),
            ExecutablePlanWithDetails(
                planId = 2,
                exerciseName = "Exercise 2",
                planName = "Plan 2",
                sets = 4,
                order = 1,
                exerciseId = 2,
                executablePlanId = 0
            )
        )
}
