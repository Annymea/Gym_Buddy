package com.example.gymbuddy.ui.workouts.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymbuddy.R
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.ui.common.ScreenTitle
import com.example.gymbuddy.ui.common.draggableList.DraggableLazyColumn

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
                onDeleteWorkout = { id -> viewModel.onDeleteWorkout(id) },
                onExecuteWorkout = { id -> onExecuteWorkout(id) },
                onReorder = { viewModel.onReorder(it) }

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
    onExecuteWorkout: (id: Long) -> Unit,
    onDeleteWorkout: (id: Long) -> Unit = {},
    onReorder: (List<Workout>) -> Unit = {}
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
        DraggableLazyColumn(
            items = workouts.toMutableStateList(),
            onMove = { newWorkouts -> onReorder(newWorkouts) },
            modifier =
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) { workout ->
            WorkoutCard(
                testTag = "workoutCard",
                workoutId = workout.id,
                modifier =
                Modifier,
                workoutTitle = workout.name,
                onStartWorkout = { onExecuteWorkout(workout.id) },
                onDeleteWorkout = { onDeleteWorkout(workout.id) }
            )
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
    testTag: String = "",
    workoutTitle: String,
    workoutId: Long,
    modifier: Modifier = Modifier,
    onStartWorkout: () -> Unit = {},
    onDeleteWorkout: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier =
        modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier =
            Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
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
            WorkoutOptionsButton(
                idForTag = workoutId,
                onDeleteWorkout = { onDeleteWorkout() }
            )
        }
    }
}

@Composable
fun WorkoutOptionsButton(
    idForTag: Long,
    onDeleteWorkout: () -> Unit
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { isDropDownExpanded.value = true },
            modifier = Modifier.testTag("workoutOptionsButton$idForTag")
        ) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }

        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    isDropDownExpanded.value = false
                    onDeleteWorkout()
                },
                modifier = Modifier.testTag("deleteWorkoutMenuItem")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutCardPreview() {
    WorkoutCard(workoutTitle = "Test Workout", testTag = "rofl", workoutId = 123456789)
}

@Preview(showBackground = true)
@Composable
fun NoWorkoutsPreview() {
    CreateFirstWorkoutButton(onNewWorkout = {})
}
