package com.milkconsumptiontracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerticalLine(modifier: Modifier = Modifier) {
  Box(modifier = modifier.width(1.dp).height(100.dp).background(Color.Black))
}
