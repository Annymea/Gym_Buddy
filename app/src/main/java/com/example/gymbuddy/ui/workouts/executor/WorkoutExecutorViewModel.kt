package com.example.gymbuddy.ui.workouts.executor

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.WorkoutSet
import java.time.Instant
import kotlinx.coroutines.launch

class WorkoutExecutorViewModel(
    private val workoutRepository: WorkoutRepository,
    private val workoutId: String
) : ViewModel() {
    var workout: MutableState<Workout?> = mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            val foundWorkout = workoutRepository.getWorkout(workoutId.toLong())

            if (foundWorkout == null) {
                Log.e("WorkoutExecutor", "Workout not found")
                return@launch
            }
            Log.i("WorkoutExecutor", "Found workout: $foundWorkout")
            workout.value = foundWorkout
            addSetsToExercises()
            Log.i("WorkoutExecutor", "Workout: ${workout.value!!.name}")
            Log.i("WorkoutExecutor", "Workout: ${workout.value!!.exercises.size}")
        }
    }

    private fun addSetsToExercises() {
        if (workout.value?.exercises == null || workout.value?.exercises?.isEmpty() == true) {
            return
        }
        for (exercise in workout.value?.exercises!!) {
            if (exercise.sets.isEmpty() || exercise.sets.size < exercise.setCount - 1) {
                for (i in 1..exercise.setCount) {
                    addSet(exercise)
                }
            }
        }
    }

    fun saveExecutions() {
        viewModelScope.launch {
            val currentTime = Instant.now().toEpochMilli()

            try {
                workoutRepository.saveWorkoutExecution(workout.value!!.exercises, currentTime)
            } catch (e: Exception) {
                Log.e("WorkoutExecutor", "Error saving workout execution: ${e.message}")
            }
        }
    }

    fun updateWorkout() {
        viewModelScope.launch {
            try {
                workoutRepository.updateWorkout(workout.value!!)
            } catch (e: Exception) {
                Log.e("WorkoutExecutor", "Error updating workout: ${e.message}")
            }
        }
    }

    fun addSet(exercise: WorkoutExercise) {
        val newSet =
            WorkoutSet(
                order = exercise.sets.size + 1,
                reps = 0,
                weight = 0f
            )
        exercise.sets += newSet
    }

    fun updateSet(
        set: WorkoutSet,
        exerciseIndex: Int
    ) {
        workout.value?.exercises?.get(exerciseIndex)?.let { exercise ->
            val index = exercise.sets.indexOfFirst { it.order == set.order }
            if (index != -1) {
                exercise.sets[index] = set
            }
        }
    }

    fun deleteExercise(exerciseIndex: Int) {
        workout.value?.exercises?.removeAt(exerciseIndex)
    }
}
