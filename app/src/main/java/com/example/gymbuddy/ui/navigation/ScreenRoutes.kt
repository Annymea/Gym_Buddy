package com.example.gymbuddy.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gymbuddy.R

sealed class NavigationRoutes(
    val route: String,
    val startDestination: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    data object CreateWorkoutGraph : NavigationRoutes(
        "createWorkoutGraph",
        ScreenRoutes.CreatePlan.route,
        R.string.create_workout,
        Icons.Default.Add
    )

    data object RunWorkoutGraph : NavigationRoutes(
        "runWorkoutGraph",
        ScreenRoutes.StartWorkout.route,
        R.string.run_workout,
        Icons.Default.PlayArrow
    )

    data object DashboardGraph : NavigationRoutes(
        "dashboardGraph",
        ScreenRoutes.Dashboard.route,
        R.string.dashboardRoute,
        Icons.Default.Home
    )

    data object WorkoutGraph : NavigationRoutes(
        "workoutGraph",
        ScreenRoutes.WorkoutOverview.route,
        R.string.workouts,
        Icons.Default.Menu
    )
}

sealed class ScreenRoutes(
    val route: String
) {
    data object WorkoutOverview : ScreenRoutes(
        "workoutOverview"
    )

    data object Dashboard : ScreenRoutes(
        "dashboard"
    )

    data object CreatePlan : ScreenRoutes(
        "createPlan"
    )

    data object PlanList : ScreenRoutes(
        "planList"
    )

    data object StartWorkout : ScreenRoutes(
        "startWorkout"
    )

    data object RunningWorkout : ScreenRoutes(
        "runningWorkout/{workoutId}"
    )
}
