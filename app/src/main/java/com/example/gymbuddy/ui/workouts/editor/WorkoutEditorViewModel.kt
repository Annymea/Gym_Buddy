package com.example.gymbuddy.ui.workouts.editor

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.ExecutablePlan
import com.example.gymbuddy.data.localdatabase.Exercise
import com.example.gymbuddy.data.localdatabase.Plan
import kotlinx.coroutines.launch

data class ViewModelExercise(
    val name: String,
    val sets: Int
)

sealed class SavingWorkoutState {
    object Idle : SavingWorkoutState()

    object Saving : SavingWorkoutState()

    object Saved : SavingWorkoutState()

    data class Error(
        val message: String
    ) : SavingWorkoutState()
}

class WorkoutEditorViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    var saveState = mutableStateOf<SavingWorkoutState>(SavingWorkoutState.Idle)
        private set

    var exerciseListToBeSaved = mutableStateListOf<ViewModelExercise>()
        private set

    // default name of not changed
    var workoutName = mutableStateOf("Workout")
        private set

    fun addExercise(exercise: ViewModelExercise) {
        exerciseListToBeSaved.add(exercise)
    }

    fun updateExercise(
        index: Int,
        exercise: ViewModelExercise
    ) {
        exerciseListToBeSaved[index] = exercise
    }

    fun updateWorkoutName(name: String) {
        workoutName.value = name
    }

    fun saveWorkout() {
        saveState.value = SavingWorkoutState.Saving
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                val workoutId =
                    workoutRepository.insertPlan(
                        Plan(planName = workoutName.value.takeIf { it.isNotBlank() } ?: "Workout")
                    )
                Log.i(
                    "WorkoutSave",
                    "Workout inserted in " +
                        "${System.currentTimeMillis() - startTime} ms"
                )

                exerciseListToBeSaved.forEachIndexed { index, exercise ->
                    val exerciseStartTime = System.currentTimeMillis()
                    val exerciseId =
                        workoutRepository.insertExercise(
                            Exercise(id = 0, exerciseName = exercise.name)
                        )
                    Log.i(
                        "WorkoutSave",
                        "Exercise inserted in " +
                            "${System.currentTimeMillis() - exerciseStartTime} ms"
                    )

                    workoutRepository.insertExecutablePlan(
                        ExecutablePlan(
                            id = 0,
                            planId = workoutId,
                            exerciseId = exerciseId,
                            sets = exercise.sets,
                            order = index
                        )
                    )

                    Log.i(
                        "WorkoutSave",
                        "ExecutablePlan inserted in " +
                            "${System.currentTimeMillis() - exerciseStartTime} ms"
                    )
                }
                saveState.value = SavingWorkoutState.Saved
            } catch (e: Exception) {
                val error = "Failed to add plan to database: ${e.message}"
                saveState.value = SavingWorkoutState.Error(error)
            }
        }
    }
}
