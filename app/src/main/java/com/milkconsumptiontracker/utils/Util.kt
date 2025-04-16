package com.milkconsumptiontracker.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

fun String.toDisplayDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM")
    
    val date = LocalDate.parse(this, inputFormatter)
    return date.format(outputFormatter)
}