package com.example.gymbuddy.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gymbuddy.R

sealed class NavigationRoutes(
    val route: String,
    val startDestination: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    data object DashboardGraph : NavigationRoutes(
        "dashboardGraph",
        ScreenRoutes.Dashboard.route,
        R.string.dashboardRoute,
        Icons.Default.Home
    )

    data object WorkoutGraph : NavigationRoutes(
        "workoutGraph",
        ScreenRoutes.WorkoutOverview.route,
        R.string.bottom_navigation_workouts_button_text,
        Icons.Default.Menu
    )

    data object ExerciseGraph : NavigationRoutes(
        "exerciseGraph",
        ScreenRoutes.WorkoutOverview.route,
        R.string.bottom_navigation_exercises_button_text,
        Icons.Default.FavoriteBorder
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

    data object WorkoutEditor : ScreenRoutes(
        "workoutEditor"
    )

    data object WorkoutExecutor : ScreenRoutes(
        "workoutExecutor/{workoutId}"
    )

    data object ExerciseOverview : ScreenRoutes(
        "exerciseOverview"
    )
}
