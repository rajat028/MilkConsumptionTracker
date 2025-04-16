package com.milkconsumptiontracker.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ToolBarTextView(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
) {
  Text(
      text = title,
      style = MaterialTheme.typography.displaySmall.copy(fontSize = 24.sp),
      color = color,
      modifier = modifier)
}
