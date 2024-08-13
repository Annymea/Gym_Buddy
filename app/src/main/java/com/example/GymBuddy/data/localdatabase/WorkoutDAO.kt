package com.example.GymBuddy.data.localdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM plans")
    fun getAllPlanNames(): List<Plan>

    @Query("SELECT * FROM exercises")
    fun getAllExerciseNames(): List<Exercise>

    @Query("SELECT * FROM executablePlans WHERE plan_id = :planId")
    fun getExecutablePlanById(planId: Int): List<ExecutablePlan>

    @Query("SELECT * FROM executions WHERE exercise_id = :exerciseId")
    fun getExecutionsById(exerciseId: Int): List<Execution>

    @Query(
        """
            SELECT 
                executablePlans.id AS executablePlanId, 
                plans.id AS planId, 
                plans.plan_name AS planName, 
                exercises.id AS exerciseId, 
                exercises.exercise_name AS exerciseName, 
                executablePlans.sets, 
                executablePlans.`order`
            FROM executablePlans
            INNER JOIN plans ON executablePlans.plan_id = plans.id
            INNER JOIN exercises ON executablePlans.exercise_id = exercises.id
            WHERE executablePlans.plan_id = :planId
            ORDER BY executablePlans.`order`
        """
    )
    fun getExecutablePlanWithDetailsByPlanId(planId: Int): List<ExecutablePlanWithDetails>

    @Insert
    fun insertPlan(plan: Plan)

    @Insert
    fun insertExercise(exercise: Exercise)

    @Insert
    fun insertExecutablePlan(executablePlan: ExecutablePlan)

    @Insert
    fun insertExecution(execution: Execution)

    @Delete
    fun deletePlan(plan: Plan)

    @Delete
    fun deleteExercise(exercise: Exercise)

    @Delete
    fun deleteExerciseFromPlan(executablePlan: ExecutablePlan)

    @Delete
    fun deleteExecution(execution: Execution)
}
