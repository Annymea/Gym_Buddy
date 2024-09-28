package com.example.gymbuddy.data.localdatabase

import com.example.gymbuddy.data.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class LocalDataRepository(
    private val workoutDAO: WorkoutDAO
) : WorkoutRepository {
    override suspend fun getAllExerciseNames(): Flow<List<Exercise>> =
        workoutDAO.getAllExerciseNames()

    override suspend fun getAllPlanNames(): Flow<List<Plan>> = workoutDAO.getAllPlanNames()

    override suspend fun getPlanById(planId: Long): Flow<Plan> = workoutDAO.getPlanById(planId)

    override suspend fun getExecutablePlanById(planId: Long): Flow<List<ExecutablePlan>> =
        workoutDAO.getExecutablePlanById(
            planId
        )

    override suspend fun getExecutionsById(exerciseId: Long): Flow<List<Execution>> =
        workoutDAO.getExecutionsById(
            exerciseId
        )

    override suspend fun getPlanWithDetailsBy(planId: Long): Flow<List<ExecutablePlanWithDetails>> =
        workoutDAO.getExecutablePlanWithDetailsByPlanId(planId)

    override suspend fun insertPlan(plan: Plan): Long = workoutDAO.insertPlan(plan)

    override suspend fun insertExercise(exercise: Exercise): Long =
        workoutDAO.insertExercise(
            exercise
        )

    override suspend fun insertExecutablePlan(executablePlan: ExecutablePlan): Long =
        workoutDAO.insertExecutablePlan(
            executablePlan
        )

    override suspend fun insertExecution(execution: Execution): Long =
        workoutDAO.insertExecution(
            execution
        )

    override suspend fun deletePlan(plan: Plan) = workoutDAO.deletePlan(plan)

    override suspend fun deleteExercise(exercise: Exercise) = workoutDAO.deleteExercise(exercise)

    override suspend fun deleteExerciseFromPlan(executablePlan: ExecutablePlan) =
        workoutDAO.deleteExerciseFromPlan(
            executablePlan
        )

    override suspend fun deleteExecution(execution: Execution) =
        workoutDAO.deleteExecution(
            execution
        )
}
