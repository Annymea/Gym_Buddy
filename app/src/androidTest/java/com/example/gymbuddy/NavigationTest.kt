package com.example.gymbuddy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.ui.navigation.AppNavigation
import io.mockk.clearMocks
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
        testModule =
            module {
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

        val oneWorkout =
            Workout(
                name = "Test Workout"
            )

        coEvery { mockRepository.getAllWorkoutDetails() } returns
            flow {
                emit(listOf(oneWorkout))
            }

        coEvery { mockRepository.getWorkout(any()) } returns
            oneWorkout
    }

    @After
    fun tearDown() {
        clearMocks(mockRepository)
        unloadKoinModules(testModule)
    }

    @Test
    fun navigation_startOnDashboard() {
        composeTestRule
            .onNodeWithTag("dashboardTitle")
            .assertIsDisplayed()
            .assertTextContains("Dashboard Content")
    }

    // --------- Workout workflow ---------

    @Test
    fun navigation_navigateToWorkoutOverviewWithBottomNavigation() {
        composeTestRule
            .onNodeWithTag("bottomNavigation_Workouts")
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithTag("screenTitle_Overview")
            .assertIsDisplayed()
            .assertTextContains("Workouts")
    }

    @Test
    fun navigation_navigateToCreateWorkout() {
        composeTestRule
            .onNodeWithTag("bottomNavigation_Workouts")
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithTag("createWorkoutButton")
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithTag("screenTitle_Editor")
            .assertIsDisplayed()
            .assertTextContains("New Workout")
    }

    @Test
    fun navigation_navigateToStartWorkout() {
        composeTestRule
            .onNodeWithTag("bottomNavigation_Workouts")
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithTag("startWorkoutButton")
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithTag("screenTitle_Executor")
            .assertIsDisplayed()
    }
}
