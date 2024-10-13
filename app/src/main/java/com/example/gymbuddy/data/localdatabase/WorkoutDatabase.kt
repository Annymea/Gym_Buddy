package com.example.gymbuddy.data.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        WorkoutDetailsEntity::class,
        ExerciseDetailsEntity::class,
        WorkoutEntity::class,
        ExecutionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDAO
}
