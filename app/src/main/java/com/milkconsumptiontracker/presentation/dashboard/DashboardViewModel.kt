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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

  val currentMonthBasePrice: StateFlow<String> =
      date
          .map { date -> basePriceUseCase.getCurrentMonthBasePrice(date.month) }
          .flowOn(Dispatchers.IO)
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "--")

  val lastSevenDaysConsumption =
      milkConsumptionUseCase
          .getLastSevenDaysConsumption()
          .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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
