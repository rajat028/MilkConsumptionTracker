package com.milkconsumptiontracker.domain.repository

import com.milkconsumptiontracker.domain.model.Consumption
import kotlinx.coroutines.flow.Flow

interface MilkConsumptionRepository {

  suspend fun addQuantity(consumption: Consumption)

  fun getConsumptionBetweenDates(startDate: String, endDate: String): Flow<List<Consumption>>

  fun getConsumedQuantityOfMonth(month: String): Flow<Float>
  
  fun getConsumedDaysInAMonth(month: String): Flow<Int>
  
  fun getConsumptionAsPerDate(date: String): Flow<Int>
}
