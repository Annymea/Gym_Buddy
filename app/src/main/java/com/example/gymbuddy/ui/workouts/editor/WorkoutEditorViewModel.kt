package com.example.gymbuddy.ui.workouts.editor

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import kotlinx.coroutines.launch

sealed class SavingWorkoutState {
    data object Idle : SavingWorkoutState()

    data object Saving : SavingWorkoutState()

    data object Saved : SavingWorkoutState()

    data class Error(
        val message: String
    ) : SavingWorkoutState()
}

class WorkoutEditorViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    var saveState = mutableStateOf<SavingWorkoutState>(SavingWorkoutState.Idle)
        private set

    var workout = mutableStateOf<Workout?>(null)
        private set

    init {
        workout.value =
            Workout(
                name = "Workout"
            )
    }

    fun addExercise(exercise: WorkoutExercise) {
        workout.value =
            workout.value?.copy(
                exercises =
                workout.value
                    ?.exercises
                    ?.toMutableList()
                    ?.apply { add(exercise) }
                    ?: mutableListOf(exercise)
            )
    }

    fun updateExercise(
        index: Int,
        exercise: WorkoutExercise
    ) {
        workout.value =
            workout.value?.copy(
                exercises =
                workout.value
                    ?.exercises
                    ?.toMutableList()
                    ?.apply { set(index, exercise) }
                    ?: mutableListOf()
            )
    }

    fun removeExercise(index: Int) {
        workout.value =
            workout.value?.copy(
                exercises =
                workout.value
                    ?.exercises
                    ?.toMutableList()
                    ?.apply { removeAt(index) }
                    ?: mutableListOf()
            )
    }

    fun updateWorkoutName(newName: String) {
        workout.value = workout.value?.copy(name = newName)
    }

    fun saveWorkout() {
        saveState.value = SavingWorkoutState.Saving
        viewModelScope.launch {
            try {
                workoutRepository.createNewWorkout(workout.value ?: return@launch)
                saveState.value = SavingWorkoutState.Saved
            } catch (e: Exception) {
                val error = "Failed to add plan to database: ${e.message}"
                saveState.value = SavingWorkoutState.Error(error)
                Log.e("WorkoutEditorViewModel", error)
            }
        }
    }
}
