package com.example.GymBuddy.ui.planList

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlanList(
    modifier: Modifier = Modifier,
    planListViewModel: PlanListViewModelContract = koinViewModel<PlanListViewModel>(),
    onCreateNewPlan: () -> Unit
) {
    Column(modifier = modifier) {
        if (planListViewModel.planList.isEmpty()) {
            Text(text = "Hier könnten ihre Listen stehen", modifier = modifier)
        } else {
            Text(text = "Pläne", modifier = modifier)
            planListViewModel.planList.forEach {
                Text(text = it.planName)
            }
        }
        Button(onClick = { onCreateNewPlan() }) {
            Text(text = "Neuen Plan erstellen")
        }
    }
}
