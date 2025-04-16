package com.milkconsumptiontracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

fun ovalBottomShape(): Shape {
  return GenericShape { size, _ ->
    val path =
        Path().apply {
          moveTo(0f, 0f) // Start from top-left
          lineTo(size.width, 0f) // Draw top straight line
          lineTo(size.width, size.height * 0.7f) // Move to bottom-right before curve

          // Draw an oval curve at the bottom
          quadraticTo(
              size.width / 2,
              size.height, // Control point (center bottom)
              0f,
              size.height * 0.7f // End point (bottom-left)
              )

          close()
        }
    addPath(path)
  }
}

@Composable
fun OvalBottomBox()  {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(240.dp)
              .background(
                  brush = Brush.verticalGradient(colors = listOf(Color.Blue, Color.Cyan)),
                  shape = ovalBottomShape()))
}

@Preview
@Composable
fun PreviewOvalBottomBox() {
  OvalBottomBox()
}
