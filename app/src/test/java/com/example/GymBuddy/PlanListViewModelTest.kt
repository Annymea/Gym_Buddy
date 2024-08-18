package com.example.GymBuddy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.Plan
import com.example.GymBuddy.ui.planList.PlanListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
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
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlanListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var planListViewModel: PlanListViewModel

    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        workoutRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch plans from repository and update planList`() = runTest {
        val plans = listOf(Plan(planName = "Plan 1"), Plan(planName = "Plan 2"))
        coEvery { workoutRepository.getAllPlanNames() } returns flow { emit(plans) }

        planListViewModel = PlanListViewModel(workoutRepository)

        advanceUntilIdle()

        assertEquals(plans, planListViewModel.planList)
    }

    @Test
    fun `planList should be empty when repository returns empty list`() = runTest {
        coEvery { workoutRepository.getAllPlanNames() } returns flow { emit(emptyList()) }

        planListViewModel = PlanListViewModel(workoutRepository)

        advanceUntilIdle()

        assertEquals(emptyList<Plan>(), planListViewModel.planList)
    }

    @Test
    fun `planList should update correctly when repository emits new data`() = runTest {
        val initialPlans = listOf(Plan(planName = "Initial Plan"))
        val updatedPlans = listOf(Plan(planName = "Updated Plan"))

        val planFlow = flow {
            emit(initialPlans)
            emit(updatedPlans)
        }

        coEvery { workoutRepository.getAllPlanNames() } returns planFlow

        planListViewModel = PlanListViewModel(workoutRepository)

        advanceUntilIdle()

        assertEquals(updatedPlans, planListViewModel.planList)
    }
}
