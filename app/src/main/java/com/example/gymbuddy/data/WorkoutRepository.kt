package com.example.gymbuddy.data

import com.example.gymbuddy.data.localdatabase.ExecutablePlan
import com.example.gymbuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.gymbuddy.data.localdatabase.Execution
import com.example.gymbuddy.data.localdatabase.Exercise
import com.example.gymbuddy.data.localdatabase.Plan
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    // get
    suspend fun getAllExerciseNames(): Flow<List<Exercise>>
    suspend fun getAllPlanNames(): Flow<List<Plan>>
    suspend fun getPlanById(planId: Long): Flow<Plan>
    suspend fun getExecutablePlanById(planId: Long): Flow<List<ExecutablePlan>>
    suspend fun getExecutionsById(exerciseId: Long): Flow<List<Execution>>
    suspend fun getExecutablePlanWithDetailsByPlanId(planId: Long): Flow<List<ExecutablePlanWithDetails>>

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
