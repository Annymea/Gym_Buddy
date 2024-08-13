package com.example.GymBuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.GymBuddy.data.Plan
import com.example.GymBuddy.data.WorkoutDatabase
import com.example.GymBuddy.ui.theme.Gym_BuddyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Gym_BuddyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FirstButtons(modifier = Modifier.padding(innerPadding))
                }
            }
        }

        val db = Room.databaseBuilder(
            applicationContext,
            WorkoutDatabase::class.java,
            "workout-database"
        ).build()

        val workoutDao = db.workoutDao()

        CoroutineScope(Dispatchers.IO).launch {
            val plan = Plan(id = 1, planName = "My First Plan")
            workoutDao.insertPlan(plan)
        }
    }
}

@Composable
fun FirstButtons(modifier: Modifier) {
    Column(modifier = modifier) {
        Button(
            onClick = { /*TODO*/ }
        ) {
            Text(text = stringResource(R.string.create_plan))
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = stringResource(R.string.start_training))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Gym_BuddyTheme {
        FirstButtons(Modifier)
    }
}
