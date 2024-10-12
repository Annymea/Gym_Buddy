package com.example.gymbuddy.ui.old.startWorkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartWorkout(
    modifier: Modifier = Modifier,
    startWorkoutViewModel: StartWorkoutViewModelContract =
        koinViewModel<StartWorkoutViewModel>(),
    onSelectWorkout: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Training starten!!")

        LazyColumn {
            items(startWorkoutViewModel.workouts) { workout ->
                Button(
                    modifier = Modifier.testTag("workoutButton"),
                    onClick = { onSelectWorkout(workout.id.toString()) }
                ) {
                    Text(text = workout.workoutName)
                }
            }
        }
    }
}
