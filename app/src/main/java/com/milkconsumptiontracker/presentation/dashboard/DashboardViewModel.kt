package com.milkconsumptiontracker.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milkconsumptiontracker.domain.model.DateSnapshot
import com.milkconsumptiontracker.domain.usecase.BasePriceUseCase
import com.milkconsumptiontracker.domain.usecase.DashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel
@Inject
constructor(
    private val dashboardUseCase: DashboardUseCase,
    private val basePriceUseCase: BasePriceUseCase
) : ViewModel() {

  private val _date = MutableStateFlow(DateSnapshot.default())
  val date: StateFlow<DateSnapshot> = _date

  private val _progressState = MutableStateFlow(DashboardState())

  @OptIn(ExperimentalCoroutinesApi::class)
  val consumptionProgressState =
      combine(
              _progressState,
              date.flatMapLatest { dashboardUseCase.getConsumedDaysInAMonthProgress(it.month) },
              date.flatMapLatest { dashboardUseCase.getNonConsumedDaysInAMonthProgress(it.month) },
              date.flatMapLatest { dashboardUseCase.isTodayConsumptionUpdated(it.date) }) {
                  progressState,
                  consumedDaysProgress,
                  nonConsumedDaysProgress,
                  todayConsumptionUpdated ->
                progressState.copy(
                    isTodayConsumptionUpdated = todayConsumptionUpdated,
                    consumedDaysInAMonthProgress = consumedDaysProgress,
                    nonConsumedDaysInAMonthProgress = nonConsumedDaysProgress)
              }
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

  @OptIn(ExperimentalCoroutinesApi::class)
  val state =
      combine(
              consumptionProgressState,
              date.flatMapLatest { basePriceUseCase.getCurrentMonthBasePrice(it.month) },
              date.flatMapLatest { dashboardUseCase.getConsumedQuantityOfMonth(it.month) },
              date.flatMapLatest { dashboardUseCase.getConsumedDaysInAMonth(it.month) },
              date.flatMapLatest { dashboardUseCase.getNonConsumedDaysInAMonth(it.month) },
          ) { state, basePrice, consumedQuantity, consumedDaysCount, nonConsumedDaysCount ->
            state.copy(
                currentMonthBasePrice = basePrice.toString(),
                currentMonthConsumedQuantity = consumedQuantity,
                currentMonthDueAmount = calculateDueAmount(basePrice, consumedQuantity),
                consumedDaysCount = consumedDaysCount,
                nonConsumedDaysCount = nonConsumedDaysCount)
          }
          .stateIn(
              scope = viewModelScope,
              started = SharingStarted.WhileSubscribed(5000),
              initialValue = DashboardState())

  val lastSevenDaysConsumption =
      dashboardUseCase
          .getLastSevenDaysConsumption()
          .stateIn(
              scope = viewModelScope,
              started = SharingStarted.WhileSubscribed(5000),
              initialValue = emptyList())

  init {
    viewModelScope.launch { _date.value = dashboardUseCase.fetchCurrentDate() }
  }

  fun onEvent(event: DashboardEvent) {
    when (event) {
      is DashboardEvent.AddConsumption -> {
        viewModelScope.launch { dashboardUseCase.insertQuantity(event.consumption) }
      }
    }
  }

  private fun calculateDueAmount(basePrice: Int, consumedQuantity: Float): Float {
    return basePrice * consumedQuantity
  }
}
