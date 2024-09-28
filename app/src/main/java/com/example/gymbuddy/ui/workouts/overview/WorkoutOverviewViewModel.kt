package com.example.gymbuddy.ui.workouts.overview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.Plan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class WorkoutOverviewUiState {
    data object NoWorkouts : WorkoutOverviewUiState()

    data object Workouts : WorkoutOverviewUiState()
}

interface WorkoutOverviewViewModelContract {
    val workouts: List<Plan>
    val uiState: StateFlow<WorkoutOverviewUiState>
}

class WorkoutOverviewViewModel(
    workoutRepository: WorkoutRepository,
) : ViewModel(),
    WorkoutOverviewViewModelContract {
    private var _workouts: SnapshotStateList<Plan> = mutableStateListOf()
    override val workouts: List<Plan>
        get() = _workouts

    private val _uiState =
        MutableStateFlow<WorkoutOverviewUiState>(WorkoutOverviewUiState.NoWorkouts)
    override val uiState: StateFlow<WorkoutOverviewUiState> = _uiState

    init {
        viewModelScope.launch {
            workoutRepository
                .getAllPlanNames()
                .catch {
                    _workouts.clear()
                    _uiState.value = WorkoutOverviewUiState.NoWorkouts
                }.collect { workouts ->
                    _workouts.clear()
                    _workouts.addAll(workouts)

                    if (_workouts.isNotEmpty()) {
                        _uiState.value = WorkoutOverviewUiState.Workouts
                    } else {
                        _uiState.value = WorkoutOverviewUiState.NoWorkouts
                    }
                }
        }
    }
}
