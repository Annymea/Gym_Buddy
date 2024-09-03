package com.example.GymBuddy.ui.createPlan

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePlan(
    modifier: Modifier = Modifier,
    createPlanViewModel: CreatePlanViewModelContract = koinViewModel<CreatePlanViewModel>(),
    onPlanSaved: () -> Unit
) {
    var showErrorToast by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row {
            TextField(
                value = createPlanViewModel.planName.value,
                onValueChange = { newPlanName ->
                    createPlanViewModel.updatePlanName(newPlanName)
                },
                label = { Text("Plan name") }
            )
            when (createPlanViewModel.saveState.value) {
                is SavingPlanState.Error, SavingPlanState.Idle -> {
                    Button(
                        onClick = {
                            createPlanViewModel.savePlanToDatabase()
                        }
                    ) {
                        Text(text = "Save Plan")
                    }
                }

                is SavingPlanState.Saving -> {
                    Text(text = "Saving...")
                    // CircularProgressIndicator(
                    //    modifier = Modifier.align(
                    //        Alignment.CenterVertically
                    //    )
                    // )
                }

                is SavingPlanState.Saved -> {
                    onPlanSaved()
                }
            }
        }

        Text(text = "Neue Übung", modifier = modifier)

        AddExercise(
            onExerciseChange = { createPlanViewModel.updateExercise(it) },
            onAddNewExercise = {
                createPlanViewModel.addExercise(createPlanViewModel.exerciseToAdd.value)
                createPlanViewModel.updateExercise(ViewModelExercise("", 0))
            },
            exerciseToAdd = createPlanViewModel.exerciseToAdd.value
        )

        Text(text = "Dein Plan", modifier = modifier)

        LazyColumn {
            items(createPlanViewModel.exerciseListToBeSaved) { exercise ->
                ExerciseCard(exercise = exercise)
            }
        }

        if (createPlanViewModel.saveState.value is SavingPlanState.Error) {
            LaunchedEffect(Unit) {
                showErrorToast = true
            }
        }

        if (showErrorToast) {
            Toast.makeText(
                LocalContext.current,
                (createPlanViewModel.saveState.value as? SavingPlanState.Error)?.message
                    ?: "An error occurred",
                Toast.LENGTH_SHORT
            ).show()
            showErrorToast = false
            createPlanViewModel.resetErrorState()
        }
    }
}

@Composable
fun ExerciseCard(
    modifier: Modifier = Modifier,
    exercise: ViewModelExercise
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
    exerciseToAdd: ViewModelExercise,
    onExerciseChange: (ViewModelExercise) -> Unit,
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
