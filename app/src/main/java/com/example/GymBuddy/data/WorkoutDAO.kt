package com.example.gymbuddy.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM plans")
    fun getAllPlanNames(): List<Plans>

    @Query("SELECT * FROM exercises")
    fun getAllExerciseNames(): List<Exercises>

    @Query("SELECT * FROM executablePlans WHERE plan_id = :planId")
    fun getExecutablePlanById(planId: Int): List<ExecutablePlans>

    @Query("SELECT * FROM executions WHERE exercise_id = :exerciseId")
    fun getExecutionsById(exerciseId: Int): List<Executions>

    @Insert
    fun insertPlan(plan: Plans)

    @Insert
    fun insertExercise(exercise: Exercises)

    @Insert
    fun insertExecutablePlan(executablePlan: ExecutablePlans)

    @Delete
    fun deletePlan(plan: Plans)

    @Delete
    fun deleteExercise(exercise: Exercises)

    @Delete
    fun deleteExerciseFromPlan(executablePlan: ExecutablePlans)
}
