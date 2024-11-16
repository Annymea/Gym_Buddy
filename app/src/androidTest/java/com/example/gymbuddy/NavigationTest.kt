package com.example.gymbuddy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gymbuddy.data.LocalDataRepositoryModule
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(LocalDataRepositoryModule::class)
class NavigationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mockRepository: WorkoutRepository

    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        navController = TestNavHostController(context)
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        navController.setLifecycleOwner(TestLifecycleOwner())
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
        val mockRepository = mockk<WorkoutRepository>(relaxed = true)

        val oneWorkout = Workout(name = "Test Workout")

        coEvery { mockRepository.getAllWorkoutDetails() } returns
            flow {
                emit(listOf(oneWorkout))
            }

        coEvery { mockRepository.getWorkout(any()) } returns oneWorkout

        coEvery { mockRepository.getDaysOfCompletedWorkoutsForMonth(any(), any()) } returns
            flowOf(
                listOf(1, 5, 10)
            )

        return mockRepository
    }
}
