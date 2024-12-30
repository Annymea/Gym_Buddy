package localDataRepositoryTests

import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.ExerciseDetailsEntity
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateNewWorkoutTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
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
            coEvery { workoutDao.getMaxWorkoutIndex() } returns 0

            workoutRepository.createNewWorkout(newWorkout)

            coVerify {
                workoutDao.insertWorkoutDetails(
                    WorkoutDetailsEntity(
                        workoutName = newWorkout.name,
                        category = newWorkout.category,
                        note = newWorkout.note,
                        overviewOrder = 1,
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
            coEvery { workoutDao.getMaxWorkoutIndex() } returns 0

            workoutRepository.createNewWorkout(newWorkout)

            coVerify {
                workoutDao.insertWorkoutDetails(
                    WorkoutDetailsEntity(
                        workoutName = newWorkout.name,
                        category = newWorkout.category,
                        note = newWorkout.note,
                        overviewOrder = 1,
                    ),
                )
            }

            coVerify(exactly = 0) { workoutDao.insertExerciseDetails(any()) }
            coVerify(exactly = 0) { workoutDao.insertWorkoutEntity(any()) }
        }
}

