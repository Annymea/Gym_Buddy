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

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
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
                }
            )
        }
        composable("create_plan") {
            CreatePlan(
                modifier = modifier
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
    }
}
