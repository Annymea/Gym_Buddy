package com.example.GymBuddy.ui.navigation

import androidx.annotation.StringRes
import com.example.GymBuddy.R

sealed class ScreenRoutes(
    val route: String,
    @StringRes val resourceId: Int
) {
    open fun startDestinationRoute(): String = route

    data object Dashboard : ScreenRoutes(
        "dashboard",
        R.string.dashboardRoute
    )

    data object CreatePlan : ScreenRoutes(
        "createPlan",
        R.string.createPlanRoute
    )

    data object PlanList : ScreenRoutes(
        "planList",
        R.string.planListRoute
    )

    data object StartWorkout : ScreenRoutes(
        "startWorkout",
        R.string.startWorkoutRoute
    )

    data object RunningWorkout : ScreenRoutes(
        "runningWorkout/{workoutId}",
        R.string.runningWorkoutRoute
    )

    data object CreateWorkoutGraph : ScreenRoutes(
        "createWorkoutGraph",
        R.string.create_workout
    ) {
        override fun startDestinationRoute() = PlanList.route
    }

    data object RunWorkoutGraph : ScreenRoutes(
        "startWorkoutGraph",
        R.string.run_workout
    ) {
        override fun startDestinationRoute() = StartWorkout.route
    }
}
