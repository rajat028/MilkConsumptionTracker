package com.milkconsumptiontracker.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun GeneralTextView(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
) {
  Text(text = title, style = MaterialTheme.typography.bodySmall, color = color, modifier = modifier)
}
