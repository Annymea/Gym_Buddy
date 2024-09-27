package com.example.gymbuddy.ui.workouts.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
        fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
        modifier = modifier.padding(24.dp),
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onBackground,
        text = text
    )
}
