package com.example.gymbuddy.ui.old.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymbuddy.R
import com.example.gymbuddy.ui.theme.Gym_BuddyTheme

@Composable
fun Dashboard(
    modifier: Modifier,
    onCreatePlanButtonClicked: () -> Unit,
    onStartTrainingButtonClicked: () -> Unit,
) {
    FirstButtons(
        modifier = modifier,
        onCreatePlanButtonClicked = onCreatePlanButtonClicked,
        onStartTrainingButtonClicked = onStartTrainingButtonClicked
    )
}

@Composable
fun FirstButtons(
    modifier: Modifier,
    onCreatePlanButtonClicked: () -> Unit,
    onStartTrainingButtonClicked: () -> Unit,
) {
    Column(modifier = modifier) {
        Text(text = "Dashboard Content")
        Button(
            onClick = {
                onCreatePlanButtonClicked()
            }
        ) {
            Text(text = stringResource(R.string.create_plan))
        }
        Button(
            onClick = {
                onStartTrainingButtonClicked()
            }
        ) {
            Text(text = stringResource(R.string.start_training))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Gym_BuddyTheme {
        FirstButtons(
            Modifier,
            onCreatePlanButtonClicked = {},
            onStartTrainingButtonClicked = {}
        )
    }
}
