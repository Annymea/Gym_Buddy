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
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteExerciseTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }

    @Test
    fun testDeleteExercise_DeletesExerciseDetailsById() =
        runTest {
            val exerciseId = 1L

            coEvery { workoutDao.deleteExerciseDetailsById(any()) } returns Unit

            workoutRepository.deleteExercise(exerciseId)

            coVerify {
                workoutDao.deleteExerciseDetailsById(exerciseId)
            }
        }

}