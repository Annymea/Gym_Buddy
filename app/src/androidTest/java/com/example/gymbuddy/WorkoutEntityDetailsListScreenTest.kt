package com.example.gymbuddy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.ui.old.planList.PlanList
import com.example.gymbuddy.ui.old.planList.PlanListViewModelContract
import org.junit.Rule
import org.junit.Test

class WorkoutEntityDetailsListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun planList_displaysEmptyMessageWhenListIsEmpty() {
        val emptyPlanList = emptyList<WorkoutDetailsEntity>()

        composeTestRule.setContent {
            PlanList(
                planListViewModel = FakePlanListViewModel(emptyPlanList),
                onCreateNewPlan = {},
            )
        }

        composeTestRule
            .onNodeWithText("Hier könnten ihre Listen stehen")
            .assertIsDisplayed()
    }

    @Test
    fun planList_displaysPlansWhenListIsNotEmpty() {
        val planList =
            listOf(
                WorkoutDetailsEntity(workoutName = "Plan A"),
                WorkoutDetailsEntity(workoutName = "Plan B"),
            )

        composeTestRule.setContent {
            PlanList(
                planListViewModel = FakePlanListViewModel(planList),
                onCreateNewPlan = {},
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
                onCreateNewPlan = { isButtonClicked = true },
            )
        }

        composeTestRule.onNodeWithText("Neuen Plan erstellen").performClick()

        assert(isButtonClicked)
    }
}

class FakePlanListViewModel(
    plans: List<WorkoutDetailsEntity>,
) : PlanListViewModelContract {
    override val planList: List<WorkoutDetailsEntity> = plans
}
