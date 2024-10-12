package com.example.gymbuddy.ui.workouts.editor

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.ExerciseDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutEntity
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import kotlinx.coroutines.launch

data class ViewModelExercise(
    val name: String,
    val sets: Int,
)

sealed class SavingWorkoutState {
    object Idle : SavingWorkoutState()

    object Saving : SavingWorkoutState()

    object Saved : SavingWorkoutState()

    data class Error(
        val message: String,
    ) : SavingWorkoutState()
}

class WorkoutEditorViewModel(
    private val workoutRepository: WorkoutRepository,
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
        exercise: ViewModelExercise,
    ) {
        exerciseListToBeSaved[index] = exercise
    }

    fun removeExercise(index: Int) {
        exerciseListToBeSaved.removeAt(index)
    }

    fun updateWorkoutName(name: String) {
        workoutName.value = name
    }

    fun saveWorkout() {
        saveState.value = SavingWorkoutState.Saving
        viewModelScope.launch {
            try {
                val workoutId =
                    workoutRepository.insertPlan(
                        WorkoutDetailsEntity(
                            workoutName =
                                workoutName.value.takeIf { it.isNotBlank() }
                                    ?: "Workout",
                        ),
                    )

                exerciseListToBeSaved.forEachIndexed { index, exercise ->
                    val exerciseId =
                        workoutRepository.insertExercise(
                            ExerciseDetailsEntity(id = 0, exerciseName = exercise.name),
                        )

                    workoutRepository.insertExecutablePlan(
                        WorkoutEntity(
                            id = 0,
                            workoutDetailsId = workoutId,
                            exerciseDetailsId = exerciseId,
                            sets = exercise.sets,
                            order = index,
                        ),
                    )
                }
                saveState.value = SavingWorkoutState.Saved
            } catch (e: Exception) {
                val error = "Failed to add plan to database: ${e.message}"
                saveState.value = SavingWorkoutState.Error(error)
                Log.e("WorkoutEditorViewModel", error)
            }
        }
    }
}
