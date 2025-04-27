package com.milkconsumptiontracker.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.time.LocalDate
import java.time.YearMonth

data class CalendarDay(val date: LocalDate, val isConsumed: Boolean, val isFutureDate: Boolean)

@Composable
fun rememberCalendarState(): MutableState<YearMonth> {
  return remember { mutableStateOf(YearMonth.now()) }
}

fun generateMonthDays(
    month: YearMonth,
    consumedDates: List<LocalDate>,
    today: LocalDate = LocalDate.now()
): List<CalendarDay> {
  val firstDayOfMonth = month.atDay(1)
  val lastDay = month.lengthOfMonth()
  return (1..lastDay).map { day ->
    val date = month.atDay(day)
    CalendarDay(
        date = date, isConsumed = consumedDates.contains(date), isFutureDate = date.isAfter(today))
  }
}
