package com.milkconsumptiontracker.domain.usecase

import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.model.DateSnapshot
import com.milkconsumptiontracker.domain.repository.MilkConsumptionRepository
import com.milkconsumptiontracker.utils.formatDate
import com.milkconsumptiontracker.utils.formatToMonth
import com.milkconsumptiontracker.utils.toDisplayDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DashboardUseCase @Inject constructor(private val repository: MilkConsumptionRepository) {

  companion object {
    private val dayFormatter = SimpleDateFormat("EEE", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val monthFormatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    private val displayDateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
  }

  private val calendar = Calendar.getInstance()

  suspend fun insertQuantity(consumption: Consumption) {
    repository.addQuantity(consumption)
  }

  fun getLastSevenDaysConsumption(): Flow<List<Consumption>> {
    val endDate = LocalDate.now()
    val startDate = endDate.minusDays(6)
    return repository
        .getConsumptionBetweenDates(startDate.formatDate(), endDate.formatDate())
        .map { list -> fillMissingDays(list) }
  }

  fun getConsumedQuantityOfMonth(month: String): Flow<Float> {
    return repository.getConsumedQuantityOfMonth(month)
  }

  fun fetchCurrentDate(): DateSnapshot {
    val currentTime = calendar.time
    return DateSnapshot(
        displayDate = displayDateFormatter.format(currentTime),
        day = dayFormatter.format(currentTime),
        date = dateFormatter.format(currentTime),
        month = monthFormatter.format(currentTime))
  }

  fun getConsumedDaysInAMonth(month: String): Flow<Int> {
    return repository.getConsumedDaysInAMonth(month)
  }

  fun getConsumedDaysInAMonthProgress(month: String): Flow<Float> {
    return repository.getConsumedDaysInAMonth(month).map { days ->
      val totalDays = getDaysOfMonth()
      (days.toFloat() / totalDays)
    }
  }

  fun getNonConsumedDaysInAMonth(month: String): Flow<Int> {
    return repository.getConsumedDaysInAMonth(month).map { getDaysOfMonth().minus(it) }
  }

  fun getNonConsumedDaysInAMonthProgress(month: String): Flow<Float> {
    return getNonConsumedDaysInAMonth(month).map { days ->
      val totalDays = getDaysOfMonth()
      (days.toFloat() / totalDays)
    }
  }

  fun isTodayConsumptionUpdated(date: String): Flow<Boolean> {
    return repository.getConsumptionAsPerDate(date).map { it > 0 }
  }

  private fun getDaysOfMonth() = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

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
          entry?.copy(displayDate = entry.date.toDisplayDate())
              ?: Consumption(
                  displayDate = formattedDate.toDisplayDate(),
                  date = formattedDate,
                  day = dayOfWeek,
                  quantity = 0F,
                  month = formatedMonth))
    }
    return result.sortedByDescending { it.date }
  }
}
