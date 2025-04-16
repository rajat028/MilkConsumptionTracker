package com.milkconsumptiontracker.domain.model

data class DateSnapshot(val day: String, val date: String, val month: String) {
  companion object {
    fun default() = DateSnapshot(day = "", date = "", month = "")
  }
}
