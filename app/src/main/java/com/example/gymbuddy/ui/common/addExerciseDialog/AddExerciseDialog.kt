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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseDialog(
    onDismissRequest: () -> Unit,
    viewModel: AddExerciseDialogViewModel = koinViewModel<AddExerciseDialogViewModel>()
) {
    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        properties =
        DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
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
                    value = viewModel.newExercise.value!!.name,
                    modifier = Modifier,
                    onValueChange = { viewModel.onNameChange(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    OutlinedButton(
                        onClick = {
                            onDismissRequest()
                        },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            viewModel.onSaveExercise()
                            onDismissRequest()
                        },
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}
