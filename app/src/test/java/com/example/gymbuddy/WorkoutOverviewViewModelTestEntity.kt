package com.example.gymbuddy

// @OptIn(ExperimentalCoroutinesApi::class)
// class CompleteWorkoutDTOOverviewViewModelTestEntity {
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    private val testDispatcher = StandardTestDispatcher()
//    private lateinit var viewModel: WorkoutOverviewViewModel
//
//    @MockK
//    private lateinit var workoutRepository: WorkoutRepository
//
//    @Before
//    fun setup() {
//        MockKAnnotations.init(this, relaxUnitFun = true)
//        Dispatchers.setMain(testDispatcher)
//        workoutRepository = mockk()
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `init fetches workouts from repository and updates workout list`() =
//        runTest {
//            val workouts =
//                listOf(
//                    WorkoutDetailsEntity(workoutName = "Plan1"),
//                    WorkoutDetailsEntity(workoutName = "Plan2")
//                )
//            coEvery { workoutRepository.getAllPlanNames() } returns flow { emit(workouts) }
//
//            viewModel = WorkoutOverviewViewModel(workoutRepository)
//
//            advanceUntilIdle()
//
//            assertEquals(workouts, viewModel.workouts)
//        }
//
//    @Test
//    fun `workouts are empty when repository returns empty list`() =
//        runTest {
//            coEvery { workoutRepository.getAllPlanNames() } returns flow { emit(emptyList()) }
//
//            viewModel = WorkoutOverviewViewModel(workoutRepository)
//
//            advanceUntilIdle()
//
//            assertEquals(emptyList<WorkoutDetailsEntity>(), viewModel.workouts)
//        }
//
//    @Test
//    fun `workouts are updated when repository updates list`() =
//        runTest {
//            val initialWorkouts =
//                listOf(
//                    WorkoutDetailsEntity(workoutName = "Plan1"),
//                    WorkoutDetailsEntity(workoutName = "Plan2")
//                )
//            val updatedWorkouts =
//                listOf(
//                    WorkoutDetailsEntity(workoutName = "Plan3"),
//                    WorkoutDetailsEntity(workoutName = "Plan4")
//                )
//
//            coEvery { workoutRepository.getAllPlanNames() } returns
//                flow {
//                    emit(initialWorkouts)
//                    emit(updatedWorkouts)
//                }
//            viewModel = WorkoutOverviewViewModel(workoutRepository)
//
//            advanceUntilIdle()
//            assertEquals(updatedWorkouts, viewModel.workouts)
//        }
// }
//