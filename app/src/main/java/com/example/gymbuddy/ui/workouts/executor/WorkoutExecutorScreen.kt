package com.example.gymbuddy.ui.workouts.executor

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.gymbuddy.R
import com.example.gymbuddy.ui.workouts.common.ConfirmationDialog
import com.example.gymbuddy.ui.workouts.common.ScreenTitle

@Composable
fun WorkoutExecutorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        ScreenTitle(text = "Workout Name")

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            modifier = Modifier.weight(1f),
        ) {
            item {
                ExerciseCard()
            }
            item {
                ExerciseCard()
            }
            item {
                ExerciseCard()
            }
        }

        FinishAndCancelButton(
            modifier = Modifier.padding(16.dp),
            saveButtonEnabled = true,
            onFinishWithChanges = {
            },
            onFinishWithoutChanges = {
            },
            onCancelButtonClicked = {
            },
        )
    }
}

@Composable
private fun FinishAndCancelButton(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onFinishWithoutChanges: () -> Unit = {},
    onFinishWithChanges: () -> Unit = {},
    saveButtonEnabled: Boolean,
) {
    var showFinishDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showFinishDialog) {
        FinishWorkoutDialog(
            onFinishWithoutChanges = { onFinishWithoutChanges() },
            onFinishWithChanges = { onFinishWithChanges() },
            onDismissRequest = { showFinishDialog = false },
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
            },
        )
    }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        OutlinedButton(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
            onClick = { showCancelDialog = true },
        ) {
            Text(text = stringResource(R.string.workout_editor_cancel_button_text))
        }

        Button(
            enabled = saveButtonEnabled,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
            onClick = { showFinishDialog = true },
        ) {
            Text(text = stringResource(R.string.workout_editor_save_button_text))
        }
    }
}

@Composable
fun ExerciseCard(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(start = 16.dp, top = 8.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Name of exercise 1",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Sets",
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Reps",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Weight",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
            }

            Column(modifier = Modifier) {
                ExecutionRow(
                    set = 1,
                    reps = 4,
                    weight = 55f,
                )
                ExecutionRow(
                    set = 2,
                    reps = 4,
                    weight = 512f,
                )
                ExecutionRow(
                    set = 3,
                    reps = 4,
                    weight = 512f,
                )
            }

            TextButton(onClick = { /*TODO*/ }) {
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
    onDismissRequest: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        content = {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                ) {
                    Text(
                        text = "Finish workout",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Text(
                        text = "Do you want to save the changes on the workout?",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

                    Button(
                        onClick = {
                            onFinishWithoutChanges()
                            onDismissRequest()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Finish without changes")
                    }

                    Button(
                        onClick = {
                            onFinishWithChanges()
                            onDismissRequest()
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    ) {
                        Text("Finish with changes")
                    }

                    OutlinedButton(
                        onClick = {
                            onDismissRequest()
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    ) {
                        Text("Cancel")
                    }
                }
            }
        },
    )
}

@Composable
fun ExecutionRow(
    modifier: Modifier = Modifier,
    set: Int,
    reps: Int,
    weight: Float,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = set.toString(),
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            OutlinedTextField(
                singleLine = true,
                label = {},
                value = reps.toString(),
                onValueChange = { },
                modifier = Modifier.width(80.dp),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    ),
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            OutlinedTextField(
                singleLine = true,
                label = {},
                value = weight.toString(),
                onValueChange = { },
                modifier = Modifier.width(80.dp),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseCardPreview() {
    ExerciseCard()
}

@Preview(showBackground = true)
@Composable
fun WorkoutExecutorScreenPreview() {
    WorkoutExecutorScreen()
}

@Preview(showBackground = true)
@Composable
fun FinishWorkoutDialogPreview() {
    FinishWorkoutDialog(
        onFinishWithoutChanges = { /*TODO*/ },
        onFinishWithChanges = { /*TODO*/ },
        onDismissRequest = { /*TODO*/ },
    )
}
