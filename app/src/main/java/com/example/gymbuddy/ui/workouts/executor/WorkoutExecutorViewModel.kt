package com.example.gymbuddy.ui.workouts.executor

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.ExecutablePlanWithDetails
import com.example.gymbuddy.data.localdatabase.ExecutionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant

data class Workout(
    val workoutId: Long,
    val workoutName: String,
    var workoutExercises: SnapshotStateList<WorkoutExercise>,
)

data class WorkoutExercise(
    val exerciseId: Long,
    val exerciseName: String,
    var exerciseSets: SnapshotStateList<ExerciseSet>,
    var order: Int = 0,
)

data class ExerciseSet(
    val setNumber: Int,
    var reps: Int,
    var weight: Float,
)

class WorkoutExecutorViewModel(
    private val workoutRepository: WorkoutRepository,
    private val workoutId: String,
) : ViewModel() {
    var workout: MutableState<Workout?> = mutableStateOf(null)
        private set

    init {
        Log.i("WorkoutExecutor", "init")
        Log.i("WorkoutExecutor", workoutId)
        viewModelScope.launch {
            val exercises = workoutRepository.getPlanWithDetailsBy(workoutId.toLong()).first()
            Log.i("WorkoutExecutor", exercises.size.toString())
            if (workout.value == null) {
                val workoutName =
                    workoutRepository.getPlanById(workoutId.toLong()).first().workoutName
                Log.i("WorkoutExecutor", workoutName)
                workout.value =
                    Workout(
                        workoutId = workoutId.toLong(),
                        workoutName = workoutName,
                        workoutExercises = mapExercisesToWorkoutExercise(exercises),
                    )
                Log.i("WorkoutExecutor", workout.value.toString())
            }
        }
    }

    private fun mapExercisesToWorkoutExercise(
        exercises: List<ExecutablePlanWithDetails>,
    ): SnapshotStateList<WorkoutExercise> {
        val workoutExercises = mutableStateListOf<WorkoutExercise>()
        for (exercise in exercises) {
            workoutExercises +=
                WorkoutExercise(
                    exerciseName = exercise.exerciseName,
                    exerciseSets = mutableStateListOf(),
                    order = exercise.order,
                    exerciseId = exercise.exerciseId,
                )
            for (int in 1..exercise.sets) {
                workoutExercises.last().exerciseSets +=
                    ExerciseSet(
                        setNumber = int,
                        reps = 0,
                        weight = 0f,
                    )
            }
        }
        return workoutExercises
    }

    fun saveExecutions() {
        viewModelScope.launch {
            val currentTime = Instant.now().toEpochMilli()
            workout.value?.let { workout ->
                for (exercise in workout.workoutExercises) {
                    for (set in exercise.exerciseSets) {
                        if (set.reps == 0) continue
                        try {
                            workoutRepository.insertExecution(
                                ExecutionEntity(
                                    exerciseDetailsId = exercise.exerciseId,
                                    date = currentTime,
                                    reps = set.reps,
                                    weight = set.weight.toInt(),
                                ),
                            )
                        } catch (e: Exception) {
                            Log.e(
                                "WorkoutExecutor",
                                "Error saving execution: ${e.message}",
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateWorkout() {
        // ToDo
    }

    fun addSet(exercise: WorkoutExercise) {
        val newSet =
            ExerciseSet(
                setNumber = exercise.exerciseSets.size + 1,
                reps = 0,
                weight = 0f,
            )
        exercise.exerciseSets += newSet
    }

    fun updateSet(
        set: ExerciseSet,
        exerciseIndex: Int,
    ) {
        workout.value?.workoutExercises?.get(exerciseIndex)?.let { exercise ->
            val index = exercise.exerciseSets.indexOfFirst { it.setNumber == set.setNumber }
            if (index != -1) {
                exercise.exerciseSets[index] = set
            }
        }
    }

    fun deleteExercise(exerciseIndex: Int) {
        workout.value?.workoutExercises?.removeAt(exerciseIndex)
    }
}
