package com.example.gymbuddy.data.localdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM workout_details WHERE id = :workoutId")
    fun getWorkoutDetailsFor(workoutId: Long) : Flow<WorkoutDetailsEntity>

    @Query("SELECT * FROM exercise_details WHERE id = :exerciseId")
    fun getExerciseDetailsFor(exerciseId: Long) : Flow<ExerciseDetailsEntity>

    @Query("SELECT * FROM workout WHERE id = :workoutId")
    fun getWorkoutFor(workoutId: Long) : Flow<List<WorkoutEntity>>







    @Query("SELECT * FROM workout_details")
    fun getAllPlanNames(): Flow<List<WorkoutDetailsEntity>>

    @Query("SELECT * FROM exercise_details")
    fun getAllExerciseNames(): Flow<List<ExerciseDetailsEntity>>

    @Query("SELECT * FROM workout_details WHERE id = :workoutDetailsId")
    fun getPlanById(workoutDetailsId: Long): Flow<WorkoutDetailsEntity>

    @Query("SELECT * FROM executablePlans WHERE workout_detail_id = :planId")
    fun getExecutablePlanById(planId: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM executions WHERE exercise_id = :exerciseId")
    fun getExecutionsById(exerciseId: Long): Flow<List<ExecutionEntity>>

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
        """,
    )
    fun getExecutablePlanWithDetailsByPlanId(planId: Long): Flow<List<ExecutablePlanWithDetails>>

    @Insert
    suspend fun insertPlan(plan: WorkoutDetailsEntity): Long

    @Insert
    suspend fun insertExercise(exercise: ExerciseDetailsEntity): Long

    @Insert
    suspend fun insertExecutablePlan(executablePlan: WorkoutEntity): Long

    @Insert
    suspend fun insertExecution(execution: ExecutionEntity): Long

    @Delete
    suspend fun deletePlan(plan: WorkoutDetailsEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseDetailsEntity)

    @Delete
    suspend fun deleteExerciseFromPlan(executablePlan: WorkoutEntity)

    @Delete
    suspend fun deleteExecution(execution: ExecutionEntity)

    @Query()
}
