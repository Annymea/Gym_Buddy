package com.example.gymbuddy.data.localdatabase

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutExercise
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.WorkoutSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class LocalDataRepository(
    private val workoutDAO: WorkoutDAO
) : WorkoutRepository {
    override suspend fun getWorkout(workoutId: Long): Workout? {
        val workoutDetails =
            workoutDAO.getWorkoutDetailsFor(workoutId).firstOrNull()
                ?: return null

        val workoutEntities =
            workoutDAO.getWorkoutFor(workoutId).firstOrNull()
                ?: return null

        val exercises: SnapshotStateList<WorkoutExercise> =
            workoutEntities
                .mapNotNull { workoutEntity ->
                    val exerciseDetails =
                        workoutDAO
                            .getExerciseDetailsFor(
                                workoutEntity.exerciseDetailsId
                            ).firstOrNull()
                            ?: return@mapNotNull null

                    // hier kann ich dann die previos sets einfügen
                    val sets = mutableStateListOf<WorkoutSet>()

                    WorkoutExercise(
                        id = exerciseDetails.id,
                        name = exerciseDetails.exerciseName,
                        note = exerciseDetails.note,
                        setCount = workoutEntity.sets,
                        order = workoutEntity.order,
                        sets = sets
                    )
                }.toMutableStateList()

        return Workout(
            id = workoutDetails.id,
            name = workoutDetails.workoutName,
            category = workoutDetails.category,
            note = workoutDetails.note,
            exercises = exercises
        )
    }

    override suspend fun getAllWorkoutDetails(): Flow<List<Workout>> {
        val workoutDetailsEntityList = workoutDAO.getAllWorkoutDetails()

        return workoutDetailsEntityList.map { entityList ->
            entityList.map { entity ->
                Workout(
                    id = entity.id,
                    name = entity.workoutName,
                    category = entity.category,
                    note = entity.note,
                    exercises = mutableStateListOf()
                )
            }
        }
    }

    override suspend fun saveWorkoutExecution(
        doneExercises: List<WorkoutExercise>,
        date: Long
    ) {
        val executionEntities =
            doneExercises.flatMap { exercise ->
                exercise.sets.map { set ->
                    ExecutionEntity(
                        exerciseDetailsId = exercise.id,
                        weight = set.weight,
                        reps = set.reps,
                        date = date
                    )
                }
            }
        workoutDAO.saveWorkoutExecution(executionEntities)
    }

    override suspend fun updateWorkout(newWorkout: Workout) {
        val currentWorkoutEntities =
            workoutDAO.getExercisesFor(newWorkout.id).firstOrNull() ?: emptyList()

        val newWorkoutEntities =
            newWorkout.exercises.map { exercise ->
                WorkoutEntity(
                    workoutDetailsId = newWorkout.id,
                    exerciseDetailsId = exercise.id,
                    sets = exercise.setCount,
                    order = exercise.order
                )
            }

        val workoutEntitiesToDelete =
            currentWorkoutEntities.filter { currentEntity ->
                newWorkoutEntities.none { newEntity ->
                    newEntity.exerciseDetailsId == currentEntity.exerciseDetailsId &&
                        newEntity.order == currentEntity.order
                }
            }

        val workoutEntitiesToUpdate =
            newWorkoutEntities.filter { newEntity ->
                currentWorkoutEntities.any { currentEntity ->
                    newEntity.exerciseDetailsId == currentEntity.exerciseDetailsId &&
                        newEntity.order == currentEntity.order
                }
            }

        val workoutEntitiesToInsert =
            newWorkoutEntities.filter { newEntity ->
                currentWorkoutEntities.none { currentEntity ->
                    newEntity.exerciseDetailsId == currentEntity.exerciseDetailsId &&
                        newEntity.order == currentEntity.order
                }
            }

        workoutDAO.deleteWorkoutEntities(workoutEntitiesToDelete)

        workoutDAO.updateWorkoutEntities(workoutEntitiesToUpdate)

        workoutDAO.insertWorkoutEntities(workoutEntitiesToInsert)
    }

    override suspend fun createNewWorkout(newWorkout: Workout) {
        val newWorkoutId =
            workoutDAO.insertWorkoutDetails(
                WorkoutDetailsEntity(
                    workoutName = newWorkout.name,
                    category = newWorkout.category,
                    note = newWorkout.note
                )
            )
        // workaround until i implemented the correct workflow for exercises
        // later no new exercises should be created here
        newWorkout.exercises.forEach { exercise ->
            val newExerciseId =
                workoutDAO.insertExerciseDetails(
                    ExerciseDetailsEntity(
                        exerciseName = exercise.name,
                        note = exercise.note,
                        category = exercise.category
                    )
                )

            workoutDAO.insertWorkoutEntity(
                WorkoutEntity(
                    workoutDetailsId = newWorkoutId,
                    exerciseDetailsId = newExerciseId,
                    sets = exercise.setCount,
                    order = exercise.order
                )
            )
        }
    }
}
