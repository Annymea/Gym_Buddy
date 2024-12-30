package localDataRepositoryTests

import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.ExerciseDetailsEntity
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddExerciseTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }

    @Test
    fun testAddExercise_InsertsExerciseDetails() =
        runTest {
            val newExercise =
                WorkoutExercise(
                    id = 0L,
                    name = "Pull-Up",
                    note = "Upper body strength exercise",
                    category = "Strength",
                    setCount = 0,
                    order = 0,
                    sets = mutableStateListOf(),
                )

            coEvery { workoutDao.insertExerciseDetails(any()) } returns 1L

            workoutRepository.addExercise(newExercise)

            coVerify {
                workoutDao.insertExerciseDetails(
                    ExerciseDetailsEntity(
                        exerciseName = "Pull-Up",
                        note = "Upper body strength exercise",
                        category = "Strength",
                    ),
                )
            }
        }

    @Test
    fun testAddExercise_HandlesDatabaseError() =
        runTest {
            val validExercise =
                WorkoutExercise(
                    id = 0L,
                    name = "Valid Exercise",
                    note = "Some note",
                    category = "Strength",
                    setCount = 0,
                    order = 0,
                    sets = mutableStateListOf(),
                )

            coEvery { workoutDao.insertExerciseDetails(any()) } throws
                    RuntimeException("Database error")

            try {
                workoutRepository.addExercise(validExercise)
                fail("Expected a RuntimeException to be thrown")
            } catch (e: RuntimeException) {
                assertEquals("Database error", e.message)
            }

            coVerify { workoutDao.insertExerciseDetails(any()) }
        }
}