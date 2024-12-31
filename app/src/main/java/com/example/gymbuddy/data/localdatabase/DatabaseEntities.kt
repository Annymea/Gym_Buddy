package com.example.gymbuddy.data.localdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "workout_details")
data class WorkoutDetailsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "workout_name") val workoutName: String,
    val note: String = "",
    val category: String = "",
    var overviewOrder: Int
)

@Entity(tableName = "exercise_details")
data class ExerciseDetailsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "exercise_name") val exerciseName: String,
    val note: String = "",
    val category: String = ""
)

@Entity(
    tableName = "workout",
    indices = [
        Index(value = ["workout_details_id"]),
        Index(value = ["exercise_details_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["workout_details_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_details_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "workout_details_id") val workoutDetailsId: Long,
    @ColumnInfo(name = "exercise_details_id") val exerciseDetailsId: Long,
    val sets: Int,
    val order: Int
)

@Entity(
    tableName = "executions",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_details_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExecutionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "exercise_details_id") val exerciseDetailsId: Long,
    val weight: Float,
    val reps: Int,
    val date: Long
)
