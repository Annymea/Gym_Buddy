package com.example.GymBuddy.ui.runningWorkout

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.ExecutablePlanWithDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RunningWorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val workoutId: String
) : ViewModel() {
    private var _exercises: SnapshotStateList<ExecutablePlanWithDetails> = mutableStateListOf()
    val exercises: List<ExecutablePlanWithDetails>
        get() = _exercises

    private var _planName: MutableState<String> = mutableStateOf("")
    val planName: MutableState<String>
        get() = _planName

    init {
        viewModelScope.launch {
            val exerciseList =
                workoutRepository.getExecutablePlanWithDetailsByPlanId(workoutId.toLong())
                    .first()
            _exercises.addAll(exerciseList)

            _planName.value = workoutRepository.getPlanById(workoutId.toLong()).first().planName
        }
    }
}
