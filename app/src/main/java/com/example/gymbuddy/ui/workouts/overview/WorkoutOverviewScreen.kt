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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymbuddy.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutOverviewScreen(
    modifier: Modifier = Modifier,
    workoutOverviewViewModel: WorkoutOverviewViewModelContract =
        koinViewModel<WorkoutOverviewViewModel>(),
    onCreateWorkout: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()

    ) {
        Text(
            fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
            fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
            modifier = Modifier.padding(24.dp),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            text = stringResource(R.string.workoutOverviewTitle)
        )

        if (workoutOverviewViewModel.workouts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NoWorkouts(
                    onNewWorkout = { onCreateWorkout() }
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(workoutOverviewViewModel.workouts) { workout ->
                    WorkoutCard(workoutTitle = workout.planName)
                }
            }
        }
    }
}

@Composable
fun NoWorkouts(
    onNewWorkout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        IconButton(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.CenterHorizontally),
            colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),
            onClick = { onNewWorkout() }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_workout_button_description),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp),
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(R.string.add_first_workout_button_text)
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
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                modifier = Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically),
                text = workoutTitle,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),
                onClick = { onStartWorkout() }
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.start_workout_button_description)
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
    NoWorkouts(onNewWorkout = {})
}
