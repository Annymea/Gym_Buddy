package com.example.gymbuddy.ui.workouts.executor

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.WorkoutSet
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.launch

interface WorkoutExecutorViewModelContract {
    val workout: State<Workout?>

    fun saveExecutions()

    fun updateWorkout()

    fun addSet(exercise: WorkoutExercise)

    fun updateSet(
        set: WorkoutSet,
        exerciseIndex: Int
    )

    fun deleteExercise(exerciseIndex: Int)
}

@HiltViewModel
class WorkoutExecutorViewModel
@Inject
constructor(
    private val workoutRepository: WorkoutRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    WorkoutExecutorViewModelContract {
    private val workoutId: String =
        savedStateHandle["workoutId"]
            ?: throw IllegalArgumentException("Workout ID is required")

    override var workout: MutableState<Workout?> = mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            val foundWorkout = workoutRepository.getWorkout(workoutId.toLong())

            // What happens if the workout is null?
            if (foundWorkout == null) {
                Log.e("WorkoutExecutor", "Workout not found")
                return@launch
            }
            workout.value = foundWorkout
            addSetsToExercises()
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

    override fun saveExecutions() {
        viewModelScope.launch {
            val currentTime = Instant.now().toEpochMilli()

            try {
                workoutRepository.saveWorkoutExecution(workout.value!!.exercises, currentTime)
            } catch (e: Exception) {
                Log.e("WorkoutExecutor", "Error saving workout execution: ${e.message}")
            }
        }
    }

    override fun updateWorkout() {
        viewModelScope.launch {
            try {
                workoutRepository.updateWorkout(workout.value!!)
            } catch (e: Exception) {
                Log.e("WorkoutExecutor", "Error updating workout: ${e.message}")
            }
        }
    }

    override fun addSet(exercise: WorkoutExercise) {
        val newSet =
            WorkoutSet(
                order = exercise.sets.size + 1,
                reps = 0,
                weight = 0f
            )
        exercise.setCount += 1
        exercise.sets += newSet
    }

    override fun updateSet(
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

    override fun deleteExercise(exerciseIndex: Int) {
        workout.value?.exercises?.removeAt(exerciseIndex)
    }
}
