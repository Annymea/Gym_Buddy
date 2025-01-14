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
    fun getAllWorkoutDetailsFromDatabaseinCorrectOrder() =
        runBlocking {
            val workoutDetails =
                listOf(
                    getWorkoutDetails(id = 2, name = "Test Workout 2"),
                    getWorkoutDetails(id = 1, name = "Test Workout 1"),
                    getWorkoutDetails(id = 3, name = "Test Workout 3"),
                )

            workoutDetails[0].overviewOrder = 0
            workoutDetails[1].overviewOrder = 1
            workoutDetails[2].overviewOrder = 2

            for (workoutDetail in workoutDetails) {
                workoutDao.insertWorkoutDetails(workoutDetail)
            }

            val savedWorkoutDetails: List<WorkoutDetailsEntity> =
                workoutDao.getAllWorkoutDetails().first()

            assert(savedWorkoutDetails.size == 3)
            assert(savedWorkoutDetails.containsAll(workoutDetails))

            assert(savedWorkoutDetails[0].workoutName == "Test Workout 2")
            assert(savedWorkoutDetails[1].workoutName == "Test Workout 1")
            assert(savedWorkoutDetails[2].workoutName == "Test Workout 3")
        }

    @Test
    fun getMaxIndexForWorkouts() =
        runBlocking {
            val workoutDetails =
                listOf(
                    getWorkoutDetails(id = 2, name = "Test Workout 2"),
                    getWorkoutDetails(id = 1, name = "Test Workout 1"),
                    getWorkoutDetails(id = 3, name = "Test Workout 3"),
                )

            workoutDetails[0].overviewOrder = 0
            workoutDetails[1].overviewOrder = 1
            workoutDetails[2].overviewOrder = 2

            for (workoutDetail in workoutDetails) {
                workoutDao.insertWorkoutDetails(workoutDetail)
            }

            val maxIndex = workoutDao.getMaxWorkoutIndex()

            assert(maxIndex == 2)
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

    @Test
    fun getAllDatesOfExecutionsForTimeSpan() =
        runBlocking {
            val startDate = Instant.now().minusSeconds(60 * 60 * 24 * 10).toEpochMilli()
            val endDate = Instant.now().toEpochMilli()

            val executionEntities =
                listOf(
                    ExecutionEntity(
                        id = 1,
                        weight = 20F,
                        date = startDate + 1000,
                        reps = 10,
                        exerciseDetailsId = 1,
                    ),
                    ExecutionEntity(
                        id = 2,
                        weight = 25F,
                        date = startDate + (60 * 60 * 24 * 2 * 1000),
                        reps = 12,
                        exerciseDetailsId = 1,
                    ),
                    ExecutionEntity(
                        id = 3,
                        weight = 30F,
                        date = endDate - 1000,
                        reps = 15,
                        exerciseDetailsId = 2,
                    ),
                    ExecutionEntity(
                        id = 4,
                        weight = 20F,
                        date = startDate - (60 * 60 * 24 * 5 * 1000),
                        reps = 8,
                        exerciseDetailsId = 2,
                    ),
                )

            workoutDao.insertExerciseDetails(getExerciseDetails(id = 1L))
            workoutDao.insertExerciseDetails(getExerciseDetails(id = 2L))
            workoutDao.saveWorkoutExecution(executionEntities)

            val dates = workoutDao.getAllDatesOfExecutionsForTimeSpan(startDate, endDate).first()

            assert(dates.size == 3)
            assert(dates.contains(executionEntities[0].date))
            assert(dates.contains(executionEntities[1].date))
            assert(dates.contains(executionEntities[2].date))
            assert(!dates.contains(executionEntities[3].date))
        }

    @Test
    fun getLatestExecutionForExercise() =
        runBlocking {
            val exerciseId = 1L
            val exerciseDetails = getExerciseDetails(id = exerciseId)

            val execution1 =
                ExecutionEntity(
                    id = 1,
                    weight = 20F,
                    date = Instant.now().minusSeconds(60 * 60 * 24).toEpochMilli(),
                    reps = 10,
                    exerciseDetailsId = exerciseId,
                )
            val execution2 =
                ExecutionEntity(
                    id = 2,
                    weight = 25F,
                    date = Instant.now().toEpochMilli(),
                    reps = 12,
                    exerciseDetailsId = exerciseId,
                )

            workoutDao.insertExerciseDetails(exerciseDetails)
            workoutDao.saveWorkoutExecution(listOf(execution1, execution2))

            val latestExecutions = workoutDao.getLatestExecutionsFor(exerciseId).first()

            assert(latestExecutions.size == 1)
            assert(latestExecutions[0] == execution2)
        }

    @Test
    fun getLatestExecutionForExercise_returnsEmptyListWhenNoEntriesExist() =
        runBlocking {
            val exerciseId = 1L

            val latestExecutions = workoutDao.getLatestExecutionsFor(exerciseId).first()

            assert(latestExecutions.isEmpty())
        }

    @Test
    fun getAllExerciseDetails_ReturnsAllInsertedExercises() =
        runBlocking {
            val exerciseDetails =
                listOf(
                    getExerciseDetails(id = 1L).copy(exerciseName = "Push-Up"),
                    getExerciseDetails(id = 2L).copy(exerciseName = "Pull-Up"),
                )

            for (exercise in exerciseDetails) {
                workoutDao.insertExerciseDetails(exercise)
            }

            val savedExerciseDetails = workoutDao.getAllExerciseDetails().first()

            assert(savedExerciseDetails.size == 2)
            assert(savedExerciseDetails.containsAll(exerciseDetails))
        }

    @Test
    fun getAllExerciseDetails_ReturnsEmptyListWhenNoDataExists() =
        runBlocking {
            val savedExerciseDetails = workoutDao.getAllExerciseDetails().first()

            assert(savedExerciseDetails.isEmpty())
        }

    @Test
    fun deleteExerciseDetailsById_RemovesCorrectEntry() =
        runBlocking {
            val exercise1 = getExerciseDetails(id = 1L).copy(exerciseName = "Push-Up")
            val exercise2 = getExerciseDetails(id = 2L).copy(exerciseName = "Pull-Up")

            workoutDao.insertExerciseDetails(exercise1)
            workoutDao.insertExerciseDetails(exercise2)

            workoutDao.deleteExerciseDetailsById(1L)

            val savedExerciseDetails = workoutDao.getAllExerciseDetails().first()

            assert(savedExerciseDetails.size == 1)
            assert(savedExerciseDetails[0] == exercise2)
        }

    @Test
    fun deleteWorkoutDetailsById_RemovesCorrectEntry() =
        runBlocking {
            val workout1 = getWorkoutDetails(id = 1L, name = "Test Workout 1")
            val workout2 = getWorkoutDetails(id = 2L, name = "Test Workout 2")

            workoutDao.insertWorkoutDetails(workout1)
            workoutDao.insertWorkoutDetails(workout2)

            workoutDao.deleteWorkoutDetailsById(1L)

            val savedWorkoutDetails = workoutDao.getAllWorkoutDetails().first()

            assert(savedWorkoutDetails.size == 1)
            assert(savedWorkoutDetails[0] == workout2)
        }

    @Test
    fun deleteExerciseDetailsById_DoesNotFailWhenIdDoesNotExist() =
        runBlocking {
            val exercise = getExerciseDetails(id = 1L).copy(exerciseName = "Push-Up")

            workoutDao.insertExerciseDetails(exercise)

            workoutDao.deleteExerciseDetailsById(2L)

            val savedExerciseDetails = workoutDao.getAllExerciseDetails().first()

            assert(savedExerciseDetails.size == 1)
            assert(savedExerciseDetails[0] == exercise)
        }

    @Test
    fun updateWorkoutOverviewOrderIndex() =
        runBlocking {
            val workoutDetails =
                listOf(
                    getWorkoutDetails(id = 2L, name = "Test Workout 2"),
                    getWorkoutDetails(id = 1L, name = "Test Workout 1"),
                    getWorkoutDetails(id = 3L, name = "Test Workout 3"),
                )

            workoutDetails[0].overviewOrder = 0
            workoutDetails[1].overviewOrder = 1
            workoutDetails[2].overviewOrder = 2

            for (workoutDetail in workoutDetails) {
                workoutDao.insertWorkoutDetails(workoutDetail)
            }

            workoutDao.updateOrderValueForWorkout(1L, 0)
            workoutDao.updateOrderValueForWorkout(2L, 1)
            workoutDao.updateOrderValueForWorkout(3L, 2)

            val savedWorkoutDetails: List<WorkoutDetailsEntity> =
                workoutDao.getAllWorkoutDetails().first()

            assert(savedWorkoutDetails.size == 3)

            assert(savedWorkoutDetails[0].overviewOrder == 0)
            assert(savedWorkoutDetails[1].overviewOrder == 1)
            assert(savedWorkoutDetails[2].overviewOrder == 2)
        }
}
