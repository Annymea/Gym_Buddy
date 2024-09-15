package com.example.GymBuddy.ui.runningWorkout

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.GymBuddy.data.localdatabase.Execution
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ExerciseExecution(
    val set: Int,
    var weight: Int,
    var reps: Int,
    val exerciseId: Long
)

interface RunningWorkoutViewModelContract {
    val exercises: List<ExecutablePlanWithDetails>
    val planName: MutableState<String>

    fun getExecutions(exerciseId: Long): List<ExerciseExecution>
    fun addOrUpdateExecution(execution: ExerciseExecution)
    fun saveExecutionsToRepository()
}

class RunningWorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val workoutId: String
) : ViewModel(), RunningWorkoutViewModelContract {
    private var _exercises: SnapshotStateList<ExecutablePlanWithDetails> =
        mutableStateListOf()
    override val exercises: List<ExecutablePlanWithDetails>
        get() = _exercises

    private var _planName: MutableState<String> = mutableStateOf("")
    override val planName: MutableState<String>
        get() = _planName

    private var _executions: SnapshotStateList<ExerciseExecution> = mutableStateListOf()

    init {
        viewModelScope.launch {
            val exerciseList =
                workoutRepository.getExecutablePlanWithDetailsByPlanId(workoutId.toLong())
                    .first()
            _exercises.addAll(exerciseList)

            _planName.value = workoutRepository.getPlanById(workoutId.toLong()).first().planName
        }
    }

    override fun getExecutions(exerciseId: Long): List<ExerciseExecution> {
        return _executions.filter { it.exerciseId == exerciseId }
    }

    override fun addOrUpdateExecution(execution: ExerciseExecution) {
        val index = _executions.indexOfFirst {
            it.exerciseId == execution.exerciseId && it.set == execution.set
        }

        if (index != -1) {
            _executions[index] = execution
        } else {
            _executions.add(execution)
        }
    }

    override fun saveExecutionsToRepository() {
        viewModelScope.launch {
            _executions.forEach {
                workoutRepository.insertExecution(
                    Execution(
                        exerciseId = it.exerciseId,
                        weight = it.weight,
                        reps = it.reps,
                        date = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
