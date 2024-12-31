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

class UpdateWorkoutOrder {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }

    @Test
    fun testUpdateWorkoutOrder_UpdatesOrderCorrectly() = runTest {
        val workoutIds = listOf(101L, 102L, 103L)

        coEvery { workoutDao.updateOrderValueForWorkout(any(), any()) } returns Unit

        workoutRepository.updateWorkoutOrder(workoutIds)

        workoutIds.forEachIndexed { index, id ->
            coVerify { workoutDao.updateOrderValueForWorkout(id, index) }
        }
    }

    @Test
    fun testUpdateWorkoutOrder_WithEmptyList() = runTest {
        val workoutIds = emptyList<Long>()

        workoutRepository.updateWorkoutOrder(workoutIds)

        coVerify(exactly = 0) { workoutDao.updateOrderValueForWorkout(any(), any()) }
    }
}