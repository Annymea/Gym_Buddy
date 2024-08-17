package com.example.GymBuddy.data.localdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM plans")
    fun getAllPlanNames(): Flow<List<Plan>>

    @Query("SELECT * FROM exercises")
    fun getAllExerciseNames(): Flow<List<Exercise>>

    @Query("SELECT * FROM executablePlans WHERE plan_id = :planId")
    fun getExecutablePlanById(planId: Int): Flow<List<ExecutablePlan>>

    @Query("SELECT * FROM executions WHERE exercise_id = :exerciseId")
    fun getExecutionsById(exerciseId: Int): Flow<List<Execution>>

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
    fun getExecutablePlanWithDetailsByPlanId(planId: Int): Flow<List<ExecutablePlanWithDetails>>

    @Insert
    suspend fun insertPlan(plan: Plan): Long

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert
    suspend fun insertExecutablePlan(executablePlan: ExecutablePlan): Long

    @Insert
    suspend fun insertExecution(execution: Execution): Long

    @Delete
    suspend fun deletePlan(plan: Plan)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExerciseFromPlan(executablePlan: ExecutablePlan)

    @Delete
    suspend fun deleteExecution(execution: Execution)
}
