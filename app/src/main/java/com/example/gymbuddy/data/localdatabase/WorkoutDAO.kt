package com.example.gymbuddy.data.localdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDAO {
    //tested
    @Query("SELECT * FROM workout_details WHERE id = :workoutId")
    fun getWorkoutDetailsFor(workoutId: Long): Flow<WorkoutDetailsEntity>

    @Query("SELECT * FROM exercise_details WHERE id = :exerciseId")
    fun getExerciseDetailsFor(exerciseId: Long): Flow<ExerciseDetailsEntity>

    // tested
    @Query("SELECT * FROM workout WHERE workout_details_id = :workoutId")
    fun getWorkoutFor(workoutId: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workout_details")
    fun getAllWorkoutDetails(): Flow<List<WorkoutDetailsEntity>>

    @Query("SELECT * FROM workout WHERE workout_details_id = :workoutId")
    fun getExercisesFor(workoutId: Long): Flow<List<WorkoutEntity>>

    // tested
    @Query("SELECT * FROM executions WHERE exercise_details_id = :workoutId")
    fun getExecutionsFor(workoutId: Long): Flow<List<ExecutionEntity>>

    // tested
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorkoutExecution(executionEntities: List<ExecutionEntity>)

    // tested
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutEntities(workoutEntities: List<WorkoutEntity>)
    //tested
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
}
