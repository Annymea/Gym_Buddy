package com.example.gymbuddy.ui.dashboard.widgets.trainingsCalender

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymbuddy.data.WorkoutRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface TrainingsCalenderViewModelContract {
    val shownMonth: MutableState<Int>
    val shownYear: MutableState<Int>
    val currentDate: LocalDate
    val highlightedForShownMonth: SnapshotStateList<Int>

    fun nameOfMonth(month: Int): String

    fun updateMonth(increment: Boolean)
}

class TrainingsCalenderViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel(),
    TrainingsCalenderViewModelContract {
    override var shownMonth: MutableState<Int> = mutableIntStateOf(LocalDate.now().monthValue)
        private set
    override var shownYear: MutableState<Int> = mutableIntStateOf(LocalDate.now().year)
        private set
    override var currentDate: LocalDate = LocalDate.now()
        private set
    override var highlightedForShownMonth: SnapshotStateList<Int> = mutableStateListOf()
        private set

    init {
        updateHighlightedDays(shownMonth.value, shownYear.value)
    }

    override fun nameOfMonth(month: Int): String {
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

    override fun updateMonth(increment: Boolean) {
        if (increment) {
            if (shownMonth.value == 12) {
                shownMonth.value = 1
                shownYear.value += 1
            } else {
                shownMonth.value += 1
            }
        } else {
            if (shownMonth.value == 1) {
                shownMonth.value = 12
                shownYear.value -= 1
            } else {
                shownMonth.value -= 1
            }
        }

        updateHighlightedDays(shownMonth.value, shownYear.value)
    }

    private fun updateHighlightedDays(
        month: Int,
        year: Int
    ) {
        viewModelScope.launch {
            try {
                val dates =
                    workoutRepository
                        .getDaysOfCompletedWorkoutsForMonth(month, year)
                        .first()
                highlightedForShownMonth.clear()
                highlightedForShownMonth.addAll(dates)
                Log.d(
                    "TrainingsCalenderViewModel",
                    "updateHighlightedDays: $highlightedForShownMonth"
                )
            } catch (e: Exception) {
                Log.d("TrainingsCalenderViewModel", "updateHighlightedDays: $e")
            }
        }
    }
}
