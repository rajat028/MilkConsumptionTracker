package com.milkconsumptiontracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Consumption(
    @PrimaryKey val date: String,
    val displayDate: String,
    val quantity: Float,
    val day: String,
    val month: String
) {
    companion object {
        val EMPTY = Consumption(
            date = "",
            displayDate = "",
            quantity = 0f,
            day = "",
            month = ""
        )
    }
}
