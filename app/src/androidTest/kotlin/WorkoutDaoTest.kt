import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.GymBuddy.data.localdatabase.ExecutablePlan
import com.example.GymBuddy.data.localdatabase.Execution
import com.example.GymBuddy.data.localdatabase.Exercise
import com.example.GymBuddy.data.localdatabase.Plan
import com.example.GymBuddy.data.localdatabase.WorkoutDAO
import com.example.GymBuddy.data.localdatabase.WorkoutDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class WorkoutDaoTest {
    private lateinit var workoutDao: WorkoutDAO
    private lateinit var workoutDatabase: WorkoutDatabase

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        workoutDatabase = Room.inMemoryDatabaseBuilder(
            context,
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        workoutDao = workoutDatabase.workoutDao()
    }

    @After
    fun closeDb() {
        workoutDatabase.close()
    }

    //Insert tests
    @Test
    fun insertOnePlanAndGetOnePlan() = runBlocking {
        val plan = getPlan()
        workoutDao.insertPlan(plan)

        val retrievedPlan = workoutDao.getAllPlanNames().first()
        assert(retrievedPlan.contains(plan))
        assert(retrievedPlan.size == 1)
    }

    @Test
    fun insertMultiplePlansAndGetMultiplePlans() = runBlocking {
        val listOfPlans = listOf(
            getPlan(id = 1, planName = "Test Plan 1"),
            getPlan(id = 2, planName = "Test Plan 2"),
            getPlan(id = 3, planName = "Test Plan 3")
        )
        for (plan in listOfPlans) {
            workoutDao.insertPlan(plan)
        }

        val retrievedPlan = workoutDao.getAllPlanNames().first()
        for (plan in listOfPlans) {
            assert(retrievedPlan.contains(plan))
        }
        assert(retrievedPlan.size == 3)
    }

    @Test
    fun insertOneExerciseAndGetOneExercise() = runBlocking {
        val exercise = getExercise()
        workoutDao.insertExercise(exercise)

        val retrievedExercise = workoutDao.getAllExerciseNames().first()
        assert(retrievedExercise.contains(exercise))
        assert(retrievedExercise.size == 1)
    }

    @Test
    fun insertMultipleExercisesAndGetMultipleExercises() = runBlocking {
        val listOfExercises = listOf(
            getExercise(id = 1, exerciseName = "Test Exercise 1"),
            getExercise(id = 2, exerciseName = "Test Exercise 2"),
            getExercise(id = 3, exerciseName = "Test Exercise 3")
        )
        for (exercise in listOfExercises) {
            workoutDao.insertExercise(exercise)
        }

        val retrievedExercise = workoutDao.getAllExerciseNames().first()
        for (exercise in listOfExercises) {
            assert(retrievedExercise.contains(exercise))
        }
        assert(retrievedExercise.size == 3)
    }

    @Test
    fun insertOneExecutablePlanAndGetOneExecutablePlanByPlanId() = runBlocking {
        val planId: Long = 1

        val executablePlan = getExecutablePlan()

        workoutDao.insertPlan(getPlan(id = planId))
        workoutDao.insertExercise(getExercise())

        workoutDao.insertExecutablePlan(executablePlan)

        val retrievedExecutablePlan = workoutDao.getExecutablePlanById(planId).first()
        assert(retrievedExecutablePlan.contains(executablePlan))
        assert(retrievedExecutablePlan.size == 1)
    }

    @Test
    fun insertMultipleExecutablePlansAndGetMultipleExecutablePlansByPlanId() = runBlocking {
        val planId: Long = 1

        val listOfExecutablePlans = listOf(
            getExecutablePlan(
                id = 1,
                planId = planId,
            ),
            getExecutablePlan(
                id = 2,
                planId = planId,
            ),
            getExecutablePlan(
                id = 3,
                planId = planId,
            )
        )
        workoutDao.insertPlan(getPlan(id = planId))
        workoutDao.insertExercise(getExercise(id = 1))
        workoutDao.insertExercise(getExercise(id = 2))
        workoutDao.insertExercise(getExercise(id = 3))

        for (executablePlan in listOfExecutablePlans) {
            workoutDao.insertExecutablePlan(executablePlan)
        }

        val retrievedExecutablePlan = workoutDao.getExecutablePlanById(planId).first()
        for (executablePlan in listOfExecutablePlans) {
            assert(retrievedExecutablePlan.contains(executablePlan))
        }
        assert(retrievedExecutablePlan.size == 3)

    }

    @Test
    fun insertOneExecutionAndGetByExerciseId() = runBlocking {
        val exerciseId: Long = 1

        val execution = getExecution()

        workoutDao.insertExercise(Exercise(id = exerciseId, exerciseName = "Test Exercise"))
        workoutDao.insertExecution(getExecution())

        val retrievedExecution = workoutDao.getExecutionsById(exerciseId).first()
        assert(retrievedExecution.contains(execution))
        assert(retrievedExecution.size == 1)
    }


    @Test
    fun insertMultipleExecutionsAndGetByExerciseId() = runBlocking {
        val exerciseId: Long = 1

        val listOfExecutions = listOf(
            getExecution(id = 1, exerciseId = exerciseId),
            getExecution(id = 2, exerciseId = exerciseId),
            getExecution(id = 3, exerciseId = exerciseId)
        )

        workoutDao.insertExercise(getExercise())
        for (execution in listOfExecutions) {
            workoutDao.insertExecution(execution)
        }

        val retrievedExecution = workoutDao.getExecutionsById(exerciseId).first()
        for (execution in listOfExecutions) {
            assert(retrievedExecution.contains(execution))
        }
        assert(retrievedExecution.size == 3)
    }

    //Delete Tests
    @Test
    fun insertAndDeleteExercise() = runBlocking {
        val exercise = Exercise(id = 1, exerciseName = "Push-up")
        workoutDao.insertExercise(exercise)
        workoutDao.deleteExercise(exercise)

        val allExercises = workoutDao.getAllExerciseNames().first()
        assertTrue(allExercises.isEmpty())
    }

    @Test
    fun deletePlan_removesCorrectPlan() = runBlocking {
        val plan = Plan(id = 1, planName = "Test Plan")
        workoutDao.insertPlan(plan)

        var allPlans = workoutDao.getAllPlanNames().first()
        assertEquals(1, allPlans.size)

        workoutDao.deletePlan(plan)

        allPlans = workoutDao.getAllPlanNames().first()
        assertTrue(allPlans.isEmpty())
    }

    @Test
    fun deleteExercise_removesCorrectExercise() = runBlocking {
        val exercise = Exercise(id = 1, exerciseName = "Push-up")
        workoutDao.insertExercise(exercise)

        var allExercises = workoutDao.getAllExerciseNames().first()
        assertEquals(1, allExercises.size)

        workoutDao.deleteExercise(exercise)

        allExercises = workoutDao.getAllExerciseNames().first()
        assertTrue(allExercises.isEmpty())
    }

    @Test
    fun deleteExecutablePlan_removesCorrectExecutablePlan() = runBlocking {
        workoutDao.insertPlan(getPlan())
        workoutDao.insertExercise(getExercise())

        val executablePlan = ExecutablePlan(id = 1, planId = 1, exerciseId = 1, sets = 3, order = 1)
        workoutDao.insertExecutablePlan(executablePlan)

        var allExecutablePlans = workoutDao.getExecutablePlanById(1).first()
        assertEquals(1, allExecutablePlans.size)

        workoutDao.deleteExerciseFromPlan(executablePlan)

        allExecutablePlans = workoutDao.getExecutablePlanById(1).first()
        assertTrue(allExecutablePlans.isEmpty())
    }

    @Test
    fun deleteNonExistentPlan_doesNothing() = runBlocking {
        val plan = Plan(id = 1, planName = "Non-existent Plan")

        workoutDao.deletePlan(plan)

        val allPlans = workoutDao.getAllPlanNames().first()
        assertTrue(allPlans.isEmpty())
    }

    @Test
    fun deletePlan_cascadesDeleteToExecutablePlans() = runBlocking {
        val plan = Plan(id = 1, planName = "Test Plan")
        workoutDao.insertPlan(plan)

        val exercise = Exercise(id = 1, exerciseName = "Push-up")
        workoutDao.insertExercise(exercise)

        val executablePlan =
            ExecutablePlan(id = 1, planId = plan.id, exerciseId = exercise.id, sets = 3, order = 1)
        workoutDao.insertExecutablePlan(executablePlan)

        workoutDao.deletePlan(plan)

        val allExecutablePlans = workoutDao.getExecutablePlanById(plan.id).first()
        assertTrue(allExecutablePlans.isEmpty())
    }

    @Test
    fun deleteNonExistentExercise_doesNothing() = runBlocking {
        val exercise = Exercise(id = 1, exerciseName = "Non-existent Exercise")

        workoutDao.deleteExercise(exercise)

        val allExercises = workoutDao.getAllExerciseNames().first()
        assertTrue(allExercises.isEmpty())
    }

    private fun getPlan(id: Long = 1, planName: String = "Test Plan"): Plan =
        Plan(id, planName)

    private fun getExercise(id: Long = 1, exerciseName: String = "Test Exercise"): Exercise =
        Exercise(id, exerciseName)

    private fun getExecutablePlan(
        id: Long = 1,
        planId: Long = 1,
        exerciseId: Long = 1,
        sets: Int = 3,
        order: Int = 1,
    ): ExecutablePlan = ExecutablePlan(id, planId, exerciseId, sets, order)


    private fun getExecution(
        id: Long = 1,
        exerciseId: Long = 1,
        reps: Int = 10,
        weight: Int = 200,
        date: Long = LocalDate.now().toEpochDay(),
    )
            : Execution =
        Execution(id, exerciseId, weight, reps, date)
}