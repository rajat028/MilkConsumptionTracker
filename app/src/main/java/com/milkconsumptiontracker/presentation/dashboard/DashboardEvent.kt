package com.milkconsumptiontracker.presentation.dashboard

import com.milkconsumptiontracker.domain.model.Consumption

sealed class DashboardEvent {
  data class AddConsumption(val consumption: Consumption) : DashboardEvent()
}
