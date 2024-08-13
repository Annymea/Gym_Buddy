package com.example.GymBuddy.data.localdatabase

import com.example.GymBuddy.data.WorkoutRepository

class LocalDataRepository(
    private val workoutDAO: WorkoutDAO
) : WorkoutRepository {
    override fun getAllExerciseNames(): List<Exercise> {
        return workoutDAO.getAllExerciseNames()
    }

    override fun getAllPlanNames(): List<Plan> {
        return workoutDAO.getAllPlanNames()
    }

    override fun getExecutablePlanById(planId: Int): List<ExecutablePlan> {
        return workoutDAO.getExecutablePlanById(planId)
    }

    override fun getExecutionsById(exerciseId: Int): List<Execution> {
        return workoutDAO.getExecutionsById(exerciseId)
    }

    override fun getExecutablePlanWithDetailsByPlanId(planId: Int): List<ExecutablePlanWithDetails> {
        return workoutDAO.getExecutablePlanWithDetailsByPlanId(planId)
    }

    override suspend fun insertPlan(plan: Plan) {
        return workoutDAO.insertPlan(plan)
    }

    override suspend fun insertExercise(exercise: Exercise) {
        return workoutDAO.insertExercise(exercise)
    }

    override suspend fun insertExecutablePlan(executablePlan: ExecutablePlan) {
        return workoutDAO.insertExecutablePlan(executablePlan)
    }

    override suspend fun insertExecution(execution: Execution) {
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
