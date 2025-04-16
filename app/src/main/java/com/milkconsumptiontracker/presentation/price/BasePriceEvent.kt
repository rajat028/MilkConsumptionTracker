package com.milkconsumptiontracker.presentation.price

sealed class BasePriceEvent {
  data class UpdateBasePrice(val basePrice : String) : BasePriceEvent()
}
