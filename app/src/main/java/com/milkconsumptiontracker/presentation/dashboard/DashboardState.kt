package com.milkconsumptiontracker.presentation.dashboard

import com.milkconsumptiontracker.domain.model.Consumption

data class DashboardState(
    val currentMonthBasePrice: String = "0",
    val currentMonthConsumedQuantity: Float = 0f,
    val currentMonthDueAmount: Float = 0f,
    val consumedDaysCount: Int = 0,
    val nonConsumedDaysCount: Int = 0,
    val consumedDaysInAMonthProgress: Float = 0f,
    val nonConsumedDaysInAMonthProgress: Float = 0f,
    val isTodayConsumptionUpdated: Boolean = true
)
