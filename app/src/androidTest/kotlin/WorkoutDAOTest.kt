import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.gymbuddy.data.localdatabase.ExecutionEntity
import com.example.gymbuddy.data.localdatabase.ExerciseDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutDAO
import com.example.gymbuddy.data.localdatabase.WorkoutDatabase
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Instant

class WorkoutDAOTest {
    private lateinit var workoutDao: WorkoutDAO
    private lateinit var workoutDatabase: WorkoutDatabase

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        workoutDatabase =
            Room
                .inMemoryDatabaseBuilder(
                    context,
                    WorkoutDatabase::class.java,
                ).allowMainThreadQueries()
                .build()
        workoutDao = workoutDatabase.workoutDao()
    }

    @After
    fun closeDb() {
        workoutDatabase.close()
    }

    /*
     *       Insert Tests
     */

    @Test
    fun insertWorkoutExecutionListToDatabase() =
        runBlocking {
            val exerciseDetails = getExerciseDetails()

            val workoutExecutions =
                listOf(
                    ExecutionEntity(
                        id = 1,
                        weight = 20F,
                        date = Instant.now().toEpochMilli(),
                        reps = 10,
                        exerciseDetailsId = 1,
                    ),
                    ExecutionEntity(
                        id = 2,
                        weight = 20F,
                        date = Instant.now().toEpochMilli(),
                        reps = 10,
                        exerciseDetailsId = 1,
                    ),
                )

            workoutDao.insertExerciseDetails(exerciseDetails)
            workoutDao.saveWorkoutExecution(workoutExecutions)

            val savedExecutions: List<ExecutionEntity> =
                workoutDao.getExecutionsFor(1).first()

            assert(savedExecutions.size == 2)
            assert(savedExecutions.containsAll(workoutExecutions))
        }

    @Test
    fun insertWorkoutListToDatabase() =
        runBlocking {
            val workoutDetails = getWorkoutDetails()
            val exerciseDetails = getExerciseDetails()

            val workout =
                listOf(getWorkout(1), getWorkout(2))

            workoutDao.insertWorkoutDetails(workoutDetails)
            workoutDao.insertExerciseDetails(exerciseDetails)
            workoutDao.insertWorkoutEntities(workout)

            val savedWorkout: List<WorkoutEntity> =
                workoutDao.getWorkoutFor(1).first()

            assert(savedWorkout.size == 2)
            assert(savedWorkout.containsAll(workout))
        }

    @Test
    fun insertWorkoutDetailsToDatabase() =
        runBlocking {
            val workoutDetails = getWorkoutDetails()

            val savedWorkoutDetailsId: Long = workoutDao.insertWorkoutDetails(workoutDetails)
            val savedWorkoutDetails: WorkoutDetailsEntity? =
                workoutDao.getWorkoutDetailsFor(1L).first()

            assert(savedWorkoutDetails == workoutDetails)
            assert(savedWorkoutDetailsId == 1L)
        }

    @Test
    fun insertWorkoutToDatabase() =
        runBlocking {
            val workoutDetails = getWorkoutDetails()
            val exerciseDetails = getExerciseDetails()
            val workout = getWorkout()

            workoutDao.insertWorkoutDetails(workoutDetails)
            workoutDao.insertExerciseDetails(exerciseDetails)
            workoutDao.insertWorkoutEntity(workout)

            val savedWorkout: List<WorkoutEntity> =
                workoutDao.getWorkoutFor(1).first()

            assert(savedWorkout.size == 1)
            assert(savedWorkout.contains(workout))
        }

    @Test
    fun insertExerciseDetailsToDatabase() =
        runBlocking {
            val exerciseDetails = getExerciseDetails()
            workoutDao.insertExerciseDetails(exerciseDetails)

            val savedExerciseDetails: ExerciseDetailsEntity? =
                workoutDao.getExerciseDetailsFor(1L).first()

            assert(savedExerciseDetails == exerciseDetails)
            assert(savedExerciseDetails?.id == 1L)
        }

    /*
     *       Update Tests
     */

    @Test
    fun updateWorkoutsToDatabase() =
        runBlocking {
            val workoutDetails = getWorkoutDetails()
            val exerciseDetails = getExerciseDetails()

            val workout =
                listOf(getWorkout(1), getWorkout(2))

            workoutDao.insertWorkoutDetails(workoutDetails)
            workoutDao.insertExerciseDetails(exerciseDetails)
            workoutDao.insertWorkoutEntities(workout)

            val updatedWorkout =
                listOf(getWorkout(id = 1L, sets = 4), getWorkout(id = 2L, sets = 2))

            workoutDao.updateWorkoutEntities(updatedWorkout)

            val savedWorkout: List<WorkoutEntity> =
                workoutDao.getWorkoutFor(1).first()

            assert(savedWorkout.size == 2)
            assert(savedWorkout.containsAll(updatedWorkout))
        }

    /*
     *       Delete Tests
     */

    @Test
    fun deleteWorkoutsFromDatabase() =
        runBlocking {
            val workoutDetails = getWorkoutDetails()
            val exerciseDetails = getExerciseDetails()

            val workout =
                listOf(getWorkout(1), getWorkout(2))

            workoutDao.insertWorkoutDetails(workoutDetails)
            workoutDao.insertExerciseDetails(exerciseDetails)
            workoutDao.insertWorkoutEntities(workout)

            workoutDao.deleteWorkoutEntities(listOf(workout[0]))

            val savedWorkout: List<WorkoutEntity> =
                workoutDao.getWorkoutFor(1).first()

            assert(savedWorkout.size == 1)
            assert(savedWorkout.contains(workout[1]))
        }

    /*
     *       Special Query Tests
     */

    @Test
    fun getAllWorkoutDetailsFromDatabase() =
        runBlocking {
            val workoutDetails =
                listOf(
                    getWorkoutDetails(id = 1, name = "Test Workout 1"),
                    getWorkoutDetails(id = 2, name = "Test Workout 2"),
                    getWorkoutDetails(id = 3, name = "Test Workout 3"),
                )

            for (workoutDetail in workoutDetails) {
                workoutDao.insertWorkoutDetails(workoutDetail)
            }

            val savedWorkoutDetails: List<WorkoutDetailsEntity> =
                workoutDao.getAllWorkoutDetails().first()

            assert(savedWorkoutDetails.size == 3)
            assert(savedWorkoutDetails.containsAll(workoutDetails))
        }

    @Test
    fun getAllExerciseForWorkoutFromDatabase() =
        runBlocking {
            val workoutDetails = getWorkoutDetails()
            val exerciseDetails = getExerciseDetails()

            val workout =
                listOf(getWorkout(1), getWorkout(2))

            workoutDao.insertWorkoutDetails(workoutDetails)
            workoutDao.insertExerciseDetails(exerciseDetails)
            workoutDao.insertWorkoutEntities(workout)

            val savedWorkout: List<WorkoutEntity> =
                workoutDao.getExercisesFor(1).first()

            assert(savedWorkout.size == 2)
            assert(savedWorkout.containsAll(workout))
        }
}