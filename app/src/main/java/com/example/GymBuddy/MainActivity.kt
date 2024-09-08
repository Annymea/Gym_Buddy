package com.example.GymBuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.GymBuddy.ui.navigation.AppNavigation
import com.example.GymBuddy.ui.theme.Gym_BuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Gym_BuddyTheme {
                AppNavigation()
            }
        }
    }
}
