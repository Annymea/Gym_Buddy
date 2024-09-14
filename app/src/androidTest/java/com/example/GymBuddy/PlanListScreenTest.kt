package com.example.GymBuddy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.GymBuddy.data.localdatabase.Plan
import com.example.GymBuddy.ui.planList.PlanList
import com.example.GymBuddy.ui.planList.PlanListViewModelContract
import org.junit.Rule
import org.junit.Test

class PlanListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun planList_displaysEmptyMessageWhenListIsEmpty() {
        val emptyPlanList = emptyList<Plan>()

        composeTestRule.setContent {
            PlanList(
                planListViewModel = FakePlanListViewModel(emptyPlanList),
                onCreateNewPlan = {}
            )
        }

        composeTestRule
            .onNodeWithText("Hier könnten ihre Listen stehen")
            .assertIsDisplayed()
    }

    @Test
    fun planList_displaysPlansWhenListIsNotEmpty() {
        val planList = listOf(
            Plan(planName = "Plan A"),
            Plan(planName = "Plan B")
        )

        composeTestRule.setContent {
            PlanList(
                planListViewModel = FakePlanListViewModel(planList),
                onCreateNewPlan = {}
            )
        }

        composeTestRule.onNodeWithText("Pläne").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plan A").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plan B").assertIsDisplayed()
    }

    @Test
    fun planList_navigateToCreatePlanWhenButtonClicked() {
        var isButtonClicked = false
        composeTestRule.setContent {
            PlanList(
                planListViewModel = FakePlanListViewModel(emptyList()),
                onCreateNewPlan = { isButtonClicked = true }
            )
        }

        composeTestRule.onNodeWithText("Neuen Plan erstellen").performClick()

        assert(isButtonClicked)
    }
}

class FakePlanListViewModel(plans: List<Plan>) : PlanListViewModelContract {
    override val planList: List<Plan> = plans
}
