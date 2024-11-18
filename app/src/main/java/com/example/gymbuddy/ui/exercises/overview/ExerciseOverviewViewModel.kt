package com.example.gymbuddy.ui.exercises.overview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class ExerciseOverviewUiState {
    data object NoExercises : ExerciseOverviewUiState()

    data object Exercises : ExerciseOverviewUiState()
}

interface ExerciseOverviewViewModelContract {
    val exercises: SnapshotStateList<WorkoutExercise>
    val uiState: MutableStateFlow<ExerciseOverviewUiState>

    fun deleteExercise(exerciseId: Long)
}

@HiltViewModel
class ExerciseOverviewViewModel
@Inject
constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel(),
    ExerciseOverviewViewModelContract {
    override val exercises: SnapshotStateList<WorkoutExercise> = mutableStateListOf()

    override var uiState: MutableStateFlow<ExerciseOverviewUiState> =
        MutableStateFlow(ExerciseOverviewUiState.NoExercises)
        private set

    init {
        viewModelScope.launch {
            workoutRepository
                .getAllExercises()
                .catch {
                    exercises.clear()
                    uiState.value = ExerciseOverviewUiState.NoExercises
                }.collect {
                    exercises.clear()
                    exercises.addAll(it)

                    uiState.value =
                        if (it.isNotEmpty()) {
                            ExerciseOverviewUiState.Exercises
                        } else {
                            ExerciseOverviewUiState.NoExercises
                        }
                }
        }
    }

    override fun deleteExercise(exerciseId: Long) {
        viewModelScope.launch {
            workoutRepository.deleteExercise(exerciseId)
        }
    }
}
