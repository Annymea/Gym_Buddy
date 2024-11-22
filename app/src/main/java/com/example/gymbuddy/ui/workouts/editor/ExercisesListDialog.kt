package com.example.gymbuddy.ui.workouts.editor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.ui.common.addExerciseDialog.AddExerciseDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseToWorkoutDialog(
    existingExercises: List<WorkoutExercise>,
    onDismissRequest: () -> Unit,
    onExercisesSelected: (List<WorkoutExercise>) -> Unit,
) {
    val selectedExercises = remember { mutableStateListOf<WorkoutExercise>() }
    val addExerciseDialogShown = remember { mutableStateOf(false) }

    if (addExerciseDialogShown.value) {
        AddExerciseDialog(
            onDismissRequest = { addExerciseDialogShown.value = false },
        )
    }

    BasicAlertDialog(
        modifier = Modifier.testTag("chooseExercisesDialog"),
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
    ) {
        Card(
            modifier =
                Modifier.padding(top = 16.dp, bottom = 16.dp),
        ) {
            Column(
                modifier =
                    Modifier.height(
                        500.dp,
                    ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Exercises",
                        style = MaterialTheme.typography.titleLarge,
                        modifier =
                            Modifier
                                .padding(16.dp)
                                .weight(1f),
                    )
                    OutlinedButton(
                        onClick = { addExerciseDialogShown.value = true },
                        modifier =
                            Modifier
                                .padding(end = 8.dp)
                                .testTag("addNewExerciseButton"),
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                        Text(text = "Create")
                    }
                }
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(existingExercises) { exercise ->
                        val isChecked = selectedExercises.contains(exercise)
                        ListItem(
                            headlineContent = {
                                Text(text = exercise.name)
                            },
                            leadingContent = {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = null,
                                    modifier = Modifier.testTag("addExerciseCheckbox"),
                                )
                            },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isChecked) {
                                            selectedExercises.remove(exercise)
                                        } else {
                                            selectedExercises.add(exercise)
                                        }
                                    }.testTag("addExerciseListItem"),
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .weight(1f),
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { onExercisesSelected(selectedExercises) },
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .weight(1f)
                                .testTag("addDialogSubmitButton"),
                    ) {
                        Text("Add Selected")
                    }
                }
            }
        }
    }
}
