package com.example.GymBuddy.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Plan::class,
        Exercise::class,
        ExecutablePlan::class,
        Execution::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateTimestampCoverter::class
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDAO
}
