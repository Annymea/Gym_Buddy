package com.example.gymbuddy.ui.old.startWorkout

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.Plan
import kotlinx.coroutines.launch

interface StartWorkoutViewModelContract {
    val workouts: List<Plan>
}

class StartWorkoutViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel(), StartWorkoutViewModelContract {
    private var _workouts: SnapshotStateList<Plan> = mutableStateListOf()
    override val workouts: List<Plan>
        get() = _workouts

    init {
        viewModelScope.launch {
            workoutRepository.getAllPlanNames().collect { plans ->
                _workouts.clear()
                _workouts.addAll(plans)
            }
        }
    }
}
