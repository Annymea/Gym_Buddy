package com.example.GymBuddy.data

data class ExecutablePlanWithDetails(
    val executablePlanId: Int,
    val planId: Int,
    val planName: String,
    val exerciseId: Int,
    val exerciseName: String,
    val sets: Int,
    val order: Int
)
