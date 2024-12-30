package localDataRepositoryTests

import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import getExerciseDetails
import getWorkout
import getWorkoutDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class GetDaysOfCompletedWorkoutsForMonthTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }


    @Test
    fun testGetDaysOfCompletedWorkoutsForMonth_ReturnsCorrectDays() =
        runTest {
            val month = 10
            val year = 2023

            val calendar =
                Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month - 1)
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            val startDate = calendar.timeInMillis
            calendar.add(Calendar.MONTH, 1)
            calendar.add(Calendar.MILLISECOND, -1)
            val endDate = calendar.timeInMillis

            val executionTimestamps =
                listOf(
                    startDate + (60 * 60 * 24 * 2 * 1000),
                    startDate + (60 * 60 * 24 * 10 * 1000),
                    startDate + (60 * 60 * 24 * 20 * 1000),
                )

            val expectedDays = listOf(3, 11, 21)

            coEvery {
                workoutDao.getAllDatesOfExecutionsForTimeSpan(
                    startDate,
                    endDate,
                )
            } returns flow { emit(executionTimestamps) }

            val result = workoutRepository.getDaysOfCompletedWorkoutsForMonth(month, year).first()
            assertEquals(expectedDays, result)
        }

    @Test
    fun testGetDaysOfCompletedWorkoutsForMonth_ReturnsEmptyListWhenNoExecutions() =
        runTest {
            val month = 11
            val year = 2023

            val calendar =
                Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month - 1)
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            val startDate = calendar.timeInMillis
            calendar.add(Calendar.MONTH, 1)
            calendar.add(Calendar.MILLISECOND, -1)
            val endDate = calendar.timeInMillis

            coEvery {
                workoutDao.getAllDatesOfExecutionsForTimeSpan(
                    startDate,
                    endDate,
                )
            } returns flow { emit(emptyList()) }

            val result = workoutRepository.getDaysOfCompletedWorkoutsForMonth(month, year).first()
            assertEquals(emptyList<Int>(), result)
        }

    @Test
    fun testGetDaysOfCompletedWorkoutsForMonth_OnlyReturnsDaysInTheSpecifiedMonth() =
        runTest {
            val month = 12
            val year = 2023

            val calendar =
                Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month - 1)
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            val startDate = calendar.timeInMillis
            calendar.add(Calendar.MONTH, 1)
            calendar.add(Calendar.MILLISECOND, -1)
            val endDate = calendar.timeInMillis

            val executionTimestamps =
                listOf(
                    startDate + (60 * 60 * 24 * 5 * 1000),
                    endDate + (60 * 60 * 24 * 2 * 1000),
                )

            val expectedDays = listOf(6)

            coEvery {
                workoutDao.getAllDatesOfExecutionsForTimeSpan(
                    startDate,
                    endDate,
                )
            } returns flow { emit(executionTimestamps) }

            val result = workoutRepository.getDaysOfCompletedWorkoutsForMonth(month, year).first()
            assertEquals(expectedDays, result)
        }
}