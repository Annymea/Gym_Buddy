package com.example.gymbuddy.ui.dashboard.widgets.trainingsCalender

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun TrainingsCalender(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            val currentDate = LocalDate.now()
            var shownMonth by remember { mutableIntStateOf(currentDate.month.value) }
            var shownYear by remember { mutableIntStateOf(currentDate.year) }

            fun updateMonth(increment: Boolean) {
                if (increment) {
                    if (shownMonth == 12) {
                        shownMonth = 1
                        shownYear += 1
                    } else {
                        shownMonth += 1
                    }
                } else {
                    if (shownMonth == 1) {
                        shownMonth = 12
                        shownYear -= 1
                    } else {
                        shownMonth -= 1
                    }
                }
            }

            MonthNavigation(
                shownMonth = shownMonth,
                shownYear = shownYear,
                onPreviousMonthClick = { updateMonth(increment = false) },
                onNextMonthClick = { updateMonth(increment = true) }
            )

            WeekdayHeaders()

            val highligtedDays = listOf(1, 13, 6, 3, 17, 25, 29)

            MonthlyCalendar(
                shownMonth = shownMonth,
                shownYear = shownYear,
                currentDate = currentDate,
                highligtedDays = highligtedDays
            )
        }
    }
}

@Composable
fun MonthNavigation(
    shownMonth: Int,
    shownYear: Int,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onPreviousMonthClick, modifier = Modifier.weight(1f)) {
            Icon(
                rememberVectorPainter(Icons.Filled.KeyboardArrowLeft),
                contentDescription = "Previous"
            )
        }
        Text(
            text = "${nameOfMonth(shownMonth)} $shownYear",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        IconButton(onClick = onNextMonthClick, modifier = Modifier.weight(1f)) {
            Icon(
                rememberVectorPainter(Icons.Filled.KeyboardArrowRight),
                contentDescription = "After"
            )
        }
    }
}

@Composable
fun WeekdayHeaders() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MonthlyCalendar(
    shownMonth: Int,
    shownYear: Int,
    currentDate: LocalDate,
    highligtedDays: List<Int> = emptyList()
) {
    var day = 1
    val firstWeekDay = LocalDate.of(shownYear, shownMonth, 1).dayOfWeek.value
    val daysInMonth = LocalDate.of(shownYear, shownMonth, 1).lengthOfMonth()

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        while (day <= daysInMonth) {
            Row {
                for (weekday in 1..7) {
                    if (day > daysInMonth || (weekday < firstWeekDay && day == 1)) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        DayButton(
                            day = day,
                            isToday = (
                                currentDate.dayOfMonth == day &&
                                    currentDate.monthValue == shownMonth &&
                                    currentDate.year == shownYear
                                ),
                            modifier = Modifier.weight(1f),
                            highlighted = highligtedDays.contains(day)
                        )
                        day++
                    }
                }
            }
        }
    }
}

@Composable
fun DayButton(
    modifier: Modifier = Modifier,
    day: Int,
    isToday: Boolean,
    highlighted: Boolean = false
) {
    if (highlighted && !isToday) {
        FilledTonalButton(
            modifier = modifier,
            contentPadding = PaddingValues(0.dp),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center
            )
        }
    } else if (isToday && highlighted) {
        FilledTonalButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(0.dp),
            border = ButtonDefaults.outlinedButtonBorder()
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center
            )
        }
    } else if (isToday) {
        OutlinedButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center
            )
        }
    } else {
        TextButton(
            modifier = modifier,
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrainingsCalenderPreview() {
    TrainingsCalender()
}

fun nameOfMonth(month: Int): String {
    val monthNames =
        arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )
    return if (month in 1..12) monthNames[month - 1] else "Unknown"
}
