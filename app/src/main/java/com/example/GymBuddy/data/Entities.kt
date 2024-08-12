package com.example.gymbuddy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Plans(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "plan_name") val planName: String,
)

@Entity
data class Exercises(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "exercise_name") val exerciseName: String,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Plans::class,
            parentColumns = ["id"],
            childColumns = ["plan_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercises::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExecutablePlans(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "plan_id") val planId: Int,
    @ColumnInfo(name = "exercise_id") val exerciseId: Int,
    val sets: Int,
    val order: Int,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Exercises::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Executions(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "exercise_id") val exerciseId: Int,
    val weight: Int,
    val reps: Int,
    val date: Date,
)


