package com.example.gymbuddy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gymbuddy.data.LocalDataRepositoryModule
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.ui.navigation.AppNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Singleton
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    private lateinit var navController: TestNavHostController
    private lateinit var mockRepository: WorkoutRepository

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Before
    fun setupAppNavHost() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        navController = TestNavHostController(context)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        composeTestRule.setContent {
            AppNavigation(navController = navController)
        }
    }

    @After
    fun tearDown() {
        clearMocks(mockRepository)
    }

    @Test
    fun navigation_startOnDashboard() {
        composeTestRule
            .onNodeWithTag("dashboard_title")
            .assertIsDisplayed()
            .assertTextContains("Dashboard")
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

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalDataRepositoryModule::class]
)
object TestRepositoryModule {
    @Provides
    @Singleton
    fun provideWorkoutRepository(): WorkoutRepository {
        val mockRepository = mockk<WorkoutRepository>()

        val oneWorkout = Workout(name = "Test Workout")

        coEvery { mockRepository.getAllWorkoutDetails() } returns
            flow {
                emit(listOf(oneWorkout))
            }

        coEvery { mockRepository.getWorkout(any()) } returns oneWorkout

        return mockRepository
    }
}
