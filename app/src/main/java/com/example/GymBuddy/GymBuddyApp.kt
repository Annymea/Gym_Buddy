package com.example.GymBuddy

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

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
