package localDataRepositoryTests

import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteWorkoutTests {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }

    @Test
    fun testDeleteWorkout_DeletesWorkoutDetailsById() =
        runTest {
            val workoutId = 1L

            coEvery { workoutDao.deleteExerciseDetailsById(any()) } returns Unit

            workoutRepository.deleteExercise(workoutId)

            coVerify {
                workoutDao.deleteExerciseDetailsById(workoutId)
            }
        }

}