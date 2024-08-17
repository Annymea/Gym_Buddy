package com.example.GymBuddy.ui.createPlan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePlan(
    modifier: Modifier = Modifier,
    createPlanViewModel: CreatePlanViewModel = koinViewModel<CreatePlanViewModel>()
) {
    Column(modifier = modifier) {
        Text(text = "Neue Übung", modifier = modifier)
        AddExercise(
            onExerciseChange = { createPlanViewModel.updateExercise(it) },
            onAddNewExercise = { createPlanViewModel.addExercise(createPlanViewModel.exerciseToAdd) },
            exerciseToAdd = createPlanViewModel.exerciseToAdd
        )
        Text(text = "Dein Plan", modifier = modifier)
        createPlanViewModel.exerciseListToBeSaved.forEach { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
fun ExerciseCard(
    modifier: Modifier = Modifier,
    exercise: Exercise
) {
    Card(modifier = modifier) {
        Column {
            Text(text = exercise.name)
            Text(text = exercise.sets.toString())
        }
    }
}

@Composable
fun AddExercise(
    modifier: Modifier = Modifier,
    exerciseToAdd: Exercise,
    onExerciseChange: (Exercise) -> Unit,
    onAddNewExercise: () -> Unit
) {
    Card(modifier = modifier) {
        Row {
            Column {
                TextField(
                    value = exerciseToAdd.name,
                    onValueChange = {
                        onExerciseChange(exerciseToAdd.copy(name = it))
                    },
                    label = { Text("Exercise name") }
                )
                TextField(
                    value = exerciseToAdd.sets.toString(),
                    onValueChange = {
                        onExerciseChange(exerciseToAdd.copy(sets = it.toIntOrNull() ?: 0))
                    },
                    label = { Text("Sets") }
                )
            }

            Button(onClick = { onAddNewExercise() }) {
                Text(text = "Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExercisePreview() {
    AddExercise(
        modifier = Modifier,
        onExerciseChange = {},
        onAddNewExercise = {},
        exerciseToAdd = Exercise(name = "Push-up", sets = 3)
    )
}
// hier möchte ich jetzt nach und nach neue Übungen hinzufügen.
// oben soll ein save button sein. Wenn ein user den Screen verlässt ohne, dass er diesen Button drück
// soll er gewarnt werden und gefragt werden, ob er das ganze behalten will oder lieber nicht.
