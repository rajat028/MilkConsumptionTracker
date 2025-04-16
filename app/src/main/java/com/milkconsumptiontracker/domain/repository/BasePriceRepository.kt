package com.milkconsumptiontracker.domain.repository

import com.milkconsumptiontracker.domain.model.PriceSnapshot
import kotlinx.coroutines.flow.Flow

interface BasePriceRepository {
  suspend fun insertPrice(priceSnapshot: PriceSnapshot)
  suspend fun getCurrentMonthBasePrice(monthAndYear: String): Flow<Int>
}
