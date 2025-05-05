package com.milkconsumptiontracker.presentation.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.milkconsumptiontracker.R
import com.milkconsumptiontracker.components.CircularProgressBarWithProgressText
import com.milkconsumptiontracker.components.ConsumptionCell
import com.milkconsumptiontracker.components.GeneralTextView
import com.milkconsumptiontracker.components.LabelTextView
import com.milkconsumptiontracker.components.OvalBottomBox
import com.milkconsumptiontracker.components.QuantityBottomSheetContainer
import com.milkconsumptiontracker.components.TitleTextView
import com.milkconsumptiontracker.components.TopBar
import com.milkconsumptiontracker.components.VerticalLine
import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.model.DateSnapshot
import com.milkconsumptiontracker.presentation.destinations.BasePriceScreenRouteDestination
import com.milkconsumptiontracker.presentation.destinations.HistoryScreenRouteDestination
import com.milkconsumptiontracker.utils.isZeroOrEmpty
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination(start = true)
@Composable
fun DashboardScreenRoute(navigator: DestinationsNavigator) {
  DashboardScreen(
      onEditBasePriceClick = { navigator.navigate(BasePriceScreenRouteDestination()) },
      onHistoryClicked = { navigator.navigate(HistoryScreenRouteDestination()) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(onEditBasePriceClick: () -> Unit, onHistoryClicked: () -> Unit = {}) {
  val viewModel: DashboardViewModel = hiltViewModel()
  val dateSnapshot by viewModel.date.collectAsState()
  var quantityState by
      remember(dateSnapshot) {
        mutableStateOf(
            Consumption(
                quantity = 0.25f,
                displayDate = dateSnapshot.displayDate,
                date = dateSnapshot.date,
                day = dateSnapshot.day,
                month = dateSnapshot.month))
      }
  val state by viewModel.state.collectAsStateWithLifecycle()
  val lastSevenDaysConsumption by viewModel.lastSevenDaysConsumption.collectAsState(emptyList())

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val quantityBottomSheetState = rememberModalBottomSheetState()

  Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
    OvalBottomBox()
    Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
      TopBar(
          title = "Milk Tracker", navigationIconClick = { /*TODO*/ }, navigationIconVisible = false)

      BasePrice(state.currentMonthBasePrice, dateSnapshot, onEditBasePriceClick)

      Card(
          modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
              Column(modifier = Modifier.padding(16.dp)) {
                ConsumptionAndNonConsumptionProgress(
                    state.consumedDaysCount,
                    state.nonConsumedDaysCount,
                    state.consumedDaysInAMonthProgress,
                    state.nonConsumedDaysInAMonthProgress)
                CurrentMonthConsumption(state.currentMonthConsumedQuantity, dateSnapshot.month)
              }
              VerticalLine()
              DueAmount(dateSnapshot.month, state.currentMonthDueAmount)
            }
          }

      Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically) {
            TitleTextView(
                title = "Last 7 days consumption",
                setBold = true,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                color = Color.Black)

            GeneralTextView(
                title = "View All >",
                color = Color.Blue,
                modifier = Modifier.padding(4.dp).clickable { onHistoryClicked() })
          }

      LazyColumn(
          modifier = Modifier,
          verticalArrangement = Arrangement.spacedBy(16.dp),
          contentPadding = PaddingValues(top = 12.dp)) {
            items(lastSevenDaysConsumption) { consumption ->
              ConsumptionCell(
                  consumption,
                  onQuantityEdit = {
                    coroutineScope.launch {
                      quantityState = consumption
                      quantityBottomSheetState.show()
                    }
                  })
            }
          }
    }

    if (state.isTodayConsumptionUpdated.not()) {
      FloatingActionButton(
          onClick = {
            if (state.currentMonthBasePrice.isZeroOrEmpty()) {
              Toast.makeText(
                      context,
                      "Base Price is necessary before entering quantity",
                      Toast.LENGTH_SHORT)
                  .show()
            } else {
              coroutineScope.launch { quantityBottomSheetState.show() }
            }
          },
          containerColor = Color.Blue,
          modifier =
              Modifier.align(Alignment.BottomEnd)
                  .padding(WindowInsets.navigationBars.asPaddingValues())
                  .padding(horizontal = 16.dp, vertical = 16.dp)) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add/Update Quantity",
                tint = Color.White)
          }
    }
  }

  if (quantityBottomSheetState.isVisible) {
    QuantityBottomSheetContainer(
        consumption = quantityState,
        sheetState = quantityBottomSheetState,
        onQuantitySelected = { quantity ->
          coroutineScope.launch {
            viewModel.onEvent(
                DashboardEvent.AddConsumption(quantityState.copy(quantity = quantity)))
            quantityBottomSheetState.hide()
          }
        },
        onDismiss = { coroutineScope.launch { quantityBottomSheetState.hide() } })
  }
}

@Composable
private fun BasePrice(
    currentMonthBasePrice: String,
    dateSnapshot: DateSnapshot,
    onEditBasePriceClick: () -> Unit,
) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
      verticalAlignment = Alignment.Bottom) {
        TitleTextView(title = "Base Price:", setBold = true)
        TitleTextView(title = "$currentMonthBasePrice / Ltr", Modifier.padding(horizontal = 8.dp))
        GeneralTextView(title = "(${dateSnapshot.month})")
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_edit_white),
            contentDescription = "Edit Price",
            modifier = Modifier.padding(horizontal = 4.dp).clickable { onEditBasePriceClick() })
      }
}

@Composable
private fun DueAmount(month: String, currentMonthDueAmount: Float) {
  Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    LabelTextView(title = "Due Amount", color = Color.Gray)
    GeneralTextView(title = "($month)", color = Color.Gray)
    LabelTextView(
        title = "₹ $currentMonthDueAmount",
        setBold = true,
        color = Color.Black,
        fontSize = 16.sp,
        modifier = Modifier.padding(top = 4.dp))
  }
}

@Composable
private fun ConsumptionAndNonConsumptionProgress(
    consumedDaysCount: Int,
    nonConsumedDaysCount: Int,
    consumedDaysInAMonthProgress: Float,
    nonConsumedDaysInAMonthProgress: Float
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    ConsumedDataWithProgress(consumedDaysCount, consumedDaysInAMonthProgress)
    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
    NonConsumedDataWithProgress(nonConsumedDaysCount, nonConsumedDaysInAMonthProgress)
  }
}

@Composable
private fun CurrentMonthConsumption(currentMonthConsumedQuantity: Float, month: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    LabelTextView(title = "Consumption($month)", color = Color.Gray, setBold = true)
    LabelTextView(
        title = "$currentMonthConsumedQuantity Ltrs",
        Modifier.padding(horizontal = 8.dp),
        color = Color.Gray)
  }
}

@Composable
private fun NonConsumedDataWithProgress(nonConsumedDaysCount: Int, progress: Float) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    CircularProgressBarWithProgressText(nonConsumedDaysCount, progress)
    GeneralTextView(
        title = "Non-consumed days",
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 8.dp))
  }
}

@Composable
private fun ConsumedDataWithProgress(consumedDaysCount: Int, progress: Float) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    CircularProgressBarWithProgressText(consumedDaysCount, progress)
    GeneralTextView(
        title = "Consumed days", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
  }
}

@Preview
@Composable
fun PreviewHomeScreen() {
  DashboardScreen({})
}
