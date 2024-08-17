package com.example.GymBuddy.data.localdatabase

data class ExecutablePlanWithDetails(
    val executablePlanId: Int,
    val planId: Int,
    val planName: String,
    val exerciseId: Int,
    var exerciseName: String,
    var sets: Int,
    var order: Int
)
