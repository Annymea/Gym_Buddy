package com.example.GymBuddy.data

import com.example.GymBuddy.data.localdatabase.ExecutablePlan
import com.example.GymBuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.GymBuddy.data.localdatabase.Execution
import com.example.GymBuddy.data.localdatabase.Exercise
import com.example.GymBuddy.data.localdatabase.Plan

interface WorkoutRepository {
    // get
    fun getAllExerciseNames(): List<Exercise>
    fun getAllPlanNames(): List<Plan>
    fun getExecutablePlanById(planId: Int): List<ExecutablePlan>
    fun getExecutionsById(exerciseId: Int): List<Execution>
    fun getExecutablePlanWithDetailsByPlanId(planId: Int): List<ExecutablePlanWithDetails>

    // insert
    suspend fun insertPlan(plan: Plan)
    suspend fun insertExercise(exercise: Exercise)
    suspend fun insertExecutablePlan(executablePlan: ExecutablePlan)
    suspend fun insertExecution(execution: Execution)

    // delete
    suspend fun deletePlan(plan: Plan)
    suspend fun deleteExercise(exercise: Exercise)
    suspend fun deleteExerciseFromPlan(executablePlan: ExecutablePlan)
    suspend fun deleteExecution(execution: Execution)
}
