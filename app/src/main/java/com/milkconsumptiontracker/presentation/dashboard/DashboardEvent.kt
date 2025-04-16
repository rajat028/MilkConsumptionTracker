package com.milkconsumptiontracker.presentation.dashboard

import com.milkconsumptiontracker.domain.model.DateSnapshot

sealed class DashboardEvent {
  data class AddConsumption(val quantity: Float, val dateSnapshot: DateSnapshot) : DashboardEvent()
}
