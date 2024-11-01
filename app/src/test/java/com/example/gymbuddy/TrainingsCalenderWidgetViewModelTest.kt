package com.example.gymbuddy

import android.util.Log
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.ui.dashboard.widgets.trainingsCalender.TrainingsCalenderViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import java.time.LocalDate
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
class TrainingsCalenderWidgetViewModelTest {
    private lateinit var viewModel: TrainingsCalenderViewModel
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = TrainingsCalenderViewModel(workoutRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun trainingsCalenderViewModel_initializesWithCurrentMonthAndYear() {
        assertEquals(LocalDate.now().monthValue, viewModel.shownMonth.value)
        assertEquals(LocalDate.now().year, viewModel.shownYear.value)
    }

    @Test
    fun trainingsCalenderViewModel_updatesHighlightedDaysOnInit() =
        runTest {
            val highlightedDays = listOf(5, 10, 15)
            coEvery {
                workoutRepository.getDaysOfCompletedWorkoutsForMonth(
                    any(),
                    any()
                )
            } returns flow { emit(highlightedDays) }

            viewModel = TrainingsCalenderViewModel(workoutRepository)
            advanceUntilIdle()

            assertEquals(highlightedDays, viewModel.highlightedForShownMonth)
        }

    @Test
    fun trainingsCalenderViewModel_updatesToNextMonth() =
        runTest {
            viewModel.shownMonth.value = 10
            viewModel.shownYear.value = 2024
            viewModel.updateMonth(increment = true)
            advanceUntilIdle()

            assertEquals(11, viewModel.shownMonth.value)
            assertEquals(2024, viewModel.shownYear.value)
        }

    @Test
    fun trainingsCalenderViewModel_updatesToPreviousMonth() =
        runTest {
            viewModel.shownMonth.value = 1
            viewModel.shownYear.value = 2024
            viewModel.updateMonth(increment = false)
            advanceUntilIdle()

            assertEquals(12, viewModel.shownMonth.value)
            assertEquals(2023, viewModel.shownYear.value)
        }

    @Test
    fun trainingsCalenderViewModel_nameOfMonth_returnsCorrectMonthName() {
        assertEquals("October", viewModel.nameOfMonth(10))
        assertEquals("January", viewModel.nameOfMonth(1))
        assertEquals("Unknown", viewModel.nameOfMonth(13))
    }

    @Test
    fun trainingsCalenderViewModel_updatesHighlightedDaysCorrectly() =
        runTest {
            val newHighlightedDays = listOf(1, 12, 23)
            coEvery {
                workoutRepository.getDaysOfCompletedWorkoutsForMonth(
                    any(),
                    any()
                )
            } returns flow { emit(newHighlightedDays) }

            viewModel.updateMonth(false)
            advanceUntilIdle()

            assertEquals(newHighlightedDays, viewModel.highlightedForShownMonth)
        }
}
