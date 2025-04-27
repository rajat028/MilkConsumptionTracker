package com.milkconsumptiontracker.data.repository

import com.milkconsumptiontracker.data.local.HistoryDao
import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(private val dao: HistoryDao) : HistoryRepository {
  override fun getConsumptionDatesOfMonth(month: String): Flow<List<Consumption>> {
    return dao.getConsumptionDatesOfMonth(month)
  }
}
