package com.example.gymbuddy.ui.old.createPlan

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.ExecutablePlan
import com.example.gymbuddy.data.localdatabase.Exercise
import com.example.gymbuddy.data.localdatabase.Plan
import kotlinx.coroutines.launch

data class ViewModelExercise(
    val name: String,
    val sets: Int
)

sealed class SavingPlanState {
    object Idle : SavingPlanState()
    object Saving : SavingPlanState()
    object Saved : SavingPlanState()
    data class Error(val message: String) : SavingPlanState()
}

interface CreatePlanViewModelContract {
    val exerciseListToBeSaved: SnapshotStateList<ViewModelExercise>
    val exerciseToAdd: MutableState<ViewModelExercise>
    val planName: MutableState<String>
    val saveState: MutableState<SavingPlanState>

    fun resetErrorState()
    fun addExercise(exercise: ViewModelExercise)
    fun updateExercise(updatedExercise: ViewModelExercise)
    fun updatePlanName(newPlanName: String)
    fun savePlanToDatabase()
}

class CreatePlanViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel(), CreatePlanViewModelContract {
    override var exerciseListToBeSaved = mutableStateListOf<ViewModelExercise>()
        private set
    override var exerciseToAdd = mutableStateOf(ViewModelExercise(name = "", sets = 0))
        private set
    override var planName = mutableStateOf("")
        private set

    override var saveState = mutableStateOf<SavingPlanState>(SavingPlanState.Idle)
        private set

    override fun resetErrorState() {
        saveState.value = SavingPlanState.Idle
    }

    override fun addExercise(exercise: ViewModelExercise) {
        exerciseListToBeSaved.add(exercise)
    }

    override fun updateExercise(updatedExercise: ViewModelExercise) {
        exerciseToAdd.value = updatedExercise
    }

    override fun updatePlanName(newPlanName: String) {
        planName.value = newPlanName
    }

    override fun savePlanToDatabase() {
        saveState.value = SavingPlanState.Saving
        viewModelScope.launch {
            try {
                if (planName.value.isEmpty()) {
                    saveState.value = SavingPlanState.Error("Plan name cannot be empty")
                    return@launch
                }
                val planId = addPlanToDatabase(planName.value)

                exerciseListToBeSaved.forEachIndexed { index, exercise ->
                    try {
                        addExerciseToDatabase(
                            exerciseName = exercise.name,
                            planId = planId,
                            index = index,
                            sets = exercise.sets
                        )
                    } catch (e: Exception) {
                        val error = "Failed to add exercise: ${e.message}"
                        saveState.value = SavingPlanState.Error(error)

                        return@launch
                    }
                }

                saveState.value = SavingPlanState.Saved
            } catch (e: Exception) {
                val error = "Failed to add plan to database: ${e.message}"
                saveState.value = SavingPlanState.Error(error)
            }
        }
    }

    private suspend fun addPlanToDatabase(planName: String): Long {
        return workoutRepository.insertPlan(Plan(planName = planName))
    }

    private suspend fun addExerciseToDatabase(
        exerciseName: String,
        planId: Long,
        index: Int,
        sets: Int
    ) {
        val exerciseId = workoutRepository.insertExercise(Exercise(exerciseName = exerciseName))

        addExecutablePlanToDatabase(
            planId = planId,
            exerciseId = exerciseId,
            sets = sets,
            order = index
        )
    }

    private suspend fun addExecutablePlanToDatabase(
        planId: Long,
        exerciseId: Long,
        sets: Int,
        order: Int
    ) {
        workoutRepository.insertExecutablePlan(
            ExecutablePlan(
                planId = planId,
                exerciseId = exerciseId,
                sets = sets,
                order = order
            )
        )
    }
}
