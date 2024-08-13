package com.example.GymBuddy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "plans")
data class Plan(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "plan_name") val planName: String
)

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "exercise_name") val exerciseName: String
)

@Entity(
    tableName = "executablePlans",
    indices = [
        Index(value = ["plan_id"]),
        Index(value = ["exercise_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Plan::class,
            parentColumns = ["id"],
            childColumns = ["plan_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExecutablePlan(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "plan_id") val planId: Int,
    @ColumnInfo(name = "exercise_id") val exerciseId: Int,
    val sets: Int,
    val order: Int
)

@Entity(
    tableName = "executions",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Execution(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "exercise_id") val exerciseId: Int,
    val weight: Int,
    val reps: Int,
    val date: Date
)
