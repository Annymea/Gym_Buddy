package com.example.gymbuddy.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.gymbuddy.ui.dashboard.Dashboard
import com.example.gymbuddy.ui.exercises.overview.ExerciseOverviewScreen
import com.example.gymbuddy.ui.workouts.editor.WorkoutEditorScreen
import com.example.gymbuddy.ui.workouts.executor.WorkoutExecutorScreen
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val items =
        listOf(
            NavigationRoutes.DashboardGraph,
            NavigationRoutes.WorkoutGraph,
            NavigationRoutes.ExerciseGraph
        )

    Scaffold(
        modifier = modifier.systemBarsPadding(),
        bottomBar = {
            BottomNavigationBar(navController, items)
        }
    ) { innerPadding ->
        Navigation(
            modifier = modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    items: List<NavigationRoutes>
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { navigationRoute ->
            NavigationBarItem(
                modifier =
                Modifier.testTag(
                    "bottomNavigation_" + stringResource(navigationRoute.resourceId)
                ),
                icon = {
                    Icon(navigationRoute.icon, contentDescription = null)
                },
                label = {
                    Text(text = stringResource(navigationRoute.resourceId))
                },
                selected =
                currentDestination?.hierarchy?.any
                { it.route == navigationRoute.route } == true,
                onClick = {
                    if (currentDestination?.hierarchy?.any
                        { it.route == navigationRoute.route } == true
                    ) {
                        navController.popBackStack(navigationRoute.startDestination, false)
                    } else {
                        navController.navigate(navigationRoute.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = false
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.DashboardGraph.route
    ) {
        dashboardNavigation(modifier)
        workoutNavigation(modifier, navController)
        exerciseNavigation(modifier)
    }
}

private fun NavGraphBuilder.workoutNavigation(
    modifier: Modifier,
    navController: NavHostController
) {
    navigation(
        startDestination = ScreenRoutes.WorkoutOverview.route,
        route = NavigationRoutes.WorkoutGraph.route
    ) {
        composable(ScreenRoutes.WorkoutOverview.route) {
            WorkoutOverviewScreen(
                modifier = modifier,
                onCreateWorkout = { navController.navigate(ScreenRoutes.WorkoutEditor.route) },
                onExecuteWorkout = { workoutId ->
                    navController.navigate("workoutExecutor/$workoutId")
                }
            )
        }

        composable(ScreenRoutes.WorkoutEditor.route) {
            WorkoutEditorScreen(
                modifier = modifier,
                navigateBack = {
                    navController.popBackStack(ScreenRoutes.WorkoutOverview.route, false)
                }
            )
        }

        composable(ScreenRoutes.WorkoutExecutor.route) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            workoutId?.let {
                WorkoutExecutorScreen(
                    modifier = modifier,
                    navigateBack = {
                        navController.popBackStack(ScreenRoutes.WorkoutOverview.route, false)
                    }
                )
            }
        }
    }
}

private fun NavGraphBuilder.dashboardNavigation(modifier: Modifier) {
    navigation(
        startDestination = ScreenRoutes.Dashboard.route,
        route = NavigationRoutes.DashboardGraph.route
    ) {
        composable(ScreenRoutes.Dashboard.route) {
            Dashboard(
                modifier = modifier
            )
        }
    }
}

private fun NavGraphBuilder.exerciseNavigation(modifier: Modifier) {
    navigation(
        startDestination = ScreenRoutes.ExerciseOverview.route,
        route = NavigationRoutes.ExerciseGraph.route
    ) {
        composable(ScreenRoutes.ExerciseOverview.route) {
            ExerciseOverviewScreen(
                modifier = modifier
            )
        }
    }
}
