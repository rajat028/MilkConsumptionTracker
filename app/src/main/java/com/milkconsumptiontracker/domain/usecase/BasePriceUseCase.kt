package com.milkconsumptiontracker.domain.usecase

import com.milkconsumptiontracker.domain.model.PriceSnapshot
import com.milkconsumptiontracker.domain.repository.BasePriceRepository
import javax.inject.Inject

class BasePriceUseCase @Inject constructor(private val repository: BasePriceRepository) {

  suspend fun updateBasePrice(price: String, monthAndYear: String) {
    val priceSnapshot = PriceSnapshot(price = price.toInt(), monthAndYear = monthAndYear)
    repository.insertPrice(priceSnapshot)
  }

  suspend fun getCurrentMonthBasePrice(monthAndYear: String): String {
    return repository.getCurrentMonthBasePrice(monthAndYear).toString()
  }
  
  fun getCurrentMonthBasePriceTest(monthAndYear: String): String {
    return repository.getCurrentMonthBasePrice(monthAndYear).toString()
  }
}
