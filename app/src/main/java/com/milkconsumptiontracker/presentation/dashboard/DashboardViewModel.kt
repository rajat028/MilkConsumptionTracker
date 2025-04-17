package com.milkconsumptiontracker.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milkconsumptiontracker.domain.model.DateSnapshot
import com.milkconsumptiontracker.domain.usecase.BasePriceUseCase
import com.milkconsumptiontracker.domain.usecase.MilkConsumptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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
    private val milkConsumptionUseCase: MilkConsumptionUseCase,
    private val basePriceUseCase: BasePriceUseCase
) : ViewModel() {

  private val _date = MutableStateFlow(DateSnapshot.default())
  val date: StateFlow<DateSnapshot> = _date

  private val _state = MutableStateFlow(DashboardState())
  @OptIn(ExperimentalCoroutinesApi::class)
  val state =
      combine(
              _state,
              date.flatMapLatest { currentDate ->
                basePriceUseCase.getCurrentMonthBasePrice(currentDate.month)
              },
              milkConsumptionUseCase.getLastSevenDaysConsumption(),
          ) { state, basePrice, consumptions ->
            state.copy(currentMonthBasePrice = basePrice, lastSevenDaysConsumption = consumptions)
          }
          .stateIn(
              scope = viewModelScope,
              started = SharingStarted.WhileSubscribed(5000),
              initialValue = DashboardState())

  init {
    fetchCurrentDate()
  }

  fun onEvent(event: DashboardEvent) {
    when (event) {
      is DashboardEvent.AddConsumption -> {
        viewModelScope.launch {
          milkConsumptionUseCase.insertQuantity(event.quantity, event.dateSnapshot)
        }
      }
    }
  }

  private fun fetchCurrentDate() {
    val currentTime = Calendar.getInstance().time
    _date.value =
        DateSnapshot(
            day = dayFormatter.format(currentTime),
            date = dateFormatter.format(currentTime),
            month = monthFormatter.format(currentTime))
  }

  companion object {
    private val dayFormatter = SimpleDateFormat("EEE", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val monthFormatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
  }
}
