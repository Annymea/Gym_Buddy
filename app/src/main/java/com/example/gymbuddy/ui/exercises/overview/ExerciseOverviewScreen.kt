package com.example.gymbuddy.ui.exercises.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.workouts.common.ScreenTitle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExerciseOverviewScreen(
    modifier: Modifier = Modifier,
    viewModel: ExerciseOverviewViewModel = koinViewModel<ExerciseOverviewViewModel>()
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val uiState = viewModel.uiState.collectAsState()

        ScreenTitle(text = "Exercises", testTag = "exercise_overview_screen_title")

        when (uiState.value) {
            is ExerciseOverviewUiState.NoExercises -> {
                NoExercisesScreen()
            }

            is ExerciseOverviewUiState.Exercises -> {
                ExerciseScreen(viewModel.exercises)
            }
        }
    }
}

@Composable
fun NoExercisesScreen() {
    Text(text = "No Exercises")
}

@Composable
fun ExerciseScreen(exercises: List<WorkoutExercise>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(exercises) { exercise ->
            Text(text = exercise.name)
        }
    }
}
