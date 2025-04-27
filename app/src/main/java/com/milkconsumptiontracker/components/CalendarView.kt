package com.milkconsumptiontracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.milkconsumptiontracker.utils.generateMonthDays
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarView(
    calendarState: MutableState<YearMonth>,
    consumedDates: List<LocalDate>,
    onMonthChange: (YearMonth) -> Unit,
    onDayClicked: (LocalDate) -> Unit
) {
  val today = LocalDate.now()
  val days = generateMonthDays(calendarState.value, consumedDates, today)
  val dayOfWeekOffset = calendarState.value.atDay(1).dayOfWeek.value % 7

  Column(modifier = Modifier.padding(16.dp)) {
    // Month Navigation
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = { onMonthChange(calendarState.value.minusMonths(1)) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
          }

          Text(
              text =
                  calendarState.value.month.name.lowercase().replaceFirstChar { it.uppercase() } +
                      " ${calendarState.value.year}",
              style = MaterialTheme.typography.titleLarge)

          IconButton(onClick = { onMonthChange(calendarState.value.plusMonths(1)) }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
          }
        }

    Spacer(modifier = Modifier.height(8.dp))

    // Weekday Headers
    Row(modifier = Modifier.fillMaxWidth()) {
      listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
        Text(
            text = it,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold)
      }
    }

    // Dates
    val paddedDays = List(dayOfWeekOffset) { null } + days

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxHeight(),
        content = {
          items(paddedDays.size) { index ->
            val day = paddedDays[index]
            Box(
                modifier =
                    Modifier.aspectRatio(1f)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                              day == null -> Color.Transparent
                              day.isFutureDate -> Color.LightGray
                              day.isConsumed -> Color(0xFF4CAF50) // Green
                              else -> Color(0xFFF44336) // Red
                            })
                        .clickable { onDayClicked(day?.date ?: return@clickable) },
                contentAlignment = Alignment.Center) {
                  if (day != null) {
                    Text(text = day.date.dayOfMonth.toString(), color = Color.White)
                  }
                }
          }
        })
  }
}
