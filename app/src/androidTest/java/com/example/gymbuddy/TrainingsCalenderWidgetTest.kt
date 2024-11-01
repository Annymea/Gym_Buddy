package com.example.gymbuddy

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.gymbuddy.ui.dashboard.widgets.trainingsCalender.TrainingsCalender
import com.example.gymbuddy.ui.dashboard.widgets.trainingsCalender.TrainingsCalenderViewModelContract
import java.time.LocalDate
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TrainingsCalenderWidgetTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = FakeTrainingsCalenderViewModel()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            TrainingsCalender(
                viewModel = viewModel
            )
        }
    }

    @Test
    fun trainingsCalender_showsMonthNavigationAndWeekdays() {
        composeTestRule
            .onNodeWithTag("trainingsCalender_monthYearText")
            .assertIsDisplayed()
            .assertTextContains("October 2024")

        composeTestRule
            .onNodeWithTag("trainingsCalender_weekdayHeaders")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(7)

        composeTestRule
            .onNodeWithTag("trainingsCalender_weekdayHeader_Mon")
            .assertIsDisplayed()
            .assertTextContains("Mon")

        composeTestRule
            .onNodeWithTag("trainingsCalender_weekdayHeader_Tue")
            .assertIsDisplayed()
            .assertTextContains("Tue")

        composeTestRule
            .onNodeWithTag("trainingsCalender_weekdayHeader_Wed")
            .assertIsDisplayed()
            .assertTextContains("Wed")

        composeTestRule
            .onNodeWithTag("trainingsCalender_weekdayHeader_Thu")
            .assertIsDisplayed()
            .assertTextContains("Thu")

        composeTestRule
            .onNodeWithTag("trainingsCalender_weekdayHeader_Fri")
            .assertIsDisplayed()
            .assertTextContains("Fri")

        composeTestRule
            .onNodeWithTag("trainingsCalender_weekdayHeader_Sat")
            .assertIsDisplayed()
            .assertTextContains("Sat")

        composeTestRule
            .onNodeWithTag("trainingsCalender_weekdayHeader_Sun")
            .assertIsDisplayed()
            .assertTextContains("Sun")
    }

    @Test
    fun trainingsCalender_clicksNextMonthButton_updatesDisplayedMonth() {
        composeTestRule
            .onNodeWithTag("trainingsCalender_nextMonthButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag("trainingsCalender_monthYearText")
            .assertIsDisplayed()
            .assertTextContains("November 2024")

        composeTestRule
            .onNodeWithTag("trainingsCalender_previousMonthButton")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag("trainingsCalender_monthYearText")
            .assertIsDisplayed()
            .assertTextContains("October 2024")
    }

    @Test
    fun trainingsCalender_showsHighlightedDays() {
        val highlightedDays = listOf("2", "3", "14")
        viewModel.highlightedForShownMonth.clear()
        viewModel.highlightedForShownMonth.addAll(highlightedDays.map { it.toInt() })

        composeTestRule
            .onAllNodesWithTag("trainingsCalender_highlightedDay")
            .assertCountEquals(3)

        highlightedDays.forEachIndexed { index, day ->
            composeTestRule
                .onAllNodesWithTag("trainingsCalender_highlightedDay")[index]
                .assertTextEquals(day)
        }
    }

    @Test
    fun trainingsCalender_outlineCurrentDate() {
        composeTestRule
            .onNodeWithTag("trainingsCalender_todayDay")
            .assertIsDisplayed()
            .assertTextContains("31")
    }

    @Test
    fun trainingsCalender_highlightCurrentDate() {
        viewModel.highlightedForShownMonth.add(31)

        composeTestRule
            .onNodeWithTag("trainingsCalender_todayHighlighted")
            .assertIsDisplayed()
            .assertTextContains("31")
    }
}

class FakeTrainingsCalenderViewModel : TrainingsCalenderViewModelContract {
    override val shownMonth: MutableState<Int> = mutableStateOf(10)
    override val shownYear: MutableState<Int> = mutableStateOf(2024)
    override val currentDate: LocalDate = LocalDate.of(2024, 10, 31)
    override var highlightedForShownMonth: SnapshotStateList<Int> = mutableStateListOf(2, 3, 14)

    override fun nameOfMonth(month: Int): String {
        if (month == 10) return "October"
        if (month == 11) return "November"
        if (month == 9) return "September"
        return ""
    }

    override fun updateMonth(increment: Boolean) {
        if (increment) {
            shownMonth.value += 1
        } else {
            shownMonth.value -= 1
        }
    }
}
