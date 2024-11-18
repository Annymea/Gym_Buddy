package com.example.gymbuddy

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.ui.common.addExerciseDialog.AddExerciseDialogViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddExerciseViewModelTest {
    private lateinit var viewModel: AddExerciseDialogViewModel
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = AddExerciseDialogViewModel(workoutRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onSaveExercise_addExerciseInRepository() =
        runTest {
            val newExercise =
                WorkoutExercise(
                    id = 0L,
                    name = "Test Exercise",
                    note = "Some note",
                    category = "Strength",
                    setCount = 0,
                    order = 0,
                    sets = mutableStateListOf()
                )

            viewModel.onSaveExercise(newExercise)

            testDispatcher.scheduler.advanceUntilIdle()

            coVerify { workoutRepository.addExercise(newExercise) }
        }

    @Test
    fun onSaveExercise_handlesError() =
        runTest {
            val newExercise =
                WorkoutExercise(
                    id = 0L,
                    name = "Test Exercise",
                    note = "Some note",
                    category = "Strength",
                    setCount = 0,
                    order = 0,
                    sets = mutableStateListOf()
                )

            coEvery { workoutRepository.addExercise(any()) } throws Exception("Repository error")

            viewModel.onSaveExercise(newExercise)
            testDispatcher.scheduler.advanceUntilIdle()

            coVerify { workoutRepository.addExercise(newExercise) }
        }
}
