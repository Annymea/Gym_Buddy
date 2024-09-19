package com.example.gymbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gymbuddy.ui.navigation.AppNavigation
import com.example.gymbuddy.ui.theme.Gym_BuddyTheme

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
