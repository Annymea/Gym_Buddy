package com.example.gymbuddy.data.localdatabase

import com.example.gymbuddy.data.Exercise
import com.example.gymbuddy.data.Workout
import com.example.gymbuddy.data.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LocalDataRepository(
    private val workoutDAO: WorkoutDAO,
) : WorkoutRepository {
    override suspend fun getAllExerciseNames(): Flow<List<ExerciseDetailsEntity>> =
        workoutDAO.getAllExerciseNames()

    override suspend fun getAllPlanNames(): Flow<List<WorkoutDetailsEntity>> =
        workoutDAO.getAllPlanNames()

    override suspend fun getPlanById(planId: Long): Flow<WorkoutDetailsEntity> =
        workoutDAO.getPlanById(planId)

    override suspend fun getExecutablePlanById(planId: Long): Flow<List<WorkoutEntity>> =
        workoutDAO.getExecutablePlanById(
            planId,
        )

    override suspend fun getExecutionsById(exerciseId: Long): Flow<List<ExecutionEntity>> =
        workoutDAO.getExecutionsById(
            exerciseId,
        )

    override suspend fun getPlanWithDetailsBy(planId: Long): Flow<List<ExecutablePlanWithDetails>> =
        workoutDAO.getExecutablePlanWithDetailsByPlanId(planId)

    override suspend fun insertPlan(plan: WorkoutDetailsEntity): Long = workoutDAO.insertPlan(plan)

    override suspend fun insertExercise(exercise: ExerciseDetailsEntity): Long =
        workoutDAO.insertExercise(
            exercise,
        )

    override suspend fun insertExecutablePlan(executablePlan: WorkoutEntity): Long =
        workoutDAO.insertExecutablePlan(
            executablePlan,
        )

    override suspend fun insertExecution(execution: ExecutionEntity): Long =
        workoutDAO.insertExecution(
            execution,
        )

    override suspend fun deletePlan(plan: WorkoutDetailsEntity) = workoutDAO.deletePlan(plan)

    override suspend fun deleteExercise(exercise: ExerciseDetailsEntity) =
        workoutDAO.deleteExercise(exercise)

    override suspend fun deleteExercise(exerciseId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExerciseFromPlan(executablePlan: WorkoutEntity) =
        workoutDAO.deleteExerciseFromPlan(
            executablePlan,
        )

    override suspend fun deleteExecution(execution: ExecutionEntity) =
        workoutDAO.deleteExecution(
            execution,
        )

    override suspend fun getWorkout(workoutId: Long): Workout {
        val workoutDetails = workoutDAO.getWorkoutDetailsFor(workoutId).first()
        val workoutEntities = workoutDAO.getWorkoutFor(workoutId).first()

        val exercises =
            workoutEntities.map { workoutEntity ->
                val exerciseDetails =
                    workoutDAO.getExerciseDetailsFor(workoutEntity.exerciseDetailsId).first()

                Exercise(
                    id = exerciseDetails.id,
                    name = exerciseDetails.exerciseName,
                    note = exerciseDetails.note,
                    setCount = workoutEntity.sets,
                    order = workoutEntity.order,
                    sets = listOf(),
                )
            }

        return Workout(
            id = workoutDetails.id,
            name = workoutDetails.workoutName,
            category = workoutDetails.category,
            note = workoutDetails.note,
            exercises = exercises,
        )
    }

    override suspend fun createWorkout(workout: WorkoutEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun createExercise(exercise: ExerciseDetailsEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveWorkoutExecution(workoutExecution: WorkoutEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun editWorkout(updatedWorkout: WorkoutEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWorkout(workoutId: Long) {
        TODO("Not yet implemented")
    }
}
