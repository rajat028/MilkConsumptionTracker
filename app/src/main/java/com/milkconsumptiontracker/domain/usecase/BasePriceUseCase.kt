package com.milkconsumptiontracker.domain.usecase

import com.milkconsumptiontracker.domain.model.PriceSnapshot
import com.milkconsumptiontracker.domain.repository.BasePriceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BasePriceUseCase @Inject constructor(private val repository: BasePriceRepository) {

  suspend fun updateBasePrice(price: String, monthAndYear: String) {
    val priceSnapshot = PriceSnapshot(price = price.toInt(), monthAndYear = monthAndYear)
    repository.insertPrice(priceSnapshot)
  }

  fun getCurrentMonthBasePrice(monthAndYear: String): Flow<Int> {
    return repository.getCurrentMonthBasePrice(monthAndYear)
  }
}
