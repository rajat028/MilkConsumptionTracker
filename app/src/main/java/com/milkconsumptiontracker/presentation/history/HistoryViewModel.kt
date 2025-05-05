package com.milkconsumptiontracker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.usecase.BasePriceUseCase
import com.milkconsumptiontracker.domain.usecase.DashboardUseCase
import com.milkconsumptiontracker.domain.usecase.HistoryUseCase
import com.milkconsumptiontracker.utils.convertToLocalDate
import com.milkconsumptiontracker.utils.formatDate
import com.milkconsumptiontracker.utils.formatToMonth
import com.milkconsumptiontracker.utils.toDisplayDate
import com.milkconsumptiontracker.utils.toDisplayMonth
import com.milkconsumptiontracker.utils.toShortDayName
import com.milkconsumptiontracker.utils.toShortMonthName
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel
@Inject
constructor(
    private val useCase: HistoryUseCase,
    private val basePriceUseCase: BasePriceUseCase,
    private val dashboardUseCase: DashboardUseCase
) : ViewModel() {

  private val _monthConsumptionData = MutableStateFlow(emptyList<Consumption>())
  val monthConsumptionData: StateFlow<List<Consumption>> = _monthConsumptionData

  private val _consumptionOfDate = MutableStateFlow(Consumption.EMPTY)
  val consumptionOfDate: StateFlow<Consumption> = _consumptionOfDate

  private val _historyViewState = MutableStateFlow(HistoryViewState())
  val historyViewState: StateFlow<HistoryViewState> = _historyViewState

  val consumedDates: StateFlow<List<LocalDate>> =
      monthConsumptionData
          .map { list -> list.map { it.date.convertToLocalDate() } }
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun fetchConsumptionDataOfMonth(month: YearMonth, selectedDate: LocalDate) {
    viewModelScope.launch {
      val monthStr = month.toDisplayMonth()
      combine(
              basePriceUseCase.getCurrentMonthBasePrice(monthStr),
              dashboardUseCase.getConsumedQuantityOfMonth(monthStr),
              useCase.fetchConsumptionDatesInMonth(month)) {
                  basePrice,
                  consumedQuantity,
                  consumptions ->
                _historyViewState.value = mapToHistoryViewState(basePrice, consumedQuantity, month)

                _monthConsumptionData.value = consumptions

                if (month.toDisplayMonth() == selectedDate.formatToMonth()) {
                  getConsumptionOfDate(selectedDate)
                }
              }
          .collect()
    }
  }

  private fun mapToHistoryViewState(
      basePrice: Int,
      consumedQuantity: Float,
      month: YearMonth
  ): HistoryViewState {
    return HistoryViewState(
        basePrice = "Rs $basePrice",
        consumedQuantity = "$consumedQuantity Ltrs",
        dueAmount = "Rs ${dueAmount(basePrice, consumedQuantity)}",
        month = month.toDisplayMonth())
  }

  fun getConsumptionOfDate(date: LocalDate) {
    _consumptionOfDate.value =
        monthConsumptionData.value.find { it.date == date.formatDate() }
            ?: Consumption(
                date = date.formatDate(),
                displayDate = date.toDisplayDate(),
                quantity = 0.0f,
                day = date.dayOfWeek.name.toShortDayName(),
                month = "${date.month.name.toShortMonthName()} ${date.year}")
  }

  fun onEvent(event: HistoryEvent) {
    when (event) {
      is HistoryEvent.UpdateConsumption -> {
        viewModelScope.launch { dashboardUseCase.insertQuantity(event.consumption) }
      }
    }
  }

  private fun dueAmount(basePrice: Int, consumedQuantity: Float): Float {
    return basePrice * consumedQuantity
  }
}
