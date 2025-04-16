package com.milkconsumptiontracker.presentation.dashboard

import com.milkconsumptiontracker.domain.model.Consumption

data class DashboardState(
    val currentMonthBasePrice: String = "0",
    val lastSevenDaysConsumption: List<Consumption> = emptyList()
)
