import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.WorkoutSet
import com.example.gymbuddy.data.localdatabase.ExecutionEntity
import com.example.gymbuddy.data.localdatabase.ExerciseDetailsEntity
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LocalDataRepositoryTest {
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

            val result = workoutRepository.getWorkout(2L)

            if (result != null) {
                assertEquals("Workout IDs should match", expectedResult.id, result.id)
                assertEquals("Workout names should match", expectedResult.name, result.name)
                assertEquals("Found exercises should match", 2, result.exercises.size)
            }
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

    @Test
    fun testSaveWorkoutExecution_SavesCorrectEntities() =
        runTest {
            val date = 123456789L
            val doneExercises =
                listOf(
                    WorkoutExercise(
                        id = 1L,
                        name = "Exercise 1",
                        order = 1,
                        setCount = 2,
                        note = "",
                        category = "",
                        sets =
                            mutableStateListOf(
                                WorkoutSet(weight = 50.0F, reps = 10, order = 0),
                                WorkoutSet(weight = 55.0F, reps = 8, order = 1),
                            ),
                    ),
                    WorkoutExercise(
                        id = 2L,
                        name = "Exercise 2",
                        order = 2,
                        setCount = 1,
                        note = "",
                        category = "",
                        sets =
                            mutableStateListOf(
                                WorkoutSet(weight = 60.0F, reps = 6, order = 0),
                            ),
                    ),
                )

            val expectedExecutionEntities =
                listOf(
                    ExecutionEntity(exerciseDetailsId = 1L, weight = 50.0F, reps = 10, date = date),
                    ExecutionEntity(exerciseDetailsId = 1L, weight = 55.0F, reps = 8, date = date),
                    ExecutionEntity(exerciseDetailsId = 2L, weight = 60.0F, reps = 6, date = date),
                )

            coEvery { workoutDao.saveWorkoutExecution(any()) } returns Unit

            workoutRepository.saveWorkoutExecution(doneExercises, date)

            coVerify { workoutDao.saveWorkoutExecution(expectedExecutionEntities) }
        }

    @Test
    fun testSaveWorkoutExecution_EmptyExercisesListDoesNotSave() =
        runTest {
            val date = 123456789L
            val doneExercises = emptyList<WorkoutExercise>()

            coEvery { workoutDao.saveWorkoutExecution(any()) } returns Unit
            workoutRepository.saveWorkoutExecution(doneExercises, date)

            coVerify(exactly = 0) { workoutDao.saveWorkoutExecution(any()) }
        }

    @Test
    fun testUpdateWorkout_DeletesOldEntities() =
        runTest {
            val workoutId = 1L
            val currentWorkoutEntities =
                listOf(
                    WorkoutEntity(
                        workoutDetailsId = workoutId,
                        exerciseDetailsId = 1L,
                        sets = 3,
                        order = 1,
                    ),
                    WorkoutEntity(
                        workoutDetailsId = workoutId,
                        exerciseDetailsId = 2L,
                        sets = 2,
                        order = 2,
                    ),
                )
            val newWorkout =
                Workout(
                    id = workoutId,
                    name = "Updated Workout",
                    category = "",
                    note = "",
                    exercises =
                        mutableStateListOf(
                            WorkoutExercise(
                                id = 3L,
                                name = "New Exercise",
                                order = 3,
                                setCount = 4,
                                note = "",
                                category = "",
                                sets = mutableStateListOf(),
                            ),
                        ),
                )

            coEvery { workoutDao.getExercisesFor(workoutId) } returns
                flow { emit(currentWorkoutEntities) }
            coEvery { workoutDao.deleteWorkoutEntities(currentWorkoutEntities) } returns Unit
            coEvery { workoutDao.insertWorkoutEntities(any()) } returns Unit

            workoutRepository.updateWorkout(newWorkout)

            coVerify { workoutDao.deleteWorkoutEntities(currentWorkoutEntities) }
        }

    @Test
    fun testUpdateWorkout_InsertsNewEntities() =
        runTest {
            val workoutId = 1L
            val currentWorkoutEntities = emptyList<WorkoutEntity>()
            val newWorkout =
                Workout(
                    id = workoutId,
                    name = "New Workout",
                    category = "",
                    note = "",
                    exercises =
                        mutableStateListOf(
                            WorkoutExercise(
                                id = 1L,
                                name = "Exercise 1",
                                order = 1,
                                setCount = 3,
                                note = "",
                                category = "",
                                sets = mutableStateListOf(),
                            ),
                            WorkoutExercise(
                                id = 2L,
                                name = "Exercise 2",
                                order = 2,
                                setCount = 4,
                                note = "",
                                category = "",
                                sets = mutableStateListOf(),
                            ),
                        ),
                )
            val newWorkoutEntities =
                newWorkout.exercises.map { exercise ->
                    WorkoutEntity(
                        workoutDetailsId = workoutId,
                        exerciseDetailsId = exercise.id,
                        sets = exercise.setCount,
                        order = exercise.order,
                    )
                }

            coEvery { workoutDao.getExercisesFor(workoutId) } returns
                flow { emit(currentWorkoutEntities) }
            coEvery { workoutDao.insertWorkoutEntities(newWorkoutEntities) } returns Unit

            workoutRepository.updateWorkout(newWorkout)

            coVerify { workoutDao.insertWorkoutEntities(newWorkoutEntities) }
        }

    @Test
    fun testUpdateWorkout_UpdatesExistingEntities() =
        runTest {
            val workoutId = 1L
            val currentWorkoutEntities =
                listOf(
                    WorkoutEntity(
                        workoutDetailsId = workoutId,
                        exerciseDetailsId = 1L,
                        sets = 3,
                        order = 1,
                    ),
                )
            val newWorkout =
                Workout(
                    id = workoutId,
                    name = "Updated Workout",
                    category = "",
                    note = "",
                    exercises =
                        mutableStateListOf(
                            WorkoutExercise(
                                id = 1L,
                                name = "Exercise 1",
                                order = 1,
                                setCount = 4,
                                note = "",
                                category = "",
                                sets = mutableStateListOf(),
                            ),
                        ),
                )
            val updatedWorkoutEntities =
                newWorkout.exercises.map { exercise ->
                    WorkoutEntity(
                        workoutDetailsId = workoutId,
                        exerciseDetailsId = exercise.id,
                        sets = exercise.setCount,
                        order = exercise.order,
                    )
                }

            coEvery { workoutDao.getExercisesFor(workoutId) } returns
                flow { emit(currentWorkoutEntities) }
            coEvery { workoutDao.updateWorkoutEntities(updatedWorkoutEntities) } returns Unit

            workoutRepository.updateWorkout(newWorkout)

            coVerify { workoutDao.updateWorkoutEntities(updatedWorkoutEntities) }
        }

    @Test
    fun testCreateNewWorkout_InsertsWorkoutAndExercises() =
        runTest {
            val workoutId = 1L
            val newWorkout =
                Workout(
                    id = workoutId,
                    name = "New Workout",
                    category = "Strength",
                    note = "Workout Note",
                    exercises =
                        mutableStateListOf(
                            WorkoutExercise(
                                id = 0L,
                                name = "Exercise 1",
                                order = 1,
                                setCount = 3,
                                note = "Exercise Note 1",
                                category = "Strength",
                                sets = mutableStateListOf(),
                            ),
                            WorkoutExercise(
                                id = 0L,
                                name = "Exercise 2",
                                order = 2,
                                setCount = 4,
                                note = "Exercise Note 2",
                                category = "Cardio",
                                sets = mutableStateListOf(),
                            ),
                        ),
                )

            coEvery { workoutDao.insertWorkoutDetails(any()) } returns workoutId
            coEvery { workoutDao.insertExerciseDetails(any()) } returnsMany listOf(1L, 2L)
            coEvery { workoutDao.insertWorkoutEntity(any()) } returns Unit

            workoutRepository.createNewWorkout(newWorkout)

            coVerify {
                workoutDao.insertWorkoutDetails(
                    WorkoutDetailsEntity(
                        workoutName = newWorkout.name,
                        category = newWorkout.category,
                        note = newWorkout.note,
                    ),
                )
            }

            coVerify {
                workoutDao.insertExerciseDetails(
                    ExerciseDetailsEntity(
                        exerciseName = "Exercise 1",
                        note = "Exercise Note 1",
                        category = "Strength",
                    ),
                )
            }
            coVerify {
                workoutDao.insertExerciseDetails(
                    ExerciseDetailsEntity(
                        exerciseName = "Exercise 2",
                        note = "Exercise Note 2",
                        category = "Cardio",
                    ),
                )
            }

            coVerify {
                workoutDao.insertWorkoutEntity(
                    WorkoutEntity(
                        workoutDetailsId = workoutId,
                        exerciseDetailsId = 1L,
                        sets = 3,
                        order = 1,
                    ),
                )
            }
            coVerify {
                workoutDao.insertWorkoutEntity(
                    WorkoutEntity(
                        workoutDetailsId = workoutId,
                        exerciseDetailsId = 2L,
                        sets = 4,
                        order = 2,
                    ),
                )
            }
        }

    @Test
    fun testCreateNewWorkout_WithNoExercises() =
        runTest {
            val workoutId = 1L
            val newWorkout =
                Workout(
                    id = workoutId,
                    name = "Workout Without Exercises",
                    category = "Flexibility",
                    note = "This workout has no exercises",
                    exercises = mutableStateListOf(),
                )

            coEvery { workoutDao.insertWorkoutDetails(any()) } returns workoutId

            workoutRepository.createNewWorkout(newWorkout)

            coVerify {
                workoutDao.insertWorkoutDetails(
                    WorkoutDetailsEntity(
                        workoutName = newWorkout.name,
                        category = newWorkout.category,
                        note = newWorkout.note,
                    ),
                )
            }

            coVerify(exactly = 0) { workoutDao.insertExerciseDetails(any()) }
            coVerify(exactly = 0) { workoutDao.insertWorkoutEntity(any()) }
        }
}
