package com.example.gymbuddy.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.gymbuddy.ui.old.createPlan.CreatePlan
import com.example.gymbuddy.ui.old.dashboard.Dashboard
import com.example.gymbuddy.ui.old.planList.PlanList
import com.example.gymbuddy.ui.old.runningWorkout.RunningWorkout
import com.example.gymbuddy.ui.old.startWorkout.StartWorkout

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(
        ScreenRoutes.Dashboard,
        ScreenRoutes.CreateWorkoutGraph,
        ScreenRoutes.RunWorkoutGraph
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
    items: List<ScreenRoutes>
) {
    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text(text = stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any
                { it.route == screen.route } == true,
                onClick = {
                    if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                        navController.popBackStack(screen.startDestinationRoute(), false)
                    } else {
                        navController.navigate(screen.route) {
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
        startDestination = ScreenRoutes.Dashboard.route
    ) {
        composable(ScreenRoutes.Dashboard.route) {
            Dashboard(
                modifier = modifier,
                onCreatePlanButtonClicked = {
                    navController.navigate(ScreenRoutes.PlanList.route)
                },
                onStartTrainingButtonClicked = {
                    navController.navigate(ScreenRoutes.StartWorkout.route)
                }
            )
        }

        createWorkoutNavigation(modifier, navController)

        runWorkoutNavigation(modifier, navController)
    }
}

private fun NavGraphBuilder.runWorkoutNavigation(
    modifier: Modifier,
    navController: NavHostController
) {
    navigation(
        startDestination = ScreenRoutes.StartWorkout.route,
        route = ScreenRoutes.RunWorkoutGraph.route
    ) {
        composable(ScreenRoutes.StartWorkout.route) {
            StartWorkout(
                modifier = modifier,
                onSelectWorkout = { workoutId: String ->
                    navController.navigate(
                        ScreenRoutes.RunningWorkout.route
                            .replace(
                                oldValue = "{workoutId}",
                                newValue = workoutId
                            )
                    )
                }
            )
        }

        composable(ScreenRoutes.RunningWorkout.route) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            workoutId?.let { id ->
                RunningWorkout(
                    workoutId = id,
                    modifier = modifier,
                    saveWorkout = {
                        navController.navigate(ScreenRoutes.Dashboard.route)
                    }
                )
            }
        }
    }
}

private fun NavGraphBuilder.createWorkoutNavigation(
    modifier: Modifier,
    navController: NavHostController
) {
    navigation(
        startDestination = ScreenRoutes.PlanList.route,
        route = ScreenRoutes.CreateWorkoutGraph.route
    ) {
        composable(ScreenRoutes.CreatePlan.route) {
            CreatePlan(
                modifier = modifier,
                onPlanSaved = {
                    Log.i("Navigation", "Plan saved")
                    navController.popBackStack(ScreenRoutes.PlanList.route, false)
                }
            )
        }
        composable(ScreenRoutes.PlanList.route) {
            PlanList(
                modifier = modifier,
                onCreateNewPlan = {
                    navController.navigate(ScreenRoutes.CreatePlan.route)
                }
            )
        }
    }
}
