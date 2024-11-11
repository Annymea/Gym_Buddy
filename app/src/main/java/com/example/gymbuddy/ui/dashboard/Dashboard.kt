package com.example.gymbuddy.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymbuddy.ui.common.ScreenTitle
import com.example.gymbuddy.ui.dashboard.widgets.trainingsCalender.TrainingsCalender
import com.example.gymbuddy.ui.theme.Gym_BuddyTheme

@Composable
fun Dashboard(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ScreenTitle(text = "Dashboard", testTag = "dashboard_title")
        TrainingsCalender(modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Gym_BuddyTheme {
        Dashboard()
    }
}
