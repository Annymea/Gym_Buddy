package com.example.gymbuddy.ui.exercises.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.common.ScreenTitle
import com.example.gymbuddy.ui.common.addExerciseDialog.AddExerciseDialog

@Composable
fun ExerciseOverviewScreen(
    modifier: Modifier = Modifier,
    viewModel: ExerciseOverviewViewModelContract = hiltViewModel<ExerciseOverviewViewModel>()
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val uiState = viewModel.uiState.collectAsState()

        val showAddExerciseDialog = remember { mutableStateOf(false) }

        if (showAddExerciseDialog.value) {
            AddExerciseDialog(
                onDismissRequest = { showAddExerciseDialog.value = false }
            )
        }

        Row {
            ScreenTitle(
                text = "Exercises",
                testTag = "exercise_overview_screen_title",
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { showAddExerciseDialog.value = true },
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary),
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 24.dp)
                    .testTag("createExerciseButton")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        when (uiState.value) {
            is ExerciseOverviewUiState.NoExercises -> {
                NoExercisesScreen()
            }

            is ExerciseOverviewUiState.Exercises -> {
                ExerciseScreen(
                    viewModel.exercises,
                    deleteExercise = { viewModel.deleteExercise(it) },
                    editExercise = { }
                )
            }
        }
    }
}

@Composable
fun NoExercisesScreen() {
    Text(text = "No Exercises")
}

@Composable
fun ExerciseScreen(
    exercises: List<WorkoutExercise>,
    deleteExercise: (exerciseId: Long) -> Unit = {},
    editExercise: (exercise: WorkoutExercise) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(exercises) { exercise ->
            ExerciseCard(
                exercise = exercise,
                onDeleteExercise = { deleteExercise(exercise.id) },
                onEditExercise = { editExercise(exercise) }
            )
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: WorkoutExercise,
    onDeleteExercise: () -> Unit,
    onEditExercise: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                modifier =
                Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically),
                text = exercise.name,
                color = MaterialTheme.colorScheme.onSurface
            )

            ExerciseOptions(
                onDeleteExercise = onDeleteExercise,
                onEditExercise = onEditExercise
            )
        }
    }
}

@Composable
fun ExerciseOptions(
    onDeleteExercise: () -> Unit,
    onEditExercise: () -> Unit
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { isDropDownExpanded.value = true },
            modifier = Modifier.testTag("exerciseOptionsButton")
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
                    onDeleteExercise()
                },
                modifier = Modifier.testTag("deleteExerciseMenuItem")
            )
            // DropdownMenuItem(
            //    text = { Text("Edit") },
            //    onClick = {
            //        isDropDownExpanded.value = false
            //        onEditExercise()
            //    },
            //    modifier = Modifier.testTag("editExerciseMenuItem")
            // )
        }
    }
}
