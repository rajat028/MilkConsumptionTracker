package com.milkconsumptiontracker.data.repository

import com.milkconsumptiontracker.data.local.BasePriceDao
import com.milkconsumptiontracker.domain.model.PriceSnapshot
import com.milkconsumptiontracker.domain.repository.BasePriceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class BasePriceRepositoryImpl @Inject constructor(private val basePriceDao: BasePriceDao) :
    BasePriceRepository {
  override suspend fun insertPrice(priceSnapshot: PriceSnapshot) {
    basePriceDao.insertPrice(priceSnapshot)
  }

  override suspend fun getCurrentMonthBasePrice(monthAndYear: String): Flow<Int> {
    return basePriceDao.getPrice(monthAndYear)
  }
}
