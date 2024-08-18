package com.example.GymBuddy.ui.createPlan

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.ExecutablePlan
import com.example.GymBuddy.data.localdatabase.Exercise
import com.example.GymBuddy.data.localdatabase.Plan
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

class CreatePlanViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    var exerciseListToBeSaved = mutableStateListOf<ViewModelExercise>()
        private set
    var exerciseToAdd = mutableStateOf(ViewModelExercise(name = "", sets = 0))
        private set
    var planName = mutableStateOf("")
        private set

    var saveState = mutableStateOf<SavingPlanState>(SavingPlanState.Idle)
        private set

    fun addExercise(exercise: ViewModelExercise) {
        exerciseListToBeSaved.add(exercise)
    }

    fun updateExercise(updatedExercise: ViewModelExercise) {
        exerciseToAdd.value = updatedExercise
    }

    fun updatePlanName(newPlanName: String) {
        planName.value = newPlanName
    }

    fun savePlanToDatabase() {
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
                        Log.e("DatabaseError", error)

                        return@launch
                    }
                }

                saveState.value = SavingPlanState.Saved
            } catch (e: Exception) {
                val error = "Failed to add plan to database: ${e.message}"
                saveState.value = SavingPlanState.Error(error)
                Log.e("DatabaseError", error)
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
