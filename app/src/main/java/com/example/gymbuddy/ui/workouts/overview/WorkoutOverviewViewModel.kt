package com.example.gymbuddy.ui.workouts.overview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class WorkoutOverviewUiState {
    data object NoWorkouts : WorkoutOverviewUiState()

    data object Workouts : WorkoutOverviewUiState()
}

interface WorkoutOverviewViewModelContract {
    val workouts: List<Workout>
    val uiState: StateFlow<WorkoutOverviewUiState>
    fun onReorder(newWorkouts: List<Workout>)
}

@HiltViewModel
class WorkoutOverviewViewModel
@Inject
constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel(),
    WorkoutOverviewViewModelContract {
    override var workouts: SnapshotStateList<Workout> = mutableStateListOf()
        private set

    override var uiState =
        MutableStateFlow<WorkoutOverviewUiState>(WorkoutOverviewUiState.NoWorkouts)
        private set

    init {
        viewModelScope.launch {
            workoutRepository
                .getAllWorkoutDetails()
                .catch {
                    workouts.clear()
                    uiState.value = WorkoutOverviewUiState.NoWorkouts
                }.collect { foundWorkouts ->
                    workouts.clear()
                    workouts.addAll(foundWorkouts)

                    if (workouts.isNotEmpty()) {
                        uiState.value = WorkoutOverviewUiState.Workouts
                    } else {
                        uiState.value = WorkoutOverviewUiState.NoWorkouts
                    }
                }
        }
    }

    override fun onReorder(newWorkouts: List<Workout>) {
        workouts.clear()
        workouts.addAll(newWorkouts)

        val workoutIds = newWorkouts.map { it.id }

        viewModelScope.launch {
            workoutRepository.updateWorkoutOrder(workoutIds)
        }
    }
}
