package com.milkconsumptiontracker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.usecase.HistoryUseCase
import com.milkconsumptiontracker.utils.convertToLocalDate
import com.milkconsumptiontracker.utils.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(private val useCase: HistoryUseCase) : ViewModel() {
  private val _consumedDates = MutableStateFlow(emptyList<LocalDate>())
  val consumedDates: StateFlow<List<LocalDate>> = _consumedDates

  private val _consumption = MutableStateFlow(emptyList<Consumption>())
  val consumption: StateFlow<List<Consumption>> = _consumption

  fun fetchConsumptionDataOfMonth(month: YearMonth) {
    viewModelScope.launch {
      useCase
          .fetchConsumptionDatesInMonth(month)
          .map { consumptionData ->
            fetchConsumptionDatesInMonth(consumptionData)
            consumptionData
          }
          .collect { consumptions -> _consumption.value = consumptions }
    }
  }

  private fun fetchConsumptionDatesInMonth(consumptions: List<Consumption>) {
    _consumedDates.value = consumptions.map { it.date.convertToLocalDate() }
  }

  fun getConsumptionOfDate(date: LocalDate): Consumption? {
    return consumption.value.find { it.date == date.formatDate() }
  }
}
