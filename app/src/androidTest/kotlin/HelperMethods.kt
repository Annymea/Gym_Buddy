import com.example.gymbuddy.data.localdatabase.ExerciseDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutDetailsEntity
import com.example.gymbuddy.data.localdatabase.WorkoutEntity

fun getExerciseDetails(): ExerciseDetailsEntity =
    ExerciseDetailsEntity(
        exerciseName = "Test Exercise",
        id = 1,
        note = "",
        category = "",
    )

fun getWorkoutDetails(
    id: Long = 1L,
    name: String = "Test Workout",
): WorkoutDetailsEntity =
    WorkoutDetailsEntity(
        workoutName = name,
        id = id,
        category = "",
        note = "",
    )

fun getWorkout(
    id: Long = 1L,
    sets: Int = 3,
    exerciseDetailsId: Long = 1L,
): WorkoutEntity =
    WorkoutEntity(
        id = id,
        workoutDetailsId = 1,
        exerciseDetailsId = exerciseDetailsId,
        sets = sets,
        order = 1,
    )
