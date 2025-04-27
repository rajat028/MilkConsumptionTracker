package com.milkconsumptiontracker.presentation.history

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.milkconsumptiontracker.components.CalendarView
import com.milkconsumptiontracker.components.GeneralTopBar
import com.milkconsumptiontracker.utils.SetSystemUiVisibility
import com.milkconsumptiontracker.utils.rememberCalendarState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class HistoryScreenNavigationArgs(val currentDate: String = "")

@Destination(navArgsDelegate = HistoryScreenNavigationArgs::class)
@Composable
fun HistoryScreenRoute(navigator: DestinationsNavigator) {
  val viewModel: HistoryViewModel = hiltViewModel()
  HistoryScreen(viewModel, navigateBack = { navigator.navigateUp() })
}

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    navigateBack: () -> Unit,
) {
  SetSystemUiVisibility(useFullScreen = false)
  val calendarState = rememberCalendarState()
  val consumedDates by viewModel.consumedDates.collectAsState()
  LaunchedEffect(calendarState.value) { viewModel.fetchConsumptionDataOfMonth(calendarState.value) }

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
      CalendarView(
          calendarState,
          consumedDates,
          onMonthChange = { updateMonth -> calendarState.value = updateMonth },
          onDayClicked = { date ->
            Log.e("Consumption", "${viewModel.getConsumptionOfDate(date)?.quantity}")
          },
      )
    }
  }
}
