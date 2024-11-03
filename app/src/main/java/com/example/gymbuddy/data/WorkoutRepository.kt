package com.example.gymbuddy.data

import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    // Yet not a flow due to complex implementation.
    // This should only be changed if really needed
    suspend fun getWorkout(workoutId: Long): Workout?

    suspend fun getAllWorkoutDetails(): Flow<List<Workout>>

    suspend fun getAllExercises(): Flow<List<WorkoutExercise>>

    suspend fun saveWorkoutExecution(
        doneExercises: List<WorkoutExercise>,
        date: Long
    )

    suspend fun updateWorkout(newWorkout: Workout)

    suspend fun createNewWorkout(newWorkout: Workout)

    suspend fun getDaysOfCompletedWorkoutsForMonth(
        month: Int,
        year: Int
    ): Flow<List<Int>>
}
