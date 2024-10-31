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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun TrainingsCalender(
    modifier: Modifier = Modifier,
    viewModel: TrainingsCalenderViewModel = koinViewModel<TrainingsCalenderViewModel>(),
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            MonthNavigation(
                shownMonth =
                    viewModel.nameOfMonth(
                        viewModel.shownMonth.value,
                    ),
                shownYear = viewModel.shownYear.value,
                onPreviousMonthClick = {
                    viewModel.updateMonth(
                        increment = false,
                    )
                },
                onNextMonthClick = { viewModel.updateMonth(increment = true) },
            )

            WeekdayHeaders()

            MonthlyCalendar(
                shownMonth = viewModel.shownMonth.value,
                shownYear = viewModel.shownYear.value,
                currentDate = viewModel.currentDate,
                highlightedDays = viewModel.highlightedForShownMonth,
            )
        }
    }
}

@Composable
fun MonthNavigation(
    shownMonth: String,
    shownYear: Int,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = onPreviousMonthClick, modifier = Modifier.weight(1f)) {
            Icon(
                rememberVectorPainter(Icons.Filled.KeyboardArrowLeft),
                contentDescription = "Previous",
            )
        }
        Text(
            text = "$shownMonth $shownYear",
            modifier = Modifier.align(Alignment.CenterVertically),
        )
        IconButton(onClick = onNextMonthClick, modifier = Modifier.weight(1f)) {
            Icon(
                rememberVectorPainter(Icons.Filled.KeyboardArrowRight),
                contentDescription = "After",
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
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun MonthlyCalendar(
    shownMonth: Int,
    shownYear: Int,
    currentDate: LocalDate,
    highlightedDays: List<Int> = emptyList(),
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
                            highlighted = highlightedDays.contains(day),
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
    highlighted: Boolean = false,
) {
    if (highlighted && !isToday) {
        FilledTonalButton(
            modifier = modifier,
            contentPadding = PaddingValues(0.dp),
            onClick = { /*TODO*/ },
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center,
            )
        }
    } else if (isToday && highlighted) {
        FilledTonalButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(0.dp),
            border = ButtonDefaults.outlinedButtonBorder(),
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center,
            )
        }
    } else if (isToday) {
        OutlinedButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center,
            )
        }
    } else {
        TextButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
        ) {
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

// @Preview(showBackground = true)
// @Composable
// fun TrainingsCalenderPreview() {
//    TrainingsCalender()
// }
