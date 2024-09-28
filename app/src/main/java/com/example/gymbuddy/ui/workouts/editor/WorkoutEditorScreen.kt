package com.example.gymbuddy.ui.workouts.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymbuddy.R
import com.example.gymbuddy.ui.workouts.common.ScreenTitle

@Composable
fun WorkoutEditorScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenTitle(text = "New Workout")

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
                value =
                stringResource(R.string.workout_editor_initial_workout_title),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn {
                item {
                    AddExerciseCard(modifier = Modifier.padding(top = 16.dp))
                }
            }

            AddExerciseButton()
        }

        SaveAndCancelButton(modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun SaveAndCancelButton(modifier: Modifier = Modifier) {
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
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Cancel")
        }

        Button(
            modifier =
            Modifier
                .weight(1f)
                .padding(start = 16.dp),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
private fun AddExerciseButton(modifier: Modifier = Modifier) {
    TextButton(
        modifier =
        modifier
            .padding(top = 16.dp)
            .height(48.dp),
        onClick = { /*TODO*/ }
    ) {
        Icon(
            modifier =
            Modifier
                .height(48.dp)
                .width(48.dp),
            imageVector = Icons.Default.AddCircle,
            contentDescription = "Add Exercise"
        )
        Text(
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            text = "Add Exercise"
        )
    }
}

@Composable
fun AddExerciseCard(modifier: Modifier = Modifier) {
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
                label = { Text(text = "Exercise Name") },
                value = "Default Exercise",
                onValueChange = {},
                modifier =
                Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )
            OutlinedTextField(
                label = { Text(text = "Sets") },
                value = "3",
                onValueChange = {},
                modifier = Modifier.width(64.dp),
                singleLine = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutEditorScreenPreview() {
    WorkoutEditorScreen()
}

@Preview(showBackground = true)
@Composable
fun AddExerciseCardPreview() {
    AddExerciseCard()
}
