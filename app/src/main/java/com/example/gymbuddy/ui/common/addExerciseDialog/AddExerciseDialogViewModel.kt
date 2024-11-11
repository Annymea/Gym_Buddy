package com.example.gymbuddy.ui.common.addExerciseDialog

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import kotlinx.coroutines.launch

class AddExerciseDialogViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    var newExercise: MutableState<WorkoutExercise?> =
        mutableStateOf(
            WorkoutExercise(
                name = "",
                order = 0
            )
        )
        private set

    fun onNameChange(name: String) {
        newExercise.value = newExercise.value?.copy(name = name)
    }

    fun onSaveExercise() {
        viewModelScope.launch {
            try {
                workoutRepository.addExercise(newExercise.value!!)
            } catch (e: Exception) {
                Log.d("AddExerciseDialogViewModel", "Error saving exercise:")
            }
        }
    }
}
