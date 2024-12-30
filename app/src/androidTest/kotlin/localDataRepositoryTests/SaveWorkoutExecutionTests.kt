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
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveWorkoutExecutionTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
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

}