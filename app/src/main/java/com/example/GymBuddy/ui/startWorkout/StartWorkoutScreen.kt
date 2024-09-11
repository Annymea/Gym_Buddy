package com.example.GymBuddy.ui.startWorkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartWorkout(
    modifier: Modifier = Modifier,
    startWorkoutViewModel: StartWorkoutViewModel = koinViewModel<StartWorkoutViewModel>(),
    onSelectWorkout: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Training starten!!")

        LazyColumn {
            items(startWorkoutViewModel.workouts) { workout ->
                Button(
                    onClick = { onSelectWorkout(workout.id.toString()) }
                ) {
                    Text(text = workout.planName)
                }
            }
        }
    }
}