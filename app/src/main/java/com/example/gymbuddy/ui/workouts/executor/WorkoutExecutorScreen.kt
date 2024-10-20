package com.example.gymbuddy.ui.workouts.executor

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.gymbuddy.R
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutSet
import com.example.gymbuddy.ui.workouts.common.ConfirmationDialog
import com.example.gymbuddy.ui.workouts.common.ScreenTitle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WorkoutExecutorScreen(
    modifier: Modifier = Modifier,
    workoutId: String,
    viewModel: WorkoutExecutorViewModelContract =
        koinViewModel<WorkoutExecutorViewModel>(parameters = { parametersOf(workoutId) }),
    navigateBack: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ScreenTitle(
            text = viewModel.workout.value?.name ?: "Default workout",
            testTag = "screenTitle_Executor"
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(viewModel.workout.value?.exercises ?: listOf()) { index, exercise ->
                ExerciseCard(
                    exercise = exercise,
                    addSet = { viewModel.addSet(exercise) },
                    deleteExercise = { viewModel.deleteExercise(index) },
                    updateSet = { set -> viewModel.updateSet(exerciseIndex = index, set = set) }
                )
            }
        }

        FinishAndCancelButton(
            modifier = Modifier.padding(16.dp),
            onFinishWithChanges = {
                viewModel.saveExecutions()
                viewModel.updateWorkout()
                navigateBack()
            },
            onFinishWithoutChanges = {
                viewModel.saveExecutions()
                navigateBack()
            },
            onCancelButtonClicked = {
                navigateBack()
            }
        )
    }
}

@Composable
private fun FinishAndCancelButton(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onFinishWithoutChanges: () -> Unit = {},
    onFinishWithChanges: () -> Unit = {}
) {
    var showFinishDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showFinishDialog) {
        FinishWorkoutDialog(
            onFinishWithoutChanges = { onFinishWithoutChanges() },
            onFinishWithChanges = { onFinishWithChanges() },
            onDismissRequest = { showFinishDialog = false }
        )
    }

    if (showCancelDialog) {
        ConfirmationDialog(
            title = "Cancel workout",
            message = "Do you want to cancel the workout? All changes will be lost.",
            confirmButtonText = stringResource(R.string.dialogButton_Confirm),
            cancelButtonText = "Continue workout",
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
            modifier =
            Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .testTag("finishButton"),
            onClick = { showFinishDialog = true }
        ) {
            Text(text = stringResource(R.string.workout_editor_save_button_text))
        }
    }
}

@Composable
fun ExerciseCard(
    modifier: Modifier = Modifier,
    exercise: WorkoutExercise,
    addSet: () -> Unit = {},
    deleteExercise: () -> Unit = {},
    updateSet: (WorkoutSet) -> Unit = {}
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier.testTag("exerciseCard")
    ) {
        Row(
            modifier =
            Modifier
                .padding(start = 16.dp, top = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.name,
                modifier = Modifier.weight(1f).testTag("exerciseName"),
                style = MaterialTheme.typography.titleLarge
            )
            Box {
                IconButton(
                    modifier = Modifier.testTag("moreVertButton"),
                    onClick = { isDropDownExpanded.value = true }
                ) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }

                DropdownMenu(
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = { isDropDownExpanded.value = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete exercise") },
                        onClick = {
                            isDropDownExpanded.value = false
                            deleteExercise()
                        },
                        modifier = Modifier.testTag("deleteExerciseMenuItem")
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sets",
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Reps",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Weight",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
            ) {
                Log.d("ExerciseCard", "Sets: ${exercise.sets}")
                exercise.sets.forEach { set ->
                    Log.d("ExerciseCard", "Set: $set")
                    ExecutionRow(
                        set = set.order,
                        reps = set.reps,
                        weight = set.weight,
                        updateSet = { updatedSet ->
                            updateSet(updatedSet)
                        },
                        modifier = Modifier.testTag("executionRow")
                    )
                }
            }
            TextButton(
                onClick = { addSet() },
                modifier = Modifier.testTag("addSetButton")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Text(text = "Add set")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishWorkoutDialog(
    onFinishWithoutChanges: () -> Unit,
    onFinishWithChanges: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        modifier = Modifier.testTag("finishWorkoutDialog"),
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        content = {
            Card {
                Column(
                    modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Finish workout",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Do you want to save the changes on the workout?",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Button(
                        onClick = {
                            onFinishWithoutChanges()
                            onDismissRequest()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Finish without changes")
                    }

                    Button(
                        onClick = {
                            onFinishWithChanges()
                            onDismissRequest()
                        },
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Finish with changes")
                    }

                    OutlinedButton(
                        onClick = {
                            onDismissRequest()
                        },
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    )
}

@Composable
fun ExecutionRow(
    modifier: Modifier = Modifier,
    set: Int,
    reps: Int,
    weight: Float,
    updateSet: (WorkoutSet) -> Unit
) {
    var repsInput by remember { mutableStateOf(reps.toString()) }
    var weightInput by remember { mutableStateOf(weight.toString()) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = set.toString(),
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                singleLine = true,
                label = {},
                value = repsInput,
                onValueChange = { newReps ->
                    repsInput = newReps
                    val updatedReps = newReps.toIntOrNull() ?: reps
                    updateSet(WorkoutSet(order = set, reps = updatedReps, weight = weight))
                },
                modifier = Modifier.width(80.dp),
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                singleLine = true,
                label = {},
                value = weightInput,
                onValueChange = { newWeight ->
                    weightInput = newWeight
                    val updatedWeight = newWeight.toFloatOrNull() ?: weight
                    updateSet(WorkoutSet(order = set, reps = reps, weight = updatedWeight))
                },
                modifier = Modifier.width(80.dp),
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
        }
    }
}

// @Preview(showBackground = true)
// @Composable
// fun ExerciseCardPreview() {
//    val exercise =
//        WorkoutExercise(
//            exerciseId = 1,
//            exerciseSets = mutableStateListOf(),
//            exerciseName = "1"
//        )
//    ExerciseCard(exercise = , addSet = { /*TODO*/ }, deleteExercise = { /*TODO*/ })
// }

@Preview(showBackground = true)
@Composable
fun WorkoutExecutorScreenPreview() {
    WorkoutExecutorScreen(workoutId = "1")
}

@Preview(showBackground = true)
@Composable
fun FinishWorkoutDialogPreview() {
    FinishWorkoutDialog(
        onFinishWithoutChanges = { /*TODO*/ },
        onFinishWithChanges = { /*TODO*/ },
        onDismissRequest = { /*TODO*/ }
    )
}
