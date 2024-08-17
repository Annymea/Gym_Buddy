package com.example.GymBuddy.ui.planList

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.Plan
import kotlinx.coroutines.launch

class PlanListViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    private var _planList: SnapshotStateList<Plan> = mutableStateListOf()
    val planList: List<Plan>
        get() = _planList

    init {
        viewModelScope.launch {
            workoutRepository.getAllPlanNames().collect { plans ->
                _planList.clear()
                _planList.addAll(plans)
            }
        }
    }
}
