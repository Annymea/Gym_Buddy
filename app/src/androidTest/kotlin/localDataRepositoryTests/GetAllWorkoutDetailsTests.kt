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

class GetAllWorkoutDetailsTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }


    @Test
    fun testGetAllWorkoutDetails_ReturnsDataFromDatabase() =
        runTest {
            val workoutDetailsEntityList =
                listOf(
                    getWorkoutDetails(id = 1L, name = "Workout 1"),
                    getWorkoutDetails(id = 2L, name = "Workout 2"),
                    getWorkoutDetails(id = 3L, name = "Workout 3"),
                )

            coEvery { workoutDao.getAllWorkoutDetails() } returns
                    flow { emit(workoutDetailsEntityList) }

            val result = workoutRepository.getAllWorkoutDetails().first()

            assertEquals("Number of returned workouts should match", 3, result.size)
            assertEquals("Workout names should match", "Workout 1", result[0].name)
            assertEquals("Workout names should match", "Workout 2", result[1].name)
            assertEquals("Workout names should match", "Workout 3", result[2].name)
        }

    @Test
    fun testGetAllWorkoutDetails_EmptyListWhenNoData() =
        runTest {
            coEvery { workoutDao.getAllWorkoutDetails() } returns flow { emit(emptyList()) }

            val result = workoutRepository.getAllWorkoutDetails().first()

            assertEquals(emptyList<Workout>(), result)
        }

}