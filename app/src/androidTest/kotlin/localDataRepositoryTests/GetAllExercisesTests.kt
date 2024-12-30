package localDataRepositoryTests

import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import getExerciseDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetAllExercisesTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }

    @Test
    fun testGetAllExercises_ReturnsCorrectMappedData() =
        runTest {
            val exerciseDetailsEntities =
                listOf(
                    getExerciseDetails(id = 1L),
                    getExerciseDetails(
                        id = 2L,
                    ).copy(exerciseName = "Squat", note = "Leg strength exercise"),
                )

            val expectedExercises =
                listOf(
                    WorkoutExercise(
                        id = 1L,
                        name = "Test Exercise",
                        note = "",
                        setCount = 0,
                        order = 0,
                        sets = mutableStateListOf(),
                    ),
                    WorkoutExercise(
                        id = 2L,
                        name = "Squat",
                        note = "Leg strength exercise",
                        setCount = 0,
                        order = 0,
                        sets = mutableStateListOf(),
                    ),
                )

            coEvery { workoutDao.getAllExerciseDetails() } returns
                    flow { emit(exerciseDetailsEntities) }

            val result = workoutRepository.getAllExercises().first()

            assertEquals(expectedExercises.size, result.size)
            expectedExercises.forEachIndexed { index, expectedExercise ->
                val actualExercise = result[index]
                assertEquals(expectedExercise.id, actualExercise.id)
                assertEquals(expectedExercise.name, actualExercise.name)
                assertEquals(expectedExercise.note, actualExercise.note)
                assertEquals(expectedExercise.setCount, actualExercise.setCount)
                assertEquals(expectedExercise.order, actualExercise.order)
                assertTrue(actualExercise.sets.isEmpty())
            }
        }

    @Test
    fun testGetAllExercises_ReturnsEmptyListWhenNoData() =
        runTest {
            coEvery { workoutDao.getAllExerciseDetails() } returns flow { emit(emptyList()) }

            val result = workoutRepository.getAllExercises().first()

            assertTrue(result.isEmpty())
        }

}