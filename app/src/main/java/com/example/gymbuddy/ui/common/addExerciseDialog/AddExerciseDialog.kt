package com.example.gymbuddy.ui.common.addExerciseDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymbuddy.data.WorkoutExercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseDialog(
    onDismissRequest: () -> Unit,
    viewModel: AddExerciseDialogViewModelContract = hiltViewModel<AddExerciseDialogViewModel>()
) {
    // build here exercise to save so get a fresh start every time the dialog opens
    var newExercise by remember {
        mutableStateOf(
            WorkoutExercise(
                name = "",
                order = 0
            )
        )
    }

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        properties =
        DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        modifier = Modifier.testTag("addExerciseDialog")
    ) {
        Card {
            Column(
                modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add exercise",
                    style = MaterialTheme.typography.titleLarge,
                    modifier =
                    Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.Start)
                )

                OutlinedTextField(
                    label = {
                        Text(
                            text = "Name",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    value = newExercise.name,
                    modifier = Modifier.testTag("nameInput"),
                    onValueChange = {
                        newExercise = newExercise.copy(name = it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    OutlinedButton(
                        onClick = {
                            onDismissRequest()
                        },
                        modifier =
                        Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .testTag("cancelExerciseButton")
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        enabled = newExercise.name.isNotBlank(),
                        onClick = {
                            viewModel.onSaveExercise(newExercise)
                            onDismissRequest()
                        },
                        modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .testTag("saveExerciseButton")
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}
