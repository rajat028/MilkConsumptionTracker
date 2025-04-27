package com.milkconsumptiontracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milkconsumptiontracker.R
import com.milkconsumptiontracker.domain.model.Consumption
import kotlinx.coroutines.launch

@Composable
fun QuantityBottomSheetContent(
    consumption: Consumption,
    onQuantitySelected: (Float) -> Unit,
    onDismiss: () -> Unit
) {
  val quantity = if (consumption.quantity == 0f) 0.25f else consumption.quantity
  var value by remember { mutableFloatStateOf(quantity) }
  val coroutineScope = rememberCoroutineScope()

  Column(
      modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally) {
        TitleTextView(
            title = "${consumption.date}, ${consumption.day}", color = Color.Black, setBold = true)

        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Button(
              modifier = Modifier.size(36.dp),
              contentPadding = PaddingValues(0.dp),
              shape = CircleShape,
              onClick = { if (value > 0) value -= 0.25f },
              colors =
                  ButtonDefaults.buttonColors(
                      containerColor = Color.LightGray, contentColor = Color.Black)) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = "subtract")
              }

          Text(
              text = value.toString(),
              modifier = Modifier.width(120.dp),
              fontSize = 24.sp,
              color = Color.Black,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center)

          Button(
              modifier = Modifier.size(36.dp),
              contentPadding = PaddingValues(0.dp),
              shape = CircleShape,
              onClick = { value += 0.25f },
              colors =
                  ButtonDefaults.buttonColors(
                      containerColor = Color.LightGray, contentColor = Color.Black)) {
                Icon(Icons.Default.Add, contentDescription = "add")
              }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
          Button(
              onClick = { coroutineScope.launch { onDismiss() } },
              shape = MaterialTheme.shapes.large) {
                LabelTextView(title = "Cancel", color = Color.Black, setBold = true)
              }

          Button(
              onClick = { coroutineScope.launch { onQuantitySelected(value) } },
              shape = MaterialTheme.shapes.large) {
                LabelTextView(title = "Done", color = Color.Black, setBold = true)
              }
        }
      }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityBottomSheetContainer(
    consumption: Consumption,
    sheetState: SheetState,
    onQuantitySelected: (Float) -> Unit,
    onDismiss: () -> Unit
) {
  ModalBottomSheet(
      containerColor = Color.White,
      sheetState = sheetState,
      onDismissRequest = { onDismiss() },
      content = {
        QuantityBottomSheetContent(
            consumption = consumption,
            onQuantitySelected = onQuantitySelected,
            onDismiss = onDismiss)
      })
}
