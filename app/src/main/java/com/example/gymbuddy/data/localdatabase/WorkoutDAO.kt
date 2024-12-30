package com.example.gymbuddy.data.localdatabase

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.Update
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideWorkoutDatabase(
        @ApplicationContext appContext: Context
    ): WorkoutDatabase =
        Room
            .databaseBuilder(
                appContext,
                WorkoutDatabase::class.java,
                "gym_buddy_database"
            ).build()

    @Provides
    fun provideWorkoutDao(database: WorkoutDatabase): WorkoutDAO = database.workoutDao()
}

@Dao
interface WorkoutDAO {
    @Query("SELECT * FROM workout_details WHERE id = :workoutId")
    fun getWorkoutDetailsFor(workoutId: Long): Flow<WorkoutDetailsEntity?>

    @Query("SELECT * FROM exercise_details WHERE id = :exerciseId")
    fun getExerciseDetailsFor(exerciseId: Long): Flow<ExerciseDetailsEntity?>

    @Query("SELECT * FROM workout WHERE workout_details_id = :workoutId")
    fun getWorkoutFor(workoutId: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workout_details ORDER BY overviewOrder")
    fun getAllWorkoutDetails(): Flow<List<WorkoutDetailsEntity>>

    @Query("SELECT * FROM workout WHERE workout_details_id = :workoutId")
    fun getExercisesFor(workoutId: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM executions WHERE exercise_details_id = :workoutId")
    fun getExecutionsFor(workoutId: Long): Flow<List<ExecutionEntity>>

    @Query("SELECT DISTINCT date FROM executions WHERE date BETWEEN :startDate AND :endDate")
    fun getAllDatesOfExecutionsForTimeSpan(
        startDate: Long,
        endDate: Long
    ): Flow<List<Long>>

    @Query(
        "SELECT * FROM executions " +
            "WHERE date = " +
            "(SELECT MAX(date) FROM executions WHERE exercise_details_id = :exerciseId) " +
            "AND exercise_details_id = :exerciseId"
    )
    fun getLatestExecutionsFor(exerciseId: Long): Flow<List<ExecutionEntity>>

    @Query("SELECT * FROM exercise_details")
    fun getAllExerciseDetails(): Flow<List<ExerciseDetailsEntity>>

    @Query("SELECT COALESCE( MAX(overviewOrder), 0) FROM workout_details")
    fun getMaxWorkoutIndex(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorkoutExecution(executionEntities: List<ExecutionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutEntities(workoutEntities: List<WorkoutEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutDetails(workoutDetails: WorkoutDetailsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutEntity(workoutEntities: WorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseDetails(exerciseDetails: ExerciseDetailsEntity): Long

    @Update
    suspend fun updateWorkoutEntities(workoutEntities: List<WorkoutEntity>)

    @Delete
    suspend fun deleteWorkoutEntities(workoutEntities: List<WorkoutEntity>)

    @Query("DELETE FROM exercise_details WHERE id = :exerciseId")
    suspend fun deleteExerciseDetailsById(exerciseId: Long)

    @Query("UPDATE workout_details SET overviewOrder = :order WHERE id = :id")
    suspend fun updateSingleEntry(id: Long, order: Int)
}
