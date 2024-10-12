package com.example.gymbuddy.data

data class Workout(
    var id: Long,
    var name: String,
    var category: String,
    var note: String,
    var exercises: List<Exercise>,
)

data class Exercise(
    var id: Long,
    var name: String,
    var note: String,
    var setCount: Int,
    var order: Int,
    var sets: List<Set>,
)

data class Set(
    var id: Long,
    var reps: Int,
    var weight: Double,
)
