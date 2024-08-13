import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.GymBuddy.data.localdatabase.ExecutablePlan
import com.example.GymBuddy.data.localdatabase.Execution
import com.example.GymBuddy.data.localdatabase.Exercise
import com.example.GymBuddy.data.localdatabase.Plan
import com.example.GymBuddy.data.localdatabase.WorkoutDAO
import com.example.GymBuddy.data.localdatabase.WorkoutDatabase
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
    fun insertOnePlanAndGetOnePlan() {
        val plan = Plan(id = 1, planName = "Test Plan")
        workoutDao.insertPlan(plan)

        val retrievedPlan = workoutDao.getAllPlanNames()
        assert(retrievedPlan.contains(plan))
        assert(retrievedPlan.size == 1)
    }

    @Test
    fun insertMultiplePlansAndGetMultiplePlans() {
        val listOfPlans = listOf(
            Plan(id = 1, planName = "Test Plan 1"),
            Plan(id = 2, planName = "Test Plan 2"),
            Plan(id = 3, planName = "Test Plan 3")
        )
        for (plan in listOfPlans) {
            workoutDao.insertPlan(plan)
        }

        val retrievedPlan = workoutDao.getAllPlanNames()
        for (plan in listOfPlans) {
            assert(retrievedPlan.contains(plan))
        }
        assert(retrievedPlan.size == 3)
    }

    @Test
    fun insertOneExerciseAndGetOneExercise() {
        val exercise = Exercise(id = 1, exerciseName = "Test Exercise")
        workoutDao.insertExercise(exercise)

        val retrievedExercise = workoutDao.getAllExerciseNames()
        assert(retrievedExercise.contains(exercise))
        assert(retrievedExercise.size == 1)
    }

    @Test
    fun insertMultipleExercisesAndGetMultipleExercises() {
        val listOfExercises = listOf(
            Exercise(id = 1, exerciseName = "Test Exercise 1"),
            Exercise(id = 2, exerciseName = "Test Exercise 2"),
            Exercise(id = 3, exerciseName = "Test Exercise 3")
        )
        for (exercise in listOfExercises) {
            workoutDao.insertExercise(exercise)
        }

        val retrievedExercise = workoutDao.getAllExerciseNames()
        for (exercise in listOfExercises) {
            assert(retrievedExercise.contains(exercise))
        }
        assert(retrievedExercise.size == 3)
    }

    @Test
    fun insertOneExecutablePlanAndGetOneExecutablePlanByPlanId() {
        val planId = 1

        val executablePlan = ExecutablePlan(
            id = 1,
            planId = planId,
            exerciseId = 1,
            sets = 3,
            order = 1,
        )

        workoutDao.insertPlan(Plan(id = planId, planName = "Test Plan"))
        workoutDao.insertExercise(Exercise(id = 1, exerciseName = "Test Exercise 1"))

        workoutDao.insertExecutablePlan(executablePlan)

        val retrievedExecutablePlan = workoutDao.getExecutablePlanById(planId)
        assert(retrievedExecutablePlan.contains(executablePlan))
        assert(retrievedExecutablePlan.size == 1)
    }

    @Test
    fun insertMultipleExecutablePlansAndGetMultipleExecutablePlansByPlanId() {
        val planId = 1

        val listOfExecutablePlans = listOf(
            ExecutablePlan(
                id = 1,
                planId = planId,
                exerciseId = 1,
                sets = 3,
                order = 1,
            ),
            ExecutablePlan(
                id = 2,
                planId = planId,
                exerciseId = 2,
                sets = 4,
                order = 2,
            ),
            ExecutablePlan(
                id = 3,
                planId = planId,
                exerciseId = 3,
                sets = 5,
                order = 3,
            )
        )
        workoutDao.insertPlan(Plan(id = planId, planName = "Test Plan"))
        workoutDao.insertExercise(Exercise(id = 1, exerciseName = "Test Exercise 1"))
        workoutDao.insertExercise(Exercise(id = 2, exerciseName = "Test Exercise 2"))
        workoutDao.insertExercise(Exercise(id = 3, exerciseName = "Test Exercise 3"))

        for (executablePlan in listOfExecutablePlans) {
            workoutDao.insertExecutablePlan(executablePlan)
        }

        val retrievedExecutablePlan = workoutDao.getExecutablePlanById(planId)
        for (executablePlan in listOfExecutablePlans) {
            assert(retrievedExecutablePlan.contains(executablePlan))
        }
        assert(retrievedExecutablePlan.size == 3)

    }

    @Test
    fun insertExecutionAndGetByExerciseId() {
        val exerciseId = 1

        val execution = Execution(
            id = 1,
            exerciseId = exerciseId,
            reps = 10,
            weight = 200,
            date = LocalDate.parse("2018-12-31")
        )

    }

}