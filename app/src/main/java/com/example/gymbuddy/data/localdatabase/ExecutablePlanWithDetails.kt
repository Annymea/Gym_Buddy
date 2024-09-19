package com.example.gymbuddy.data.localdatabase

data class ExecutablePlanWithDetails(
    val executablePlanId: Long,
    val planId: Long,
    val planName: String,
    val exerciseId: Long,
    var exerciseName: String,
    var sets: Int,
    var order: Int
)
