package com.example.GymBuddy.ui.createPlan

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

class CreatePlanViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    var exerciseListToBeSaved = mutableStateListOf<ViewModelExercise>()
        private set
    var exerciseToAdd = mutableStateOf(ViewModelExercise(name = "", sets = 0))
        private set
    var planName = mutableStateOf("")
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
        viewModelScope.launch {
            val planId = workoutRepository.insertPlan(Plan(planName = planName.value))
            exerciseListToBeSaved.forEachIndexed { index, exercise ->
                val exerciseId =
                    workoutRepository.insertExercise(Exercise(exerciseName = exercise.name))
                workoutRepository.insertExecutablePlan(
                    ExecutablePlan(
                        planId = planId,
                        exerciseId = exerciseId,
                        sets = exercise.sets,
                        order = index
                    )
                )
            }
        }
    }
}
