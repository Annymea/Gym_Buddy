package com.example.gymbuddy.ui.common.addExerciseDialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

interface AddExerciseDialogViewModelContract {
    fun onSaveExercise(newExercise: WorkoutExercise)
}

@HiltViewModel
class AddExerciseDialogViewModel
@Inject
constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel(),
    AddExerciseDialogViewModelContract {
    override fun onSaveExercise(newExercise: WorkoutExercise) {
        viewModelScope.launch {
            try {
                workoutRepository.addExercise(newExercise)
            } catch (e: Exception) {
                Log.d("AddExerciseDialogViewModel", "Error saving exercise:")
            }
        }
    }
}
