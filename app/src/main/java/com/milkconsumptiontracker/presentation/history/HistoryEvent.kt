package com.milkconsumptiontracker.presentation.history

import com.milkconsumptiontracker.domain.model.Consumption

sealed class HistoryEvent {
  data class UpdateConsumption(val consumption: Consumption) : HistoryEvent()
}
