import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before

class LocalDataRepositoryTest {
    @MockK
    private lateinit var workoutDao: WorkoutDAO

    private lateinit var workoutRepository: WorkoutRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        workoutRepository = LocalDataRepository(workoutDao)
    }

    /* Implementierungs Beispiel
    @Test
    fun `test fetching workouts returns expected data`() {
        // Arrange: Definiere das Verhalten des Mock-DAOs
        val dummyWorkouts = listOf(
            Workout(id = 1, name = "Workout A", exercises = emptyList()),
            Workout(id = 2, name = "Workout B", exercises = emptyList())
        )
        every { workoutDao.getAllWorkouts() } returns flowOf(dummyWorkouts)

        // Act: Rufe die Methode im Repository auf
        val result = workoutRepository.getAllWorkouts().first()

        // Assert: Überprüfe das erwartete Verhalten
        assertEquals(dummyWorkouts, result)
        verify { workoutDao.getAllWorkouts() }
    }
     */
}
