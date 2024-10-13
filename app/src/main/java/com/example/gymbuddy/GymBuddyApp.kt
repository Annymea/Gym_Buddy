package com.example.gymbuddy

import android.app.Application
import androidx.room.Room
import com.example.gymbuddy.data.WorkoutRepository
import com.example.gymbuddy.data.localdatabase.LocalDataRepository
import com.example.gymbuddy.data.localdatabase.WorkoutDatabase
import com.example.gymbuddy.ui.workouts.editor.WorkoutEditorViewModel
import com.example.gymbuddy.ui.workouts.executor.WorkoutExecutorViewModel
import com.example.gymbuddy.ui.workouts.overview.WorkoutOverviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class GymBuddyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@GymBuddyApp)
            modules(appModule)
        }
    }
}

val appModule =
    module {
        single {
            Room
                .databaseBuilder(
                    get<Application>(),
                    WorkoutDatabase::class.java,
                    "workout-database"
                ).build()
        }

        single { get<WorkoutDatabase>().workoutDao() }

        single<WorkoutRepository> {
            LocalDataRepository(workoutDAO = get())
        }

        viewModel { WorkoutOverviewViewModel(workoutRepository = get()) }
        viewModel { WorkoutEditorViewModel(workoutRepository = get()) }
        viewModel { (workoutId: String) ->
            WorkoutExecutorViewModel(workoutRepository = get(), workoutId = workoutId)
        }
    }
