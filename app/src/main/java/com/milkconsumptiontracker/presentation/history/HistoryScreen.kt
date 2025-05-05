package com.milkconsumptiontracker.presentation.history

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.milkconsumptiontracker.components.CalendarView
import com.milkconsumptiontracker.components.ConsumptionCell
import com.milkconsumptiontracker.components.GeneralTopBar
import com.milkconsumptiontracker.components.LabelTextView
import com.milkconsumptiontracker.components.QuantityBottomSheetContainer
import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.utils.SetSystemUiVisibility
import com.milkconsumptiontracker.utils.isZeroOrEmpty
import com.milkconsumptiontracker.utils.rememberCalendarState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDate
import kotlinx.coroutines.launch

data class HistoryScreenNavigationArgs(val currentDate: String = "")

@Destination(navArgsDelegate = HistoryScreenNavigationArgs::class)
@Composable
fun HistoryScreenRoute(navigator: DestinationsNavigator) {
  val viewModel: HistoryViewModel = hiltViewModel()
  HistoryScreen(viewModel, navigateBack = { navigator.navigateUp() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    navigateBack: () -> Unit,
) {
  SetSystemUiVisibility(useFullScreen = false)
  val context = LocalContext.current
  val calendarState = rememberCalendarState()
  val consumedDates by viewModel.consumedDates.collectAsState()
  val consumptionOfDate by viewModel.consumptionOfDate.collectAsState()
  val historyState by viewModel.historyViewState.collectAsState()
  var selectedDate by remember { mutableStateOf(LocalDate.now()) }
  val coroutineScope = rememberCoroutineScope()
  val quantityBottomSheetState = rememberModalBottomSheetState()
  var quantityState by remember { mutableStateOf(Consumption.EMPTY) }
  LaunchedEffect(calendarState.value) {
    viewModel.fetchConsumptionDataOfMonth(calendarState.value, selectedDate)
  }

  Scaffold(
      topBar = {
        GeneralTopBar(
            title = "History",
            onBackButtonClick = { navigateBack() },
        )
      },
      containerColor = Color.White,
  ) { paddingValues ->
    Column(Modifier.padding(paddingValues)) {
      ConsumptionCard(historyState)

      CalendarView(
          calendarState,
          consumedDates,
          selectedDate,
          onMonthChange = { updateMonth -> calendarState.value = updateMonth },
          onDayClicked = { date ->
            selectedDate = date
            viewModel.getConsumptionOfDate(date)
          },
      )
      Spacer(Modifier.height(8.dp).background(Color.Black))

      ConsumptionCell(
          consumption = consumptionOfDate,
          onQuantityEdit = {
            if (historyState.basePrice.isZeroOrEmpty()) {
              Toast.makeText(
                      context,
                      "Base Price is necessary before entering quantity",
                      Toast.LENGTH_SHORT)
                  .show()
            } else {
              coroutineScope.launch {
                quantityState = consumptionOfDate
                quantityBottomSheetState.show()
              }
            }
          },
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
    }

    if (quantityBottomSheetState.isVisible) {
      QuantityBottomSheetContainer(
          consumption = quantityState,
          sheetState = quantityBottomSheetState,
          onQuantitySelected = { quantity ->
            coroutineScope.launch {
              viewModel.onEvent(
                  HistoryEvent.UpdateConsumption(quantityState.copy(quantity = quantity)))
              quantityBottomSheetState.hide()
            }
          },
          onDismiss = { coroutineScope.launch { quantityBottomSheetState.hide() } })
    }
  }
}

@Composable
private fun ConsumptionCard(historyState: HistoryViewState) {
  Card(
      elevation = CardDefaults.cardElevation(6.dp),
      modifier = Modifier.fillMaxWidth().padding(12.dp),
      colors = CardDefaults.cardColors(Color.White)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            LabelTextView(
                title = "Base Price - ${historyState.basePrice} (${historyState.month})",
                color = Color.Black,
                setBold = true)
          }

          Spacer(Modifier.height(8.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            LabelTextView(
                title = "Due Amount",
                color = Color.Black,
                setBold = true,
                modifier = Modifier.weight(1f))
            LabelTextView(title = historyState.dueAmount, color = Color.DarkGray, setBold = true)
          }

          Spacer(Modifier.height(2.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            LabelTextView(
                title = "Total Consumption",
                color = Color.Black,
                setBold = true,
                modifier = Modifier.weight(1f))
            LabelTextView(
                title = historyState.consumedQuantity, color = Color.DarkGray, setBold = true)
          }
        }
      }
}
