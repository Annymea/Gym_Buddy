package com.example.gymbuddy.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Workout(
    var id: Long = 0,
    var name: String = "",
    var category: String = "",
    var note: String = "",
    var exercises: SnapshotStateList<WorkoutExercise> = mutableStateListOf()
)

data class WorkoutExercise(
    var id: Long = 0,
    var category: String = "",
    var name: String,
    var note: String = "",
    var setCount: Int = 0,
    var order: Int = 0,
    var sets: SnapshotStateList<WorkoutSet> = mutableStateListOf()
)

data class WorkoutSet(
    var reps: Int,
    var weight: Float,
    var order: Int
)
