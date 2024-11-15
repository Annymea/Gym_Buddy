package com.example.gymbuddy.ui.workouts.editor

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

interface WorkoutEditorViewModelContract {
    val saveState: State<SavingWorkoutState>
    val workout: State<Workout?>

    fun addExercise(exercise: WorkoutExercise)

    fun addAllSelectedExercises(exercises: List<WorkoutExercise>)

    fun updateExercise(
        index: Int,
        exercise: WorkoutExercise
    )

    fun removeExercise(index: Int)

    fun updateWorkoutName(newName: String)

    fun saveWorkout()

    fun getExistingExercises(): List<WorkoutExercise>
}

sealed class SavingWorkoutState {
    data object Idle : SavingWorkoutState()

    data object Saving : SavingWorkoutState()

    data object Saved : SavingWorkoutState()

    data class Error(
        val message: String
    ) : SavingWorkoutState()
}

@HiltViewModel
class WorkoutEditorViewModel
@Inject
constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel(),
    WorkoutEditorViewModelContract {
    override var saveState = mutableStateOf<SavingWorkoutState>(SavingWorkoutState.Idle)
        private set

    override var workout = mutableStateOf<Workout?>(null)
        private set

    init {
        workout.value =
            Workout(
                name = "Workout"
            )
    }

    override fun addExercise(exercise: WorkoutExercise) {
        workout.value?.exercises?.add(exercise)
    }

    override fun addAllSelectedExercises(exercises: List<WorkoutExercise>) {
        workout.value?.exercises?.addAll(exercises)
    }

    override fun updateExercise(
        index: Int,
        exercise: WorkoutExercise
    ) {
        workout.value?.exercises?.set(index, exercise)
    }

    override fun removeExercise(index: Int) {
        workout.value?.exercises?.removeAt(index)
    }

    override fun updateWorkoutName(newName: String) {
        workout.value = workout.value?.copy(name = newName)
    }

    override fun saveWorkout() {
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

    override fun getExistingExercises(): List<WorkoutExercise> {
        val result = mutableStateListOf<WorkoutExercise>()
        viewModelScope.launch {
            workoutRepository.getAllExercises().collect {
                result.clear()
                result.addAll(it)
            }
        }
        Log.d("WorkoutEditorViewModel", "getExistingExercises: $result")
        return result
    }
}
