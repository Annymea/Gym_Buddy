package com.example.gymbuddy.data.localdatabase

import com.example.gymbuddy.data.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class LocalDataRepository(
    private val workoutDAO: WorkoutDAO
) : WorkoutRepository {
    override suspend fun getAllExerciseNames(): Flow<List<Exercise>> {
        return workoutDAO.getAllExerciseNames()
    }

    override suspend fun getAllPlanNames(): Flow<List<Plan>> {
        return workoutDAO.getAllPlanNames()
    }

    override suspend fun getPlanById(planId: Long): Flow<Plan> {
        return workoutDAO.getPlanById(planId)
    }

    override suspend fun getExecutablePlanById(planId: Long): Flow<List<ExecutablePlan>> {
        return workoutDAO.getExecutablePlanById(planId)
    }

    override suspend fun getExecutionsById(exerciseId: Long): Flow<List<Execution>> {
        return workoutDAO.getExecutionsById(exerciseId)
    }

    override suspend fun getExecutablePlanWithDetailsByPlanId(planId: Long): Flow<List<ExecutablePlanWithDetails>> {
        return workoutDAO.getExecutablePlanWithDetailsByPlanId(planId)
    }

    override suspend fun insertPlan(plan: Plan): Long {
        return workoutDAO.insertPlan(plan)
    }

    override suspend fun insertExercise(exercise: Exercise): Long {
        return workoutDAO.insertExercise(exercise)
    }

    override suspend fun insertExecutablePlan(executablePlan: ExecutablePlan): Long {
        return workoutDAO.insertExecutablePlan(executablePlan)
    }

    override suspend fun insertExecution(execution: Execution): Long {
        return workoutDAO.insertExecution(execution)
    }

    override suspend fun deletePlan(plan: Plan) {
        return workoutDAO.deletePlan(plan)
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        return workoutDAO.deleteExercise(exercise)
    }

    override suspend fun deleteExerciseFromPlan(executablePlan: ExecutablePlan) {
        return workoutDAO.deleteExerciseFromPlan(executablePlan)
    }

    override suspend fun deleteExecution(execution: Execution) {
        return workoutDAO.deleteExecution(execution)
    }
}
