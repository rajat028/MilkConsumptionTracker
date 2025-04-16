package com.milkconsumptiontracker.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralTopBar(
    title: String,
    onBackButtonClick: () -> Unit,
) {
  TopAppBar(
      title = { ToolBarTextView(title = title, color = Color.Black) },
      navigationIcon = {
        IconButton(onClick = { onBackButtonClick() }) {
          Icon(
              imageVector = Icons.AutoMirrored.Default.ArrowBack,
              tint = Color.Black,
              contentDescription = "navigate back")
        }
      },
      colors =
          TopAppBarDefaults.topAppBarColors(
              containerColor = Color.White,
              titleContentColor = Color.Black,
          ),
      modifier = Modifier.shadow(4.dp))
}
