package com.milkconsumptiontracker.presentation.dashboard

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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.milkconsumptiontracker.domain.model.DateSnapshot
import com.milkconsumptiontracker.presentation.destinations.BasePriceScreenRouteDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination(start = true)
@Composable
fun DashboardScreenRoute(navigator: DestinationsNavigator) {
  DashboardScreen(onEditBasePriceClick = { navigator.navigate(BasePriceScreenRouteDestination()) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen(
    onEditBasePriceClick: () -> Unit,
) {
  val viewModel: DashboardViewModel = hiltViewModel()
  val dateSnapshot by viewModel.date.collectAsState()
  val consumptionList by viewModel.lastSevenDaysConsumption.collectAsState()
  val currentMonthBasePrice by viewModel.currentMonthBasePrice.collectAsState()

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val quantityBottomSheetState = rememberModalBottomSheetState()

  Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
    OvalBottomBox()
    Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)) {
      TopBar(
          title = "Milk Tracker", navigationIconClick = { /*TODO*/ }, navigationIconVisible = false)

      BasePrice(currentMonthBasePrice, dateSnapshot, onEditBasePriceClick)

      Card(
          modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
              Column(modifier = Modifier.padding(16.dp)) {
                ConsumptionAndNonConsumptionProgress()
                CurrentMonthConsumptionPrice()
              }
              VerticalLine()
              DueAmount()
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

            GeneralTextView(title = "View All >", color = Color.Blue)
          }

      LazyColumn(
          modifier = Modifier,
          verticalArrangement = Arrangement.spacedBy(16.dp),
          contentPadding = PaddingValues(top = 12.dp)) {
            items(consumptionList) { consumption -> ConsumptionCell(consumption) }
          }
    }

    FloatingActionButton(
        onClick = { coroutineScope.launch { quantityBottomSheetState.show() } },
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

  if (quantityBottomSheetState.isVisible) {
    QuantityBottomSheetContainer(
        currentDate = dateSnapshot.date,
        currentDay = dateSnapshot.day,
        sheetState = quantityBottomSheetState,
        onQuantitySelected = { quantity ->
          coroutineScope.launch {
            viewModel.onEvent(
                DashboardEvent.AddConsumption(quantity = quantity, dateSnapshot = dateSnapshot))
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
private fun DueAmount() {
  Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    LabelTextView(title = "Due Amount", color = Color.Gray)
    LabelTextView(
        title = "₹ 5000",
        setBold = true,
        color = Color.Black,
        fontSize = 16.sp,
        modifier = Modifier.padding(top = 4.dp))
  }
}

@Composable
private fun ConsumptionAndNonConsumptionProgress() {
  Row(verticalAlignment = Alignment.CenterVertically) {
    ConsumedDataWithProgress()
    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
    NonConsumedDataWithProgress()
  }
}

@Composable
private fun CurrentMonthConsumptionPrice() {
  Row(verticalAlignment = Alignment.CenterVertically) {
    LabelTextView(title = "Current month consumption:", color = Color.Gray, setBold = true)
    LabelTextView(title = "50 L", Modifier.padding(horizontal = 8.dp), color = Color.Gray)
  }
}

@Composable
private fun NonConsumedDataWithProgress() {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    CircularProgressBarWithProgressText()
    GeneralTextView(
        title = "Non-consumed days",
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 8.dp))
  }
}

@Composable
private fun ConsumedDataWithProgress() {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    CircularProgressBarWithProgressText()
    GeneralTextView(
        title = "Consumed days", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
  }
}

@Preview
@Composable
fun PreviewHomeScreen() {
  DashboardScreen({})
}
