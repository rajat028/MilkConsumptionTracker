package com.milkconsumptiontracker.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun LocalDate.formatDate(): String {
  val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
  return this.format(formatter)
}

fun LocalDate.formatToMonth(): String {
  val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
  return this.format(formatter)
}

fun LocalDate.toDisplayDate(): String {
  val formatter = DateTimeFormatter.ofPattern("dd MMM")
  return this.format(formatter)
}

fun String.convertToLocalDate(): LocalDate {
  val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
  return LocalDate.parse(this, formatter)
}

fun String.toDisplayDate(): String {
  val inputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
  val outputFormatter = DateTimeFormatter.ofPattern("dd MMM")

  val date = LocalDate.parse(this, inputFormatter)
  return date.format(outputFormatter)
}

fun String.toShortDayName(): String {
  return try {
    DayOfWeek.valueOf(this.uppercase())
        .getDisplayName(TextStyle.SHORT, Locale.getDefault()) // Mon, Tue, etc
  } catch (e: IllegalArgumentException) {
    "Invalid Day"
  }
}

fun String.toShortMonthName(): String {
  return this.replaceFirstChar { it.uppercase() }.take(3)
}

fun YearMonth.toDisplayMonth(): String {
  return this.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault()))
}

fun String.isZeroOrEmpty(): Boolean {
  return this.isEmpty() || this == "0" || this == "0.0" || this == "Rs 0"
}
