package com.example.GymBuddy.data

import com.example.GymBuddy.data.localdatabase.ExecutablePlan
import com.example.GymBuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.GymBuddy.data.localdatabase.Execution
import com.example.GymBuddy.data.localdatabase.Exercise
import com.example.GymBuddy.data.localdatabase.Plan
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    // get
    suspend fun getAllExerciseNames(): Flow<List<Exercise>>
    suspend fun getAllPlanNames(): Flow<List<Plan>>
    suspend fun getExecutablePlanById(planId: Int): Flow<List<ExecutablePlan>>
    suspend fun getExecutionsById(exerciseId: Int): Flow<List<Execution>>
    suspend fun getExecutablePlanWithDetailsByPlanId(planId: Int): Flow<List<ExecutablePlanWithDetails>>

    // insert
    suspend fun insertPlan(plan: Plan): Long
    suspend fun insertExercise(exercise: Exercise): Long
    suspend fun insertExecutablePlan(executablePlan: ExecutablePlan): Long
    suspend fun insertExecution(execution: Execution): Long

    // delete
    suspend fun deletePlan(plan: Plan)
    suspend fun deleteExercise(exercise: Exercise)
    suspend fun deleteExerciseFromPlan(executablePlan: ExecutablePlan)
    suspend fun deleteExecution(execution: Execution)
}
