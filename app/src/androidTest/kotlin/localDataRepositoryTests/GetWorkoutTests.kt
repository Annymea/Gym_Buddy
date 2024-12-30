package localDataRepositoryTests

import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.WorkoutSet
import com.example.gymbuddy.data.localdatabase.ExecutionEntity
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import getExerciseDetails
import getWorkout
import getWorkoutDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetWorkoutTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }

    @Test
    fun testGetWorkout_NoErrorsFromDatabase() =
        runTest {
            val workoutDetails = getWorkoutDetails()
            val workoutEntities =
                listOf(
                    getWorkout(1L),
                    getWorkout(2L),
                    getWorkout(3L),
                )
            val workoutExerciseDetails = getExerciseDetails()

            val expectedResult =
                Workout(
                    id = 1,
                    name = "Test Workout",
                    note = "",
                    category = "",
                    exercises =
                    mutableStateListOf(
                        WorkoutExercise(
                            id = 1,
                            name = "Test Exercise",
                            order = 1,
                            setCount = 3,
                            note = "",
                            category = "",
                            sets = mutableStateListOf(),
                        ),
                        WorkoutExercise(
                            id = 1,
                            name = "Test Exercise",
                            order = 1,
                            setCount = 3,
                            note = "",
                            category = "",
                            sets = mutableStateListOf(),
                        ),
                        WorkoutExercise(
                            id = 1,
                            name = "Test Exercise",
                            order = 1,
                            setCount = 3,
                            note = "",
                            category = "",
                            sets = mutableStateListOf(),
                        ),
                    ),
                )

            coEvery { workoutDao.getWorkoutDetailsFor(any()) } returns flow { emit(workoutDetails) }
            coEvery { workoutDao.getWorkoutFor(any()) } returns flow { emit(workoutEntities) }
            coEvery { workoutDao.getExerciseDetailsFor(any()) } returns
                    flow {
                        emit(
                            workoutExerciseDetails,
                        )
                    }

            coEvery { workoutDao.getExecutionsFor(any()) } returns
                    flow { emit(emptyList()) }

            val result = workoutRepository.getWorkout(1L)

            if (result != null) {
                assertEquals("Workout IDs should match", expectedResult.id, result.id)
                assertEquals("Workout names should match", expectedResult.name, result.name)
                assertEquals(
                    "Workout categories should match",
                    expectedResult.category,
                    result.category,
                )
                assertEquals(
                    "Workout notes should match",
                    expectedResult.note,
                    result.note,
                )
            }
        }


    @Test
    fun testGetWorkout_AddsSetsWhenExecutionsExist() =
        runTest {
            val workoutDetails = getWorkoutDetails()
            val workoutEntities = listOf(getWorkout(1L))
            val exerciseDetails = getExerciseDetails()

            val executionEntities =
                listOf(
                    ExecutionEntity(
                        id = 1,
                        weight = 50F,
                        reps = 10,
                        date = 123456789L,
                        exerciseDetailsId = 1L,
                    ),
                    ExecutionEntity(
                        id = 2,
                        weight = 60F,
                        reps = 8,
                        date = 123456789L,
                        exerciseDetailsId = 1L,
                    ),
                )

            val expectedSets =
                listOf(
                    WorkoutSet(weight = 50F, reps = 10, order = 0),
                    WorkoutSet(weight = 60F, reps = 8, order = 1),
                )

            coEvery { workoutDao.getWorkoutDetailsFor(any()) } returns flow { emit(workoutDetails) }
            coEvery { workoutDao.getWorkoutFor(any()) } returns flow { emit(workoutEntities) }
            coEvery { workoutDao.getExerciseDetailsFor(any()) } returns
                    flow { emit(exerciseDetails) }
            coEvery { workoutDao.getExecutionsFor(any()) } returns flow { emit(executionEntities) }

            val result = workoutRepository.getWorkout(1L)

            if (result != null) {
                val actualSets = result.exercises.firstOrNull()?.sets
                assertEquals(expectedSets.size, actualSets?.size)
                expectedSets.forEachIndexed { index, expectedSet ->
                    assertEquals(expectedSet.weight, actualSets?.get(index)?.weight)
                    assertEquals(expectedSet.reps, actualSets?.get(index)?.reps)
                    assertEquals(expectedSet.order, actualSets?.get(index)?.order)
                }
            }
        }

    @Test
    fun testGetWorkout_NoSetsWhenExecutionsEmpty() =
        runTest {
            val workoutDetails = getWorkoutDetails()
            val workoutEntities = listOf(getWorkout(1L))
            val exerciseDetails = getExerciseDetails()

            val executionEntities = emptyList<ExecutionEntity>()

            coEvery { workoutDao.getWorkoutDetailsFor(any()) } returns flow { emit(workoutDetails) }
            coEvery { workoutDao.getWorkoutFor(any()) } returns flow { emit(workoutEntities) }
            coEvery { workoutDao.getExerciseDetailsFor(any()) } returns
                    flow { emit(exerciseDetails) }
            coEvery { workoutDao.getExecutionsFor(any()) } returns flow { emit(executionEntities) }

            val result = workoutRepository.getWorkout(1L)

            if (result != null) {
                val actualSets = result.exercises.firstOrNull()?.sets
                assertNotNull(actualSets)
                assertTrue(actualSets?.isEmpty() == true)
            }
        }

    @Test
    fun testGetWorkout_DatabaseReturnsNoWorkoutDetails() =
        runTest {
            val workoutEntities =
                listOf(
                    getWorkout(exerciseDetailsId = 1L),
                    getWorkout(exerciseDetailsId = 1L),
                    getWorkout(exerciseDetailsId = 2L),
                )
            val workoutExerciseDetails = getExerciseDetails()

            coEvery { workoutDao.getWorkoutDetailsFor(any()) } returns flow { emit(null) }
            coEvery { workoutDao.getWorkoutFor(any()) } returns flow { emit(workoutEntities) }
            coEvery { workoutDao.getExerciseDetailsFor(any()) } returns
                    flow {
                        emit(
                            workoutExerciseDetails,
                        )
                    }

            val result = workoutRepository.getWorkout(1L)

            assertEquals(null, result)
        }

    @Test
    fun testGetWorkout_NotFoundExercisesAreSkipped() =
        runTest {
            val workoutDetails = getWorkoutDetails()
            val workoutEntities =
                listOf(
                    getWorkout(exerciseDetailsId = 1L),
                    getWorkout(exerciseDetailsId = 1L),
                    getWorkout(exerciseDetailsId = 2L),
                )
            val workoutExerciseDetails = getExerciseDetails()

            val expectedResult =
                Workout(
                    id = 1,
                    name = "Test Workout",
                    note = "",
                    category = "",
                    exercises =
                    mutableStateListOf(
                        WorkoutExercise(
                            id = 1,
                            name = "Test Exercise",
                            order = 1,
                            setCount = 3,
                            note = "",
                            category = "",
                            sets = mutableStateListOf(),
                        ),
                        WorkoutExercise(
                            id = 1,
                            name = "Test Exercise",
                            order = 1,
                            setCount = 3,
                            note = "",
                            category = "",
                            sets = mutableStateListOf(),
                        ),
                    ),
                )

            coEvery { workoutDao.getWorkoutDetailsFor(any()) } returns flow { emit(workoutDetails) }
            coEvery { workoutDao.getWorkoutFor(any()) } returns flow { emit(workoutEntities) }
            coEvery { workoutDao.getExerciseDetailsFor(1) } returns
                    flow {
                        emit(
                            workoutExerciseDetails,
                        )
                    }
            coEvery { workoutDao.getExerciseDetailsFor(2) } returns flow { emit(null) }
            coEvery { workoutDao.getExecutionsFor(any()) } returns
                    flow { emit(emptyList()) }

            val result = workoutRepository.getWorkout(2L)

            if (result != null) {
                assertEquals("Workout IDs should match", expectedResult.id, result.id)
                assertEquals("Workout names should match", expectedResult.name, result.name)
                assertEquals("Found exercises should match", 2, result.exercises.size)
            }
        }


    @Test
    fun testGetWorkout_FillsExercisesWithPreviousSets() =
        runTest {
            val workoutDetails = getWorkoutDetails()
            val workoutEntities = listOf(getWorkout(1L), getWorkout(2L))
            val exerciseDetails = getExerciseDetails()

            val executionEntities =
                listOf(
                    ExecutionEntity(
                        id = 1,
                        weight = 20F,
                        date = 123456789L,
                        reps = 10,
                        exerciseDetailsId = 1L,
                    ),
                    ExecutionEntity(
                        id = 2,
                        weight = 25F,
                        date = 123456789L,
                        reps = 8,
                        exerciseDetailsId = 1L,
                    ),
                )

            val expectedSets =
                mutableStateListOf(
                    WorkoutSet(weight = 20F, reps = 10, order = 0),
                    WorkoutSet(weight = 25F, reps = 8, order = 1),
                )

            coEvery { workoutDao.getWorkoutDetailsFor(any()) } returns flow { emit(workoutDetails) }
            coEvery { workoutDao.getWorkoutFor(any()) } returns flow { emit(workoutEntities) }
            coEvery { workoutDao.getExerciseDetailsFor(any()) } returns
                    flow { emit(exerciseDetails) }
            coEvery { workoutDao.getExecutionsFor(1) } returns flow { emit(executionEntities) }
            coEvery { workoutDao.getExecutionsFor(2) } returns flow { emit(emptyList()) }

            val result = workoutRepository.getWorkout(1L)

            if (result != null) {
                val actualSets = result.exercises.firstOrNull()?.sets
                assertEquals(expectedSets.size, actualSets?.size)
                expectedSets.forEachIndexed { index, expectedSet ->
                    assertEquals(expectedSet.weight, actualSets?.get(index)?.weight)
                    assertEquals(expectedSet.reps, actualSets?.get(index)?.reps)
                    assertEquals(expectedSet.order, actualSets?.get(index)?.order)
                }
            }
        }

    @Test
    fun testGetWorkout_ReturnsEmptySetsWhenNoPreviousExecutions() =
        runTest {
            val workoutDetails = getWorkoutDetails()
            val workoutEntities = listOf(getWorkout(1))
            val exerciseDetails = getExerciseDetails()

            val executionEntities = emptyList<ExecutionEntity>()

            coEvery { workoutDao.getWorkoutDetailsFor(any()) } returns flow { emit(workoutDetails) }
            coEvery { workoutDao.getWorkoutFor(any()) } returns flow { emit(workoutEntities) }
            coEvery { workoutDao.getExerciseDetailsFor(any()) } returns
                    flow { emit(exerciseDetails) }
            coEvery { workoutDao.getExecutionsFor(1) } returns flow { emit(executionEntities) }

            val result = workoutRepository.getWorkout(1)

            if (result != null) {
                val actualSets = result.exercises.firstOrNull()?.sets
                assertEquals(0, actualSets?.size)
            }
        }

}