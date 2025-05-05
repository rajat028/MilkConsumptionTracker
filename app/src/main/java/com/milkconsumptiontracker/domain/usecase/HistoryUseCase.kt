package com.milkconsumptiontracker.domain.usecase

import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.repository.HistoryRepository
import com.milkconsumptiontracker.utils.toDisplayMonth
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class HistoryUseCase @Inject constructor(private val repository: HistoryRepository) {
  fun fetchConsumptionDatesInMonth(month: YearMonth): Flow<List<Consumption>> {
    return repository.getConsumptionDatesOfMonth(month.toDisplayMonth())
  }
}
