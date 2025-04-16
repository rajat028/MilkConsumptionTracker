package com.milkconsumptiontracker.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun LabelTextView(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    setBold: Boolean = false,
    fontSize : TextUnit = 14.sp,
) {
  Text(
      text = title,
      style =
          MaterialTheme.typography.labelSmall.copy(
              fontWeight = if (setBold) FontWeight.Bold else FontWeight.Normal),
      color = color,
      fontSize = fontSize,
      textAlign = TextAlign.Unspecified,
      lineHeight = 18.sp,
      modifier = modifier)
}
