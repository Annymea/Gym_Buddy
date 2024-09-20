package com.example.gymbuddy.ui.workouts.overview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.Plan
import kotlinx.coroutines.launch

interface WorkoutOverviewViewModelContract {
    val workouts: List<Plan>
}

class WorkoutOverviewViewModel(workoutRepository: WorkoutRepository) :
    WorkoutOverviewViewModelContract, ViewModel() {
    private var _workouts: SnapshotStateList<Plan> = mutableStateListOf()
    override val workouts: List<Plan>
        get() = _workouts

    init {
        viewModelScope.launch {
            workoutRepository.getAllPlanNames().collect { workouts ->
                _workouts.clear()
                _workouts.addAll(workouts)
            }
        }
    }
}
