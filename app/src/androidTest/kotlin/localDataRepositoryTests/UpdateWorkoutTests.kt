package localDataRepositoryTests

import androidx.compose.runtime.mutableStateListOf
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import com.example.gymbuddy.data.localdatabase.WorkoutEntity
import getExerciseDetails
import getWorkout
import getWorkoutDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateWorkoutTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
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
}