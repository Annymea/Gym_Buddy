package com.example.GymBuddy.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.GymBuddy.R
import com.example.GymBuddy.ui.createPlan.CreatePlanViewModel
import com.example.GymBuddy.ui.theme.Gym_BuddyTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun Dashboard(
    modifier: Modifier,
    onCreatePlanButtonClicked: () -> Unit
) {
    FirstButtons(
        modifier = modifier,
        onCreatePlanButtonClicked = onCreatePlanButtonClicked
    )
}

@Composable
fun FirstButtons(
    modifier: Modifier,
    createPlanViewModel: CreatePlanViewModel = koinViewModel<CreatePlanViewModel>(),
    onCreatePlanButtonClicked: () -> Unit
) {
    Column(modifier = modifier) {
        Button(
            onClick = {
                onCreatePlanButtonClicked()
            }
        ) {
            Text(text = stringResource(R.string.create_plan))
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.start_training))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Gym_BuddyTheme {
        FirstButtons(Modifier, onCreatePlanButtonClicked = {})
    }
}
