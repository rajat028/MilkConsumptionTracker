package com.milkconsumptiontracker.presentation.price

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milkconsumptiontracker.domain.usecase.BasePriceUseCase
import com.milkconsumptiontracker.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class BasePriceViewModel @Inject constructor(private val useCase: BasePriceUseCase) : ViewModel() {

  private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
  val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

  private val _selectedMonth =
      MutableStateFlow(
          SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Calendar.getInstance().time))
  val selectedMonth: StateFlow<String> = _selectedMonth

  @OptIn(ExperimentalCoroutinesApi::class)
  val currentMonthBasePrice: StateFlow<String> =
      selectedMonth
          .flatMapLatest { month -> useCase.getCurrentMonthBasePrice(month).map { it.toString() } }
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

  fun onEvent(event: BasePriceEvent) {
    when (event) {
      is BasePriceEvent.UpdateBasePrice -> {
        viewModelScope.launch {
          if (event.basePrice.isEmpty()) {
            _snackBarEventFlow.emit(SnackBarEvent.ShowSnackBar("Enter a valid price"))
            return@launch
          }
          try {
            useCase.updateBasePrice(event.basePrice, _selectedMonth.value)
            _snackBarEventFlow.emit(SnackBarEvent.ShowSnackBar("Price updated successfully "))
          } catch (exception: Exception) {
            _snackBarEventFlow.emit(
                SnackBarEvent.ShowSnackBar(exception.message ?: "Unknown error"))
          }
        }
      }
    }
  }

  fun updateSelectedMonth(monthAndYear: String) {
    _selectedMonth.value = monthAndYear
  }
}
