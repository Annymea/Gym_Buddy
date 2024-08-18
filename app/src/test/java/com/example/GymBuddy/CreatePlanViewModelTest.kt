package com.example.GymBuddy

import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.ui.createPlan.CreatePlanViewModel
import com.example.GymBuddy.ui.createPlan.ViewModelExercise
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CreatePlanViewModelTest {

    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var createPlanViewModel: CreatePlanViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = mockk()
        createPlanViewModel = CreatePlanViewModel(workoutRepository)
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
}
