package com.milkconsumptiontracker.presentation.price

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.milkconsumptiontracker.components.GeneralTopBar
import com.milkconsumptiontracker.components.MonthYearPickerDialog
import com.milkconsumptiontracker.components.TitleTextView
import com.milkconsumptiontracker.utils.SetSystemUiVisibility
import com.milkconsumptiontracker.utils.SnackBarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

data class PriceScreenNavigationArgs(val price: String = "")

@Destination(navArgsDelegate = PriceScreenNavigationArgs::class)
@Composable
fun BasePriceScreenRoute(navigator: DestinationsNavigator) {
  val viewModel: BasePriceViewModel = hiltViewModel()
  BasePriceScreen(
      viewModel = viewModel,
      onEvent = viewModel::onEvent,
      navigateBack = { navigator.navigateUp() },
      snackBarEventFlow = viewModel.snackBarEventFlow)
}

@Composable
private fun BasePriceScreen(
    viewModel: BasePriceViewModel,
    onEvent: (BasePriceEvent) -> Unit,
    navigateBack: () -> Unit,
    snackBarEventFlow: SharedFlow<SnackBarEvent>,
) {
  SetSystemUiVisibility(useFullScreen = false)
  val snackBarHostState = remember { SnackbarHostState() }
  val selectedMonth by viewModel.selectedMonth.collectAsState()
  val currentMonthBasePrice by viewModel.currentMonthBasePrice.collectAsState()
  var price by
      remember(currentMonthBasePrice) {
        mutableStateOf(
            TextFieldValue(
                text = currentMonthBasePrice, selection = TextRange(currentMonthBasePrice.length)))
      }
  var showDialog by remember { mutableStateOf(false) }
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current

  // Request focus and show the keyboard when the screen is displayed
  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
    keyboardController?.show()
  }
  LaunchedEffect(key1 = true) {
    snackBarEventFlow.collectLatest {
      when (it) {
        is SnackBarEvent.ShowSnackBar -> {
          snackBarHostState.showSnackbar(message = it.message)
        }
        else -> {
          // no-op
        }
      }
    }
  }

  MonthYearPickerDialog(
      showDialog = showDialog,
      onDismiss = { showDialog = false },
      onDateSelected = { monthAndYear -> viewModel.updateSelectedMonth(monthAndYear) })

  Scaffold(
      topBar = {
        GeneralTopBar(
            title = "Price Details",
            onBackButtonClick = {
              keyboardController?.hide()
              navigateBack()
            },
        )
      },
      containerColor = Color.White,
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)) {
              TitleTextView(
                  title = "Enter Price",
                  color = Color.Black,
                  setBold = true,
                  modifier = Modifier.padding(top = 8.dp))
              OutlinedTextField(
                  value = price,
                  onValueChange = { newValue -> price = newValue },
                  label = { Text("Price (₹)") },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                  modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                  textStyle = TextStyle(color = Color.Black))

              Spacer(modifier = Modifier.height(24.dp))
              TitleTextView(title = "Select Month", setBold = true, color = Color.Black)
              OutlinedButton(onClick = { showDialog = true }) { Text(text = selectedMonth) }

              Spacer(modifier = Modifier.height(24.dp))

              Button(
                  onClick = {
                    onEvent(BasePriceEvent.UpdateBasePrice(basePrice = price.text))
                    keyboardController?.hide()
                    navigateBack()
                  },
                  modifier = Modifier.align(Alignment.End)) {
                    Text("Save")
                  }
            }
      }
}
