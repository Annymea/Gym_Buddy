package com.example.gymbuddy.ui.old.runningWorkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RunningWorkout(
    workoutId: String,
    modifier: Modifier = Modifier,
    planListViewModel: RunningWorkoutViewModelContract =
        koinViewModel<RunningWorkoutViewModel>(parameters = {
            parametersOf(workoutId)
        }),
    saveWorkout: () -> Unit = { }
) {
    Column(modifier = modifier) {
        Text(text = planListViewModel.planName.value, modifier = Modifier)

        LazyColumn {
            items(planListViewModel.exercises) { exercise ->
                ExerciseCard(
                    modifier = Modifier.testTag("exerciseCard"),
                    exerciseName = exercise.exerciseName,
                    exerciseId = exercise.exerciseId,
                    sets = exercise.sets,
                    executions = planListViewModel.getExecutions(exercise.exerciseId),
                    updateExercise = {
                        planListViewModel.addOrUpdateExecution(it)
                    }
                )
            }
        }

        Button(
            onClick = {
                planListViewModel.saveExecutionsToRepository()
                saveWorkout()
            }
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
fun ExecutionRow(
    exerciseExecution: ExerciseExecution,
    updateExercise: (ExerciseExecution) -> Unit = { }
) {
    Row() {
        TextField(
            value = exerciseExecution.reps.toString(),
            onValueChange = { newReps ->
                updateExercise(exerciseExecution.copy(reps = newReps.toIntOrNull() ?: 0))
            },
            label = { Text(text = "Reps") },
            modifier = Modifier
        )
        TextField(
            value = exerciseExecution.weight.toString(),
            onValueChange = { newWeight ->
                updateExercise(exerciseExecution.copy(weight = newWeight.toIntOrNull() ?: 0))
            },
            label = { Text(text = "Weight") },
            modifier = Modifier
        )
    }
}

@Composable
fun ExerciseCard(
    modifier: Modifier = Modifier,
    exerciseName: String,
    sets: Int,
    executions: List<ExerciseExecution>,
    exerciseId: Long,
    updateExercise: (ExerciseExecution) -> Unit = { }
) {
    Card(modifier = modifier) {
        Column {
            Text(text = exerciseName)
            repeat(sets) { setIndex ->
                val execution = executions.find { it.set == setIndex }
                    ?: ExerciseExecution(
                        set = setIndex,
                        reps = 0,
                        weight = 0,
                        exerciseId = exerciseId
                    )
                ExecutionRow(
                    exerciseExecution = execution,
                    updateExercise = updateExercise
                )
            }
        }
    }
}
