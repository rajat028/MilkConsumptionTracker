package com.milkconsumptiontracker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressBarWithProgressText() {
    Box {
        CircularProgressIndicator(
            progress = { 0.2f },
            strokeWidth = 6.dp,
            color = Color.Blue,
            trackColor = Color.Gray,
            modifier = Modifier.size(60.dp)
        )
        
        Text(
            text = "2",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold),
            modifier = Modifier.align(Alignment.Center),
            color = Color.Black,
            fontSize = 18.sp
        )
    }
}
