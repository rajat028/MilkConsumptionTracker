package com.milkconsumptiontracker.domain.usecase

import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.repository.HistoryRepository
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class HistoryUseCase @Inject constructor(private val repository: HistoryRepository) {
  fun fetchConsumptionDatesInMonth(month: YearMonth): Flow<List<Consumption>> {
    return repository.getConsumptionDatesOfMonth(
        month.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())))
  }
}
