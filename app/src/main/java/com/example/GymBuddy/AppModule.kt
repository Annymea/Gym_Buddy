package com.example.GymBuddy

import android.app.Application
import androidx.room.Room
import com.example.GymBuddy.data.WorkoutRepository
import com.example.GymBuddy.data.localdatabase.LocalDataRepository
import com.example.GymBuddy.data.localdatabase.WorkoutDatabase
import org.koin.dsl.module

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
}
