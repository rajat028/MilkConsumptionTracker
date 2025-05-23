package com.milkconsumptiontracker.data.repository

import com.milkconsumptiontracker.data.local.ConsumptionDao
import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.repository.MilkConsumptionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class MilkConsumptionRepositoryImpl
@Inject
constructor(private val consumptionDao: ConsumptionDao) : MilkConsumptionRepository {
  override suspend fun addQuantity(consumption: Consumption) {
    consumptionDao.insertConsumption(consumption)
  }

  override fun getConsumptionBetweenDates(
      startDate: String,
      endDate: String
  ): Flow<List<Consumption>> {
    return consumptionDao.getConsumptionBetweenDates(startDate, endDate)
  }

  override fun getConsumedQuantityOfMonth(month: String): Flow<Float> {
    return consumptionDao.getConsumedQuantityOfMonth(month)
  }
  
  override fun getConsumedDaysInAMonth(month: String): Flow<Int> {
    return consumptionDao.getConsumedDaysInAMonth(month)
  }
  
  override fun getConsumptionAsPerDate(date: String): Flow<Int> {
    return consumptionDao.getConsumptionAsPerDate(date)
  }
}
