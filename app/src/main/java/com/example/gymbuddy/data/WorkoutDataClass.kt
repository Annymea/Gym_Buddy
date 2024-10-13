package com.example.gymbuddy.data

data class Workout(
    var id: Long = 0,
    var name: String = "",
    var category: String = "",
    var note: String = "",
    var exercises: MutableList<WorkoutExercise> = mutableListOf()
)

data class WorkoutExercise(
    var id: Long = 0,
    var category: String = "",
    var name: String,
    var note: String = "",
    var setCount: Int = 0,
    var order: Int,
    var sets: MutableList<WorkoutSet> = mutableListOf()
)

data class WorkoutSet(
    var reps: Int,
    var weight: Float,
    var order: Int
)
