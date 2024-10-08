package com.example.gymbuddy.ui.old.planList

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.Plan
import kotlinx.coroutines.launch

interface PlanListViewModelContract {
    val planList: List<Plan>
}

class PlanListViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel(), PlanListViewModelContract {
    private var _planList: SnapshotStateList<Plan> = mutableStateListOf()
    override val planList: List<Plan>
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
