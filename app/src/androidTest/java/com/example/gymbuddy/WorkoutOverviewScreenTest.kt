package com.example.gymbuddy

// class CompleteWorkoutDTOOverviewScreenTestEntity {
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun workoutOverview_showsAddWorkoutButtonIfNoWorkouts() {
//        val workouts = emptyList<WorkoutDetailsEntity>()
//
//        composeTestRule.setContent {
//            WorkoutOverviewScreen(
//                workoutOverviewViewModel = FakeWorkoutOverviewViewModel(workouts),
//                onExecuteWorkout = { }
//            )
//        }
//
//        composeTestRule.onNodeWithText("Add a workout").assertIsDisplayed()
//    }
//
//    @Test
//    fun workoutOverview_showsWorkoutNameIfWorkouts() {
//        val workouts =
//            listOf(
//                WorkoutDetailsEntity(workoutName = "Workout 1"),
//                WorkoutDetailsEntity(workoutName = "Workout 2")
//            )
//
//        composeTestRule.setContent {
//            WorkoutOverviewScreen(
//                workoutOverviewViewModel = FakeWorkoutOverviewViewModel(workouts),
//                onExecuteWorkout = { }
//            )
//        }
//
//        workouts.forEach { workout ->
//            composeTestRule.onNodeWithText(workout.workoutName).assertIsDisplayed()
//        }
//    }
// }
//
// class FakeWorkoutOverviewViewModel(
//    override val workouts: List<W>
// ) : WorkoutOverviewViewModelContract {
//    override val uiState: StateFlow<WorkoutOverviewUiState>
//        get() = _uiState
//
//    private val _uiState =
//        MutableStateFlow<WorkoutOverviewUiState>(WorkoutOverviewUiState.NoWorkouts)
//
//    init {
//        if (workouts.isNotEmpty()) {
//            _uiState.value = WorkoutOverviewUiState.Workouts
//        } else {
//            _uiState.value = WorkoutOverviewUiState.NoWorkouts
//        }
//    }
// }
