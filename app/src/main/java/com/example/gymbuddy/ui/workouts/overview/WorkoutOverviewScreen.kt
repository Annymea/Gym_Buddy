package com.example.gymbuddy.ui.workouts.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymbuddy.R
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.ui.common.ScreenTitle

@Composable
fun WorkoutOverviewScreen(
    modifier: Modifier = Modifier,
    viewModel: WorkoutOverviewViewModelContract = hiltViewModel<WorkoutOverviewViewModel>(),
    onCreateWorkout: () -> Unit = {},
    onExecuteWorkout: (id: Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is WorkoutOverviewUiState.NoWorkouts -> {
            CreateFirstWorkout(
                modifier = modifier,
                onCreateWorkout = { onCreateWorkout() }
            )
        }

        is WorkoutOverviewUiState.Workouts -> {
            WorkoutOverview(
                modifier = modifier,
                workouts = viewModel.workouts,
                onCreateWorkout = { onCreateWorkout() },
                onExecuteWorkout = { id -> onExecuteWorkout(id) }
            )
        }
    }
}

@Composable
fun CreateFirstWorkout(
    modifier: Modifier = Modifier,
    onCreateWorkout: () -> Unit = {}
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
    ) {
        ScreenTitle(
            text = stringResource(R.string.workout_overview_title),
            testTag = "screenTitle_Overview"
        )

        Box(
            modifier =
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CreateFirstWorkoutButton(
                onNewWorkout = { onCreateWorkout() }
            )
        }
    }
}

@Composable
fun WorkoutOverview(
    modifier: Modifier = Modifier,
    workouts: List<Workout> = emptyList(),
    onCreateWorkout: () -> Unit = {},
    onExecuteWorkout: (id: Long) -> Unit
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
    ) {
        Row {
            ScreenTitle(
                modifier =
                Modifier
                    .weight(1f),
                text = stringResource(R.string.workout_overview_title),
                testTag = "screenTitle_Overview"
            )

            IconButton(
                onClick = { onCreateWorkout() },
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 24.dp)
                    .testTag("createWorkoutButton")
            ) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Filled.Add,
                    contentDescription =
                    stringResource(
                        R.string.workout_overview_add_workout_button_description
                    ),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(workouts) { workout ->
                WorkoutCard(
                    workoutTitle = workout.name,
                    onStartWorkout = { onExecuteWorkout(workout.id) }
                )
            }
        }
    }
}

@Composable
fun CreateFirstWorkoutButton(
    onNewWorkout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        IconButton(
            modifier =
            Modifier
                .size(72.dp)
                .align(Alignment.CenterHorizontally),
            colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),
            onClick = { onNewWorkout() }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Filled.Add,
                contentDescription =
                stringResource(
                    R.string.workout_overview_add_workout_button_description
                ),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            fontSize = 20.sp,
            modifier =
            Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(R.string.workout_overview_add_first_workout_button_text)
        )
    }
}

@Composable
fun WorkoutCard(
    workoutTitle: String,
    modifier: Modifier = Modifier,
    onStartWorkout: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier =
        modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                modifier =
                Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically),
                text = workoutTitle,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),
                onClick = { onStartWorkout() },
                modifier = Modifier.testTag("startWorkoutButton")
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription =
                    stringResource(
                        R.string.workout_overview_start_workout_button_description
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutCardPreview() {
    WorkoutCard("Test Workout")
}

@Preview(showBackground = true)
@Composable
fun NoWorkoutsPreview() {
    CreateFirstWorkoutButton(onNewWorkout = {})
}
