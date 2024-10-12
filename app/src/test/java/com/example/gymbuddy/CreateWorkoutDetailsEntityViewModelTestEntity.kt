package com.example.gymbuddy

import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.ExerciseDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.ui.old.createPlan.CreatePlanViewModel
import com.example.gymbuddy.ui.old.createPlan.SavingPlanState
import com.example.gymbuddy.ui.old.createPlan.ViewModelExercise
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateWorkoutDetailsEntityViewModelTestEntity {
    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var createPlanViewModel: CreatePlanViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        workoutRepository = mockk()
        createPlanViewModel = CreatePlanViewModel(workoutRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @Test
    fun `test addExercise adds exercise to list`() {
        val exercise = ViewModelExercise(name = "Push-up", sets = 3)

        createPlanViewModel.addExercise(exercise)

        assertEquals(1, createPlanViewModel.exerciseListToBeSaved.size)
        assertEquals(exercise, createPlanViewModel.exerciseListToBeSaved[0])
    }

    @Test
    fun `test updateExercise updates exercise in list`() {
        val exercise = ViewModelExercise(name = "Push-up", sets = 3)
        val editedExercise = ViewModelExercise(name = "Push-up", sets = 4)

        createPlanViewModel.updateExercise(exercise)
        assertEquals(exercise, createPlanViewModel.exerciseToAdd.value)

        createPlanViewModel.updateExercise(editedExercise)
        assertEquals(editedExercise, createPlanViewModel.exerciseToAdd.value)
    }

    @Test
    fun `test updatePlanName updates plan name`() {
        val planName = "Test Plan"
        val newPlanName = "New Plan"

        createPlanViewModel.updatePlanName(planName)
        assertEquals(planName, createPlanViewModel.planName.value)

        createPlanViewModel.updatePlanName(newPlanName)
        assertEquals(newPlanName, createPlanViewModel.planName.value)
    }

    @Test
    fun `savePlanToDatabase should set Error state when planName is empty`() =
        runTest {
            createPlanViewModel.updatePlanName("")

            createPlanViewModel.savePlanToDatabase()
            advanceUntilIdle()
            assertEquals(
                SavingPlanState.Error("Plan name cannot be empty"),
                createPlanViewModel.saveState.value,
            )
        }

    @Test
    fun `savePlanToDatabase should save plan and exercises successfully`() =
        runTest {
            createPlanViewModel.updatePlanName("My Plan")
            createPlanViewModel.addExercise(ViewModelExercise(name = "Squat", sets = 3))
            createPlanViewModel.addExercise(ViewModelExercise(name = "Push-Up", sets = 4))

            val planId: Long = 1
            val exerciseId: Long = 1
            val executablePlanId: Long = 1

            coEvery { workoutRepository.insertPlan(any()) } returns planId
            coEvery { workoutRepository.insertExercise(any()) } returns exerciseId
            coEvery { workoutRepository.insertExecutablePlan(any()) } returns executablePlanId

            createPlanViewModel.savePlanToDatabase()

            advanceUntilIdle()

            coVerify { workoutRepository.insertPlan(WorkoutDetailsEntity(workoutName = "My Plan")) }
            coVerify {
                workoutRepository.insertExercise(
                    ExerciseDetailsEntity(exerciseName = "Squat"),
                )
            }
            coVerify {
                workoutRepository.insertExercise(
                    ExerciseDetailsEntity(exerciseName = "Push-Up"),
                )
            }
            coVerify(exactly = 2) { workoutRepository.insertExecutablePlan(any()) }

            assertEquals(SavingPlanState.Saved, createPlanViewModel.saveState.value)
        }

    @Test
    fun `savePlanToDatabase should set Error state when an exception occurs during plan saving`() =
        runTest {
            createPlanViewModel.updatePlanName("My Plan")

            coEvery { workoutRepository.insertPlan(any()) } throws
                RuntimeException(
                    "Database error",
                )

            createPlanViewModel.savePlanToDatabase()

            advanceUntilIdle()

            assertEquals(
                SavingPlanState.Error("Failed to add plan to database: Database error"),
                createPlanViewModel.saveState.value,
            )
        }

    @Test
    fun `savePlanToDatabase sets Error state when an exception occurs during exercise saving`() =
        runTest {
            createPlanViewModel.updatePlanName("My Plan")
            createPlanViewModel.addExercise(ViewModelExercise(name = "Squat", sets = 3))

            val planId = 1L

            coEvery { workoutRepository.insertPlan(any()) } returns planId
            coEvery { workoutRepository.insertExercise(any()) } throws
                RuntimeException(
                    "Database error",
                )

            createPlanViewModel.savePlanToDatabase()

            advanceUntilIdle()

            assertEquals(
                SavingPlanState.Error("Failed to add exercise: Database error"),
                createPlanViewModel.saveState.value,
            )
        }
}
