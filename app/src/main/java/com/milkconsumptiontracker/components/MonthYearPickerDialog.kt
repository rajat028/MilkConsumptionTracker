package com.milkconsumptiontracker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@Composable
fun MonthYearPickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (monthAndYear: String) -> Unit
) {
  if (showDialog) {
    val months =
        listOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December")
    val currentMonth = remember { mutableStateOf(LocalDate.now().monthValue - 1) }
    val currentYear = remember { mutableStateOf(LocalDate.now().year) }

    Dialog(onDismissRequest = onDismiss) {
      Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Select Month & Year", style = MaterialTheme.typography.titleMedium)

          Spacer(modifier = Modifier.height(16.dp))

          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween) {
                // Month Picker
                DropdownMenuBox(
                    label = "Month",
                    options = months,
                    selectedIndex = currentMonth.value,
                    onSelected = { currentMonth.value = it })

                // Year Picker
                DropdownMenuBox(
                    label = "Year",
                    options = (2020..2030).map { it.toString() },
                    selectedIndex = (currentYear.value - 2020),
                    onSelected = { currentYear.value = 2020 + it })
              }

          Spacer(modifier = Modifier.height(16.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onDismiss) { Text("Cancel") }
            TextButton(
                onClick = {
                  onDateSelected(
                      "${formatMonthIndexToShortName(currentMonth.value)} ${currentYear.value}")
                  onDismiss()
                }) {
                  Text("OK")
                }
          }
        }
      }
    }
  }
}

@Composable
fun DropdownMenuBox(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }

  Box {
    OutlinedTextField(
        value = options[selectedIndex],
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier.width(150.dp).clickable { expanded = true })
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      options.forEachIndexed { index, item ->
        DropdownMenuItem(
            text = { Text(item) },
            onClick = {
              onSelected(index)
              expanded = false
            })
      }
    }
  }
}

fun formatMonthIndexToShortName(index: Int): String {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.MONTH, index)
  val formatter = SimpleDateFormat("MMM", Locale.getDefault()) // "MMM" for short name
  return formatter.format(calendar.time)
}
