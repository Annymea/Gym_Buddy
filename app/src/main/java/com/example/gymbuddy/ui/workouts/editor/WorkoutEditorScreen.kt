package com.example.gymbuddy.ui.workouts.editor

import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymbuddy.R
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.common.ConfirmationDialog
import com.example.gymbuddy.ui.common.ScreenTitle

@Composable
fun WorkoutEditorScreen(
    modifier: Modifier = Modifier,
    workoutEditorViewModel: WorkoutEditorViewModelContract =
        hiltViewModel<WorkoutEditorViewModel>(),
    navigateBack: () -> Unit = {}
) {
    val addExerciseDialogShown = remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        ScreenTitle(
            text = stringResource(R.string.workout_editor_new_workout_screen_title),
            testTag = "screenTitle_Editor"
        )

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
                value = workoutEditorViewModel.workout.value?.name ?: "",
                onValueChange = { newValue -> workoutEditorViewModel.updateWorkoutName(newValue) },
                modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("workoutNameInput")
            )

            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                modifier =
                Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            ) {
                itemsIndexed(
                    workoutEditorViewModel.workout.value?.exercises ?: emptyList()
                ) { index, exercise ->
                    AddExerciseCard(
                        modifier = Modifier.padding(top = 16.dp),
                        exercise = exercise,
                        onUpdateExercise = { updatedExercise ->
                            workoutEditorViewModel.updateExercise(index, updatedExercise)
                        },
                        onDeleteExercise = {
                            workoutEditorViewModel.removeExercise(index)
                        }
                    )
                }

                item {
                    AddExerciseButton(
                        modifier = Modifier.testTag("addExerciseButton"),
                        onAddExerciseButtonClicked = {
                            addExerciseDialogShown.value = true
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
            onSaveButtonClicked = {
                workoutEditorViewModel.saveWorkout()
                navigateBack()
            },
            onCancelButtonClicked = {
                navigateBack()
            }
        )

        if (addExerciseDialogShown.value) {
            AddExerciseToWorkoutDialog(
                existingExercises = workoutEditorViewModel.getExistingExercises(),
                onDismissRequest = { addExerciseDialogShown.value = false },
                onExercisesSelected = { selectedExercises ->
                    workoutEditorViewModel.addAllSelectedExercises(selectedExercises)
                    addExerciseDialogShown.value = false
                }
            )
        }
    }
}

@Composable
private fun SaveAndCancelButton(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
    saveButtonEnabled: Boolean
) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showSaveDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.workoutEditor_saveDialog_title),
            message = stringResource(R.string.workoutEditor_saveDialog_text),
            confirmButtonText = stringResource(R.string.dialogButton_save),
            cancelButtonText = stringResource(R.string.dialogButton_cancel),
            onConfirm = {
                onSaveButtonClicked()
            },
            onDismissRequest = {
                showSaveDialog = false
            }
        )
    }

    if (showCancelDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.workoutEditor_cancelDialog_title),
            message = stringResource(R.string.workoutEditor_cancelDialog_text),
            confirmButtonText = stringResource(R.string.dialogButton_Confirm),
            cancelButtonText = stringResource(R.string.dialogButton_keepEditing),
            onConfirm = {
                onCancelButtonClicked()
            },
            onDismissRequest = {
                showCancelDialog = false
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
                .padding(end = 16.dp)
                .testTag("cancelButton"),
            onClick = { showCancelDialog = true }
        ) {
            Text(text = stringResource(R.string.workout_editor_cancel_button_text))
        }

        Button(
            enabled = saveButtonEnabled,
            modifier =
            Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .testTag("saveButton"),
            onClick = { showSaveDialog = true }
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
    exercise: WorkoutExercise,
    onUpdateExercise: (WorkoutExercise) -> Unit = {},
    onDeleteExercise: () -> Unit = {}
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier =
        modifier
            .fillMaxWidth()
            .testTag("addExerciseCard")
    ) {
        Row(
            modifier =
            Modifier
                .padding(top = 16.dp, start = 16.dp, end = 0.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = exercise.name,
                modifier =
                Modifier
                    .padding(end = 16.dp)
                    .weight(1f)
                    .testTag("exerciseNameText")
                    .align(Alignment.CenterVertically)
            )

            OutlinedTextField(
                label = { Text(text = stringResource(R.string.workout_editor_sets_input_title)) },
                value = exercise.setCount.toString(),
                onValueChange = { newSets ->
                    onUpdateExercise(
                        exercise.copy(setCount = newSets.toIntOrNull() ?: 0)
                    )
                },
                modifier = Modifier.width(64.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Box {
                IconButton(
                    modifier =
                    Modifier
                        .padding(top = 8.dp)
                        .testTag("moreButton"),
                    onClick = { isDropDownExpanded.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
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
                        modifier = Modifier.testTag("deleteExerciseButton")
                    )
                }
            }
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
    val exampleExercise = WorkoutExercise(name = "Squats", setCount = 3, order = 1)
    AddExerciseCard(
        exercise = exampleExercise,
        onUpdateExercise = { _ -> }
    )
}
