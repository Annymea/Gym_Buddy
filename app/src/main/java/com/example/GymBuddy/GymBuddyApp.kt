package com.example.GymBuddy

import android.app.Application
import androidx.room.Room
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.LocalDataRepository
import com.example.GymBuddy.data.localdatabase.WorkoutDatabase
import com.example.GymBuddy.ui.createPlan.CreatePlanViewModel
import com.example.GymBuddy.ui.planList.PlanListViewModel
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

val appModule = module {
    single {
        Room.databaseBuilder(
            get<Application>(),
            WorkoutDatabase::class.java,
            "workout-database"
        ).build()
    }

    single { get<WorkoutDatabase>().workoutDao() }

    single<WorkoutRepository> {
        LocalDataRepository(workoutDAO = get())
    }

    viewModel {
        CreatePlanViewModel(workoutRepository = get())
    }

    viewModel {
        PlanListViewModel(workoutRepository = get())
    }
}
