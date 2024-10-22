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
            val savedWorkoutDetails: WorkoutDetailsEntity =
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

            val savedExerciseDetails: ExerciseDetailsEntity =
                workoutDao.getExerciseDetailsFor(1L).first()

            assert(savedExerciseDetails == exerciseDetails)
            assert(savedExerciseDetails.id == 1L)
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

    private fun getExerciseDetails(): ExerciseDetailsEntity =
        ExerciseDetailsEntity(
            exerciseName = "Test Exercise",
            id = 1,
            note = "",
            category = "",
        )

    private fun getWorkoutDetails(
        id: Long = 1L,
        name: String = "Test Workout",
    ): WorkoutDetailsEntity =
        WorkoutDetailsEntity(
            workoutName = name,
            id = id,
            category = "",
            note = "",
        )

    private fun getWorkout(
        id: Long = 1L,
        sets: Int = 3,
    ): WorkoutEntity =
        WorkoutEntity(
            id = id,
            workoutDetailsId = 1,
            exerciseDetailsId = 1,
            sets = sets,
            order = 1,
        )
}

// class CompleteWorkoutDTODaoTestEntity {

//
//    // Insert tests
//    @Test
//    fun insertOnePlanAndGetOnePlan() =
//        runBlocking {
//            val plan = getPlan()
//            workoutDao.insertPlan(plan)
//
//            val retrievedPlan = workoutDao.getAllPlanNames().first()
//            assert(retrievedPlan.contains(plan))
//            assert(retrievedPlan.size == 1)
//        }
//
//    @Test
//    fun insertMultiplePlansAndGetMultiplePlans() =
//        runBlocking {
//            val listOfPlans =
//                listOf(
//                    getPlan(id = 1, planName = "Test Plan 1"),
//                    getPlan(id = 2, planName = "Test Plan 2"),
//                    getPlan(id = 3, planName = "Test Plan 3"),
//                )
//            for (plan in listOfPlans) {
//                workoutDao.insertPlan(plan)
//            }
//
//            val retrievedPlan = workoutDao.getAllPlanNames().first()
//            for (plan in listOfPlans) {
//                assert(retrievedPlan.contains(plan))
//            }
//            assert(retrievedPlan.size == 3)
//        }
//
//    @Test
//    fun insertOneExerciseAndGetOneExercise() =
//        runBlocking {
//            val exercise = getExercise()
//            workoutDao.insertExercise(exercise)
//
//            val retrievedExercise = workoutDao.getAllExerciseNames().first()
//            assert(retrievedExercise.contains(exercise))
//            assert(retrievedExercise.size == 1)
//        }
//
//    @Test
//    fun insertMultipleExercisesAndGetMultipleExercises() =
//        runBlocking {
//            val listOfExercises =
//                listOf(
//                    getExercise(id = 1, exerciseName = "Test Exercise 1"),
//                    getExercise(id = 2, exerciseName = "Test Exercise 2"),
//                    getExercise(id = 3, exerciseName = "Test Exercise 3"),
//                )
//            for (exercise in listOfExercises) {
//                workoutDao.insertExercise(exercise)
//            }
//
//            val retrievedExercise = workoutDao.getAllExerciseNames().first()
//            for (exercise in listOfExercises) {
//                assert(retrievedExercise.contains(exercise))
//            }
//            assert(retrievedExercise.size == 3)
//        }
//
//    @Test
//    fun insertOneExecutablePlanAndGetOneExecutablePlanByPlanId() =
//        runBlocking {
//            val planId: Long = 1
//
//            val executablePlan = getExecutablePlan()
//
//            workoutDao.insertPlan(getPlan(id = planId))
//            workoutDao.insertExercise(getExercise())
//
//            workoutDao.insertExecutablePlan(executablePlan)
//
//            val retrievedExecutablePlan = workoutDao.getExecutablePlanById(planId).first()
//            assert(retrievedExecutablePlan.contains(executablePlan))
//            assert(retrievedExecutablePlan.size == 1)
//        }
//
//    @Test
//    fun insertMultipleExecutablePlansAndGetMultipleExecutablePlansByPlanId() =
//        runBlocking {
//            val planId: Long = 1
//
//            val listOfExecutablePlans =
//                listOf(
//                    getExecutablePlan(
//                        id = 1,
//                        planId = planId,
//                    ),
//                    getExecutablePlan(
//                        id = 2,
//                        planId = planId,
//                    ),
//                    getExecutablePlan(
//                        id = 3,
//                        planId = planId,
//                    ),
//                )
//            workoutDao.insertPlan(getPlan(id = planId))
//            workoutDao.insertExercise(getExercise(id = 1))
//            workoutDao.insertExercise(getExercise(id = 2))
//            workoutDao.insertExercise(getExercise(id = 3))
//
//            for (executablePlan in listOfExecutablePlans) {
//                workoutDao.insertExecutablePlan(executablePlan)
//            }
//
//            val retrievedExecutablePlan = workoutDao.getExecutablePlanById(planId).first()
//            for (executablePlan in listOfExecutablePlans) {
//                assert(retrievedExecutablePlan.contains(executablePlan))
//            }
//            assert(retrievedExecutablePlan.size == 3)
//        }
//
//    @Test
//    fun insertOneExecutionAndGetByExerciseId() =
//        runBlocking {
//            val exerciseId: Long = 1
//
//            val execution = getExecution()
//
//            workoutDao.insertExercise(
//                ExerciseDetailsEntity(
//                    id = exerciseId,
//                    exerciseName = "Test Exercise",
//                ),
//            )
//            workoutDao.insertExecution(getExecution())
//
//            val retrievedExecution = workoutDao.getExecutionsById(exerciseId).first()
//            assert(retrievedExecution.contains(execution))
//            assert(retrievedExecution.size == 1)
//        }
//
//    @Test
//    fun insertMultipleExecutionsAndGetByExerciseId() =
//        runBlocking {
//            val exerciseId: Long = 1
//
//            val listOfExecutions =
//                listOf(
//                    getExecution(id = 1, exerciseId = exerciseId),
//                    getExecution(id = 2, exerciseId = exerciseId),
//                    getExecution(id = 3, exerciseId = exerciseId),
//                )
//
//            workoutDao.insertExercise(getExercise())
//            for (execution in listOfExecutions) {
//                workoutDao.insertExecution(execution)
//            }
//
//            val retrievedExecution = workoutDao.getExecutionsById(exerciseId).first()
//            for (execution in listOfExecutions) {
//                assert(retrievedExecution.contains(execution))
//            }
//            assert(retrievedExecution.size == 3)
//        }
//
//    // Delete Tests
//    @Test
//    fun insertAndDeleteExercise() =
//        runBlocking {
//            val exercise = ExerciseDetailsEntity(id = 1, exerciseName = "Push-up")
//            workoutDao.insertExercise(exercise)
//            workoutDao.deleteExercise(exercise)
//
//            val allExercises = workoutDao.getAllExerciseNames().first()
//            assertTrue(allExercises.isEmpty())
//        }
//
//    @Test
//    fun deletePlan_removesCorrectPlan() =
//        runBlocking {
//            val plan = WorkoutDetailsEntity(id = 1, workoutName = "Test Plan")
//            workoutDao.insertPlan(plan)
//
//            var allPlans = workoutDao.getAllPlanNames().first()
//            assertEquals(1, allPlans.size)
//
//            workoutDao.deletePlan(plan)
//
//            allPlans = workoutDao.getAllPlanNames().first()
//            assertTrue(allPlans.isEmpty())
//        }
//
//    @Test
//    fun deleteExercise_removesCorrectExercise() =
//        runBlocking {
//            val exercise = ExerciseDetailsEntity(id = 1, exerciseName = "Push-up")
//            workoutDao.insertExercise(exercise)
//
//            var allExercises = workoutDao.getAllExerciseNames().first()
//            assertEquals(1, allExercises.size)
//
//            workoutDao.deleteExercise(exercise)
//
//            allExercises = workoutDao.getAllExerciseNames().first()
//            assertTrue(allExercises.isEmpty())
//        }
//
//    @Test
//    fun deleteExecutablePlan_removesCorrectExecutablePlan() =
//        runBlocking {
//            workoutDao.insertPlan(getPlan())
//            workoutDao.insertExercise(getExercise())
//
//            val executablePlan =
//                WorkoutEntity(
//                    id = 1,
//                    workoutDetailsId = 1,
//                    exerciseDetailsId = 1,
//                    sets = 3,
//                    order = 1,
//                )
//            workoutDao.insertExecutablePlan(executablePlan)
//
//            var allExecutablePlans = workoutDao.getExecutablePlanById(1).first()
//            assertEquals(1, allExecutablePlans.size)
//
//            workoutDao.deleteExerciseFromPlan(executablePlan)
//
//            allExecutablePlans = workoutDao.getExecutablePlanById(1).first()
//            assertTrue(allExecutablePlans.isEmpty())
//        }
//
//    @Test
//    fun deleteNonExistentPlan_doesNothing() =
//        runBlocking {
//            val plan = WorkoutDetailsEntity(id = 1, workoutName = "Non-existent Plan")
//
//            workoutDao.deletePlan(plan)
//
//            val allPlans = workoutDao.getAllPlanNames().first()
//            assertTrue(allPlans.isEmpty())
//        }
//
//    @Test
//    fun deletePlan_cascadesDeleteToExecutablePlans() =
//        runBlocking {
//            val plan = WorkoutDetailsEntity(id = 1, workoutName = "Test Plan")
//            workoutDao.insertPlan(plan)
//
//            val exercise = ExerciseDetailsEntity(id = 1, exerciseName = "Push-up")
//            workoutDao.insertExercise(exercise)
//
//            val executablePlan =
//                WorkoutEntity(
//                    id = 1,
//                    workoutDetailsId = plan.id,
//                    exerciseDetailsId = exercise.id,
//                    sets = 3,
//                    order = 1,
//                )
//            workoutDao.insertExecutablePlan(executablePlan)
//
//            workoutDao.deletePlan(plan)
//
//            val allExecutablePlans = workoutDao.getExecutablePlanById(plan.id).first()
//            assertTrue(allExecutablePlans.isEmpty())
//        }
//
//    @Test
//    fun deleteNonExistentExercise_doesNothing() =
//        runBlocking {
//            val exercise = ExerciseDetailsEntity(id = 1, exerciseName = "Non-existent Exercise")
//
//            workoutDao.deleteExercise(exercise)
//
//            val allExercises = workoutDao.getAllExerciseNames().first()
//            assertTrue(allExercises.isEmpty())
//        }
//
//    private fun getPlan(
//        id: Long = 1,
//        planName: String = "Test Plan",
//    ): WorkoutDetailsEntity = WorkoutDetailsEntity(id, planName)
//
//    private fun getExercise(
//        id: Long = 1,
//        exerciseName: String = "Test Exercise",
//    ): ExerciseDetailsEntity = ExerciseDetailsEntity(id, exerciseName)
//
//    private fun getExecutablePlan(
//        id: Long = 1,
//        planId: Long = 1,
//        exerciseId: Long = 1,
//        sets: Int = 3,
//        order: Int = 1,
//    ): WorkoutEntity = WorkoutEntity(id, planId, exerciseId, sets, order)
//
//    private fun getExecution(
//        id: Long = 1,
//        exerciseId: Long = 1,
//        reps: Int = 10,
//        weight: Int = 200,
//        date: Long = LocalDate.now().toEpochDay(),
//    ): ExecutionEntity = ExecutionEntity(id, exerciseId, weight, reps, date)
// }
//
