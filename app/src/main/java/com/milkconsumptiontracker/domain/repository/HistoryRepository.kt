package com.milkconsumptiontracker.domain.repository

import com.milkconsumptiontracker.domain.model.Consumption
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
  fun getConsumptionDatesOfMonth(month: String): Flow<List<Consumption>>
}
