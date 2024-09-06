package com.example.GymBuddy.ui.runningWorkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RunningWorkout(
    workoutId: String,
    modifier: Modifier = Modifier,
    planListViewModel: RunningWorkoutViewModel = koinViewModel<RunningWorkoutViewModel>(
        parameters = { parametersOf(workoutId) }
    )
) {
    Column(modifier = modifier) {
        Text(text = planListViewModel.planName.value, modifier = Modifier)

        LazyColumn {
            items(planListViewModel.exercises) { exercise ->
                Text(text = exercise.exerciseName)
            }
        }
    }
}
