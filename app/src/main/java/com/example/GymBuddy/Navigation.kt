package com.example.GymBuddy

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.GymBuddy.ui.createPlan.CreatePlan
import com.example.GymBuddy.ui.dashboard.Dashboard
import com.example.GymBuddy.ui.planList.PlanList
import com.example.GymBuddy.ui.runningWorkout.RunningWorkout
import com.example.GymBuddy.ui.startWorkout.StartWorkout

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            Dashboard(
                modifier = modifier,
                onCreatePlanButtonClicked = {
                    navController.navigate("plan_list")
                },
                onStartTrainingButtonClicked = {
                    navController.navigate("start_workout")
                }
            )
        }
        composable("create_plan") {
            CreatePlan(
                modifier = modifier,
                onPlanSaved = {
                    navController.navigate("plan_list")
                }
            )
        }
        composable("plan_list") {
            PlanList(
                modifier = modifier,
                onCreateNewPlan = {
                    navController.navigate("create_plan")
                }
            )
        }
        composable("start_workout") {
            StartWorkout(
                modifier = modifier,
                onSelectWorkout = { workoutId: String ->
                    navController.navigate(
                        "running_workout/{workoutId}"
                            .replace(
                                oldValue = "{workoutId}",
                                newValue = workoutId
                            )
                    )
                }
            )
        }

        composable("running_workout/{workoutId}") { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            workoutId?.let { id ->
                RunningWorkout(
                    workoutId = id,
                    modifier = modifier,
                    saveWorkout = {
                        navController.navigate("dashboard")
                    }
                )
            }
        }
    }
}
