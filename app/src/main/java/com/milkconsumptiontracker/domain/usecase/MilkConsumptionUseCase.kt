package com.milkconsumptiontracker.domain.usecase

import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.model.DateSnapshot
import com.milkconsumptiontracker.domain.repository.MilkConsumptionRepository
import com.milkconsumptiontracker.utils.formatDate
import com.milkconsumptiontracker.utils.formatToMonth
import com.milkconsumptiontracker.utils.toDisplayDate
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MilkConsumptionUseCase
@Inject
constructor(private val repository: MilkConsumptionRepository) {

  suspend fun insertQuantity(quantity: Float, dateSnapshot: DateSnapshot) {
    repository.addQuantity(
        Consumption(
            quantity = quantity,
            date = dateSnapshot.date,
            day = dateSnapshot.day,
            month = dateSnapshot.month))
  }

  fun getLastSevenDaysConsumption(): Flow<List<Consumption>> {
    val endDate = LocalDate.now()
    val startDate = endDate.minusDays(6)
    return repository
        .getConsumptionBetweenDates(startDate.formatDate(), endDate.formatDate())
        .map { list -> fillMissingDays(list) }
  }

  private fun fillMissingDays(consumptionList: List<Consumption>): List<Consumption> {
    val result = mutableListOf<Consumption>()
    val today = LocalDate.now()

    for (i in 0..6) {
      val date = today.minusDays(i.toLong())
      val formattedDate = date.formatDate()
      val formatedMonth = date.formatToMonth()
      val dayOfWeek =
          date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()) // "Mon", "Tue" etc.

      val entry = consumptionList.find { it.date == formattedDate }
      result.add(
          entry?.copy(date = entry.date.toDisplayDate())
              ?: Consumption(
                  date = formattedDate.toDisplayDate(),
                  day = dayOfWeek,
                  quantity = 0F,
                  month = formatedMonth))
    }
    return result.sortedByDescending { it.date }
  }
}
