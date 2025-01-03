package com.example.gymbuddy.data

import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(
        workoutRepositoryImpl: LocalDataRepository
    ): WorkoutRepository
}

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

    suspend fun addExercise(newExercise: WorkoutExercise)

    suspend fun deleteExercise(exerciseId: Long)

    suspend fun updateWorkout(newWorkout: Workout)

    suspend fun createNewWorkout(newWorkout: Workout)

    suspend fun getDaysOfCompletedWorkoutsForMonth(
        month: Int,
        year: Int
    ): Flow<List<Int>>

    suspend fun updateWorkoutOrder(workoutIds: List<Long>)

    suspend fun deleteWorkout(workoutId: Long)
}
