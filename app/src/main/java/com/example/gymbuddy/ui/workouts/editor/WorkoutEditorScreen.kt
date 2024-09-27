package com.example.gymbuddy.ui.workouts.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymbuddy.ui.workouts.common.ScreenTitle

@Composable
fun WorkoutEditorScreen(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenTitle(text = "Workout Editor")


    }
}
