package com.example.GymBuddy.ui.createPlan

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.ExecutablePlanWithDetails

class CreatePlanViewModel(workoutRepository: WorkoutRepository) : ViewModel() {
    // exercise list
    private var _exerciseListToBeSaved: SnapshotStateList<Exercise> = mutableStateListOf()
    val exerciseListToBeSaved: List<Exercise>
        get() = _exerciseListToBeSaved

    // exercise to add
    private var _exerciseToAdd: MutableState<Exercise> =
        mutableStateOf(Exercise(name = "", sets = 0))

    val exerciseToAdd: Exercise
        get() = _exerciseToAdd.value

    fun addExercise(exercise: Exercise) {
        _exerciseListToBeSaved.add(exercise)
        Log.d("CreatePlanViewModel", "Exercise added: $exercise")
        Log.d("CreatePlanViewModel", "Exercise list: ${_exerciseListToBeSaved.joinToString()}")
    }

    fun updateExercise(updatedExercise: Exercise) {
        _exerciseToAdd.value = updatedExercise
    }

    var planName by mutableStateOf("")

    var createPlan by mutableStateOf(false)

    private var _exercises: SnapshotStateList<ExecutablePlanWithDetails> = mutableStateListOf()

    fun addExercise(exercise: ExecutablePlanWithDetails) {
        _exercises.add(exercise)
    }

    fun getExercises(): List<ExecutablePlanWithDetails> {
        return _exercises
    }
}
