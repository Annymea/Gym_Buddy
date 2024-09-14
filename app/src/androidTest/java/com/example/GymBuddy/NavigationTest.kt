package com.example.GymBuddy

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.GymBuddy.data.localdatabase.Plan
import com.example.GymBuddy.ui.navigation.AppNavigation
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class NavigationTest {
    private lateinit var navController: TestNavHostController
    private lateinit var mockRepository: WorkoutRepository
    private lateinit var testModule: Module

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setupAppNavHost() {
        testModule = module {
            single<WorkoutRepository> { mockRepository }
        }
        createMockRepository()
        loadKoinModules(testModule)

        val context = InstrumentationRegistry.getInstrumentation().targetContext

        navController = TestNavHostController(context)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        composeTestRule.setContent {
            AppNavigation(navController = navController)
        }
    }

    private fun createMockRepository() {
        mockRepository = mockk()

        val plans = listOf(
            Plan(planName = "Plan 1", id = 1),
            Plan(planName = "Plan 2", id = 2)
        )
        val plan = Plan(planName = "Plan 1", id = 1)

        val executablePlansWithDetails = listOf(
            ExecutablePlanWithDetails(
                sets = 3,
                order = 0,
                planId = 1,
                planName = "Plan 1",
                exerciseName = "Exercise 1",
                executablePlanId = 0,
                exerciseId = 1
            ),
            ExecutablePlanWithDetails(
                sets = 3,
                order = 1,
                planId = 1,
                planName = "Plan 1",
                exerciseName = "Exercise 2",
                executablePlanId = 0,
                exerciseId = 2
            )
        )

        coEvery { mockRepository.getAllPlanNames() } returns flow {
            Log.i("NavigationTest", "Mocking getAllPlanNames flow with: $plans")
            emit(plans)
        }

        coEvery { mockRepository.getPlanById(any()) } returns flow {
            Log.i("NavigationTest", "Mocking getPlanById flow with: $plan")
            emit(plan)
        }

        coEvery { mockRepository.getExecutablePlanWithDetailsByPlanId(any()) } returns flow {
            Log.i(
                "NavigationTest",
                "Mocking getExecutablePlanWithDetailsByPlanId flow with: $executablePlansWithDetails"
            )
            emit(executablePlansWithDetails)
        }

        coEvery { mockRepository.insertPlan(any()) } returns 1L
        coEvery { mockRepository.insertExecutablePlan(any()) } returns 1L
        coEvery { mockRepository.insertExercise(any()) } returns 1L
    }

    @After
    fun tearDown() {
        unloadKoinModules(testModule)
    }

    @Test
    fun appNavigation_startOnDashboard() {
        composeTestRule.onNodeWithText("Dashboard Content").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigateToDashboard() {
        composeTestRule.onNodeWithText("Run workout").performClick()
        composeTestRule.onNodeWithText("Dashboard").performClick()
        composeTestRule.onNodeWithText("Dashboard Content").assertIsDisplayed()
    }

    /*Create workout navigation test*/
    @Test
    fun appNavigation_navigateToWorkoutList() {
        composeTestRule.onNodeWithText("Create workout").performClick()
        composeTestRule.onNodeWithText("Pläne").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigateToNewWorkout() {
        composeTestRule.onNodeWithText("Create workout").performClick()
        composeTestRule.onNodeWithText("Neuen Plan erstellen").performClick()
        composeTestRule.onNodeWithText("Dein Plan").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigateBackToWorkoutListAfterCreateNewWorkout() {
        composeTestRule.onNodeWithText("Create workout").performClick()
        composeTestRule.onNodeWithText("Neuen Plan erstellen").performClick()
        composeTestRule.onNodeWithText("Plan name").performTextInput("Test Plan")
        composeTestRule.onNodeWithText("Save Plan").performClick()

        composeTestRule.onNodeWithText("Neuen Plan erstellen").assertIsDisplayed()
    }

    /*Run workout navigation test*/
    @Test
    fun appNavigation_navigateToStartWorkout() {
        composeTestRule.onNodeWithText("Run workout").performClick()
        composeTestRule.onNodeWithText("Training starten!!").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigateToRunningWorkout() {
        composeTestRule.onNodeWithText("Run workout").performClick()
        composeTestRule.onNodeWithText("Plan 1").performClick()

        composeTestRule.onNodeWithText("Plan 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigateToDashboardAfterSavingWorkout() {
        composeTestRule.onNodeWithText("Run workout").performClick()
        composeTestRule.onNodeWithText("Plan 1").performClick()
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithText("Dashboard Content").assertIsDisplayed()
    }
}
