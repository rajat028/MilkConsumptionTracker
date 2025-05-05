package com.milkconsumptiontracker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.milkconsumptiontracker.R
import com.milkconsumptiontracker.domain.model.Consumption

@Composable
fun ConsumptionCell(
    consumption: Consumption,
    onQuantityEdit: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
  Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          LabelTextView(title = consumption.day, color = Color.Gray, textAlign = TextAlign.Start)
          LabelTextView(
              title = consumption.displayDate, color = Color.Black, setBold = true, fontSize = 16.sp)
        }
        Row(verticalAlignment = Alignment.Bottom) {
          LabelTextView(
              title = "${consumption.quantity}Ltr",
              color = Color.Black,
              fontSize = 18.sp,
              setBold = true)
          Icon(
              painter = painterResource(id = R.drawable.ic_edit_black),
              contentDescription = "Vehicle",
              modifier =
                  Modifier.padding(horizontal = 4.dp).clickable {
                    onQuantityEdit()
                  })
        }
      }
}
