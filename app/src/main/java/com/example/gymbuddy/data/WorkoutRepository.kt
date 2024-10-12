package com.example.gymbuddy.data

import com.example.gymbuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.gymbuddy.data.localdatabase.ExecutionEntity
import com.example.gymbuddy.data.localdatabase.ExerciseDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutEntity
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    // get
    suspend fun getAllExerciseNames(): Flow<List<ExerciseDetailsEntity>>

    suspend fun getAllPlanNames(): Flow<List<WorkoutDetailsEntity>>

    suspend fun getPlanById(planId: Long): Flow<WorkoutDetailsEntity>

    suspend fun getExecutablePlanById(planId: Long): Flow<List<WorkoutEntity>>

    suspend fun getExecutionsById(exerciseId: Long): Flow<List<ExecutionEntity>>

    suspend fun getPlanWithDetailsBy(planId: Long): Flow<List<ExecutablePlanWithDetails>>

    // insert
    suspend fun insertPlan(plan: WorkoutDetailsEntity): Long

    suspend fun insertExercise(exercise: ExerciseDetailsEntity): Long

    suspend fun insertExecutablePlan(executablePlan: WorkoutEntity): Long

    suspend fun insertExecution(execution: ExecutionEntity): Long

    // delete
    suspend fun deletePlan(plan: WorkoutDetailsEntity)

    suspend fun deleteExercise(exercise: ExerciseDetailsEntity)

    suspend fun deleteExerciseFromPlan(executablePlan: WorkoutEntity)

    suspend fun deleteExecution(execution: ExecutionEntity)

    // refactoring:

    // Yet not a flow due to complex implementation.
    // This should only be changed if really needed
    suspend fun getWorkout(workoutId: Long): Workout

    suspend fun createWorkout(workout: Workout): Long

    suspend fun createExercise(exercise: Exercise): Long

    suspend fun saveWorkoutExecution(workoutExecution: Workout)

    suspend fun editWorkout(updatedWorkout: Workout)

    suspend fun deleteWorkout(workoutId: Long)

    suspend fun deleteExercise(exerciseId: Long)
}
