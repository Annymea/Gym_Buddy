package com.example.GymBuddy.ui.dashboard

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.GymBuddy.R
import com.example.GymBuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.GymBuddy.ui.createPlan.CreatePlanViewModel
import com.example.GymBuddy.ui.theme.Gym_BuddyTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun Dashboard(
    modifier: Modifier,
    onCreatePlanButtonClicked: () -> Unit
) {
    FirstButtons(
        modifier = modifier,
        onCreatePlanButtonClicked = onCreatePlanButtonClicked
    )
}

@Composable
fun PlanCard(
    modifier: Modifier,
    createPlanViewModel: CreatePlanViewModel = koinViewModel<CreatePlanViewModel>()
) {
    Log.d("TEST", "PlanCard: $createPlanViewModel.planName")

    Card(modifier = Modifier) {
        TextField(
            value = createPlanViewModel.planName,
            onValueChange = { createPlanViewModel.planName = it },
            label = { Text("Label") }
        )
    }
}

@Composable
fun AddExercise(
    modifier: Modifier,
    createPlanViewModel: CreatePlanViewModel = koinViewModel<CreatePlanViewModel>()
) {
    var tempExerciseName by remember { mutableStateOf("") }
    var tempSets by remember { mutableIntStateOf(0) }
    var tempOrder by remember { mutableIntStateOf(0) }
    Log.d("TEST", "AddExercise:")

    Column {
        Card(modifier = Modifier) {
            Column(modifier = Modifier) {
                TextField(
                    value = tempExerciseName,
                    onValueChange = { tempExerciseName = it },
                    label = { Text("Exercise name") }
                )
                TextField(
                    value = tempSets.toString(),
                    onValueChange = { tempSets = it.toInt() },
                    label = { Text("Sets") }
                )
                TextField(
                    value = tempOrder.toString(),
                    onValueChange = { tempOrder = it.toInt() },
                    label = { Text("Position") }
                )
            }
        }
        Button(
            onClick = {
                Log.d("TEST", "exercice name: $tempExerciseName")
                Log.d("TEST", "sets: $tempSets")
                Log.d("TEST", "order: $tempOrder")

                try {
                    createPlanViewModel.addExercise(
                        ExecutablePlanWithDetails(
                            exerciseName = tempExerciseName,
                            sets = tempSets,
                            order = tempOrder,
                            exerciseId = 0,
                            executablePlanId = 0,
                            planId = 0,
                            planName = createPlanViewModel.planName
                        )
                    )
                } catch (e: Exception) {
                    Log.e("TEST", "Fehler beim Hinzufügen der Übung: ${e.message}")
                }
            },
            content = { Text("Add exercise") }
        )

        createPlanViewModel.getExercises().forEach { exercise ->
            Card(modifier = Modifier) {
                Text(text = exercise.exerciseName)
            }
        }
    }
}

@Composable
fun FirstButtons(
    modifier: Modifier,
    createPlanViewModel: CreatePlanViewModel = koinViewModel<CreatePlanViewModel>(),
    onCreatePlanButtonClicked: () -> Unit
) {
    Column(modifier = modifier) {
        Button(
            onClick = {
                onCreatePlanButtonClicked()
                // createPlanViewModel.createPlan = true
                Log.d("TEST", "FirstButtons: ${createPlanViewModel.createPlan}")
            }
        ) {
            Text(text = stringResource(R.string.create_plan))
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.start_training))
        }

        if (createPlanViewModel.createPlan) {
            Log.d("TEST", "bin im if")
            PlanCard(Modifier)
            AddExercise(Modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Gym_BuddyTheme {
        FirstButtons(Modifier, onCreatePlanButtonClicked = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PlanCardPreview() {
    Gym_BuddyTheme {
        PlanCard(Modifier)
    }
}
