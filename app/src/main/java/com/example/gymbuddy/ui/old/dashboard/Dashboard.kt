package com.example.gymbuddy.ui.old.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymbuddy.ui.theme.Gym_BuddyTheme

@Composable
fun Dashboard(modifier: Modifier) {
    FirstButtons(
        modifier = modifier
    )
}

@Composable
fun FirstButtons(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            modifier = modifier.testTag("dashboardTitle"),
            text = "Dashboard Content"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Gym_BuddyTheme {
        FirstButtons(
            Modifier
        )
    }
}
