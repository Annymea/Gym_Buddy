package com.example.gymbuddy.ui.workouts.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
        fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
        modifier = modifier.padding(24.dp),
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onBackground,
        text = text
    )
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismissRequest()
                }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(cancelButtonText)
            }
        }
    )
}

@Preview
@Composable
private fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        title = "Title",
        message = "Message",
        confirmButtonText = "Confirm",
        cancelButtonText = "Cancel",
        onConfirm = {},
        onDismissRequest = {}
    )
}
