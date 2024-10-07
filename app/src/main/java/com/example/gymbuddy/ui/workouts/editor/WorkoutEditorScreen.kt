package com.example.gymbuddy.ui.workouts.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymbuddy.R
import com.example.gymbuddy.ui.workouts.common.ConfirmationDialog
import com.example.gymbuddy.ui.workouts.common.ScreenTitle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutEditorScreen(
    modifier: Modifier = Modifier,
    workoutEditorViewModel: WorkoutEditorViewModel = koinViewModel<WorkoutEditorViewModel>()
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenTitle(text = stringResource(R.string.workout_editor_new_workout_screen_title))

        Column(
            modifier =
            Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                label = {
                    Text(text = stringResource(id = R.string.workout_editor_workout_title))
                },
                value = workoutEditorViewModel.workoutName.value,
                onValueChange = { newValue -> workoutEditorViewModel.updateWorkoutName(newValue) },
                modifier = Modifier.fillMaxWidth()
            )

            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            LazyColumn(
                state = listState,
                modifier =
                Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            ) {
                itemsIndexed(workoutEditorViewModel.exerciseListToBeSaved) { index, exercise ->
                    AddExerciseCard(
                        modifier = Modifier.padding(top = 16.dp),
                        exercise = exercise,
                        onUpdateExercise = { updatedExercise ->
                            workoutEditorViewModel.updateExercise(index, updatedExercise)
                        }
                    )
                }

                item {
                    AddExerciseButton(
                        onAddExerciseButtonClicked = {
                            workoutEditorViewModel.addExercise(
                                ViewModelExercise(name = "Default Exercise", sets = 3)
                            )
                            coroutineScope.launch {
                                listState.animateScrollToItem(
                                    workoutEditorViewModel.exerciseListToBeSaved.size
                                )
                            }
                        }
                    )
                }
            }
        }

        SaveAndCancelButton(
            modifier = Modifier.padding(16.dp),
            saveButtonEnabled = (
                workoutEditorViewModel.saveState.value == SavingWorkoutState.Idle ||
                    workoutEditorViewModel.saveState.value == SavingWorkoutState.Saved
                ),
            onSaveButtonClicked = { workoutEditorViewModel.saveWorkout() }
        )
    }
}

@Composable
private fun SaveAndCancelButton(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
    saveButtonEnabled: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ConfirmationDialog(
            title = "Änderungen speichern?",
            message = "Möchtest du die Änderungen wirklich speichern?",
            confirmButtonText = "Speichern",
            cancelButtonText = "Abbrechen",
            onConfirm = {
                onSaveButtonClicked()
            },
            onCancel = {
                showDialog = false
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }

    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedButton(
            modifier =
            Modifier
                .weight(1f)
                .padding(end = 16.dp),
            onClick = { onCancelButtonClicked() }
        ) {
            Text(text = stringResource(R.string.workout_editor_cancel_button_text))
        }

        Button(
            enabled = saveButtonEnabled,
            modifier =
            Modifier
                .weight(1f)
                .padding(start = 16.dp),
            onClick = { showDialog = true }
        ) {
            Text(text = stringResource(R.string.workout_editor_save_button_text))
        }
    }
}

@Composable
private fun AddExerciseButton(
    modifier: Modifier = Modifier,
    onAddExerciseButtonClicked: () -> Unit = {}
) {
    TextButton(
        modifier =
        modifier
            .padding(top = 16.dp)
            .height(48.dp),
        onClick = { onAddExerciseButtonClicked() }
    ) {
        Icon(
            modifier =
            Modifier
                .height(48.dp)
                .width(48.dp),
            imageVector = Icons.Default.AddCircle,
            contentDescription =
            stringResource(
                R.string.workout_editor_add_exercise_button_description
            )
        )
        Text(
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            text = stringResource(R.string.workout_editor_add_exercise_button_text)
        )
    }
}

@Composable
private fun AddExerciseCard(
    modifier: Modifier = Modifier,
    exercise: ViewModelExercise,
    onUpdateExercise: (ViewModelExercise) -> Unit
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
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        text =
                        stringResource(
                            R.string.workout_editor_exercise_name_input_title
                        )
                    )
                },
                value = exercise.name,
                onValueChange = { newName ->
                    onUpdateExercise(
                        exercise.copy(name = newName)
                    )
                },
                modifier =
                Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )
            OutlinedTextField(
                label = { Text(text = stringResource(R.string.workout_editor_sets_input_title)) },
                value = exercise.sets.toString(),
                onValueChange = { newSets ->
                    onUpdateExercise(
                        exercise.copy(sets = newSets.toIntOrNull() ?: 0)
                    )
                },
                modifier = Modifier.width(64.dp),
                singleLine = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutEditorScreenPreview() {
    WorkoutEditorScreen()
}

@Preview(showBackground = true)
@Composable
private fun AddExerciseCardPreview() {
    val exampleExercise = ViewModelExercise(name = "Squats", sets = 3)
    AddExerciseCard(
        exercise = exampleExercise,
        onUpdateExercise = { _ -> }
    )
}
