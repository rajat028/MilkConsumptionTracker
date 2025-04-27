package com.milkconsumptiontracker.utils

import android.app.Activity
import android.view.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetSystemUiVisibility(
    useFullScreen: Boolean
) {
    val window = (LocalView.current.context as Activity).window
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    
    SideEffect {
        insetsController.isAppearanceLightStatusBars = !useFullScreen // Light icons for dark backgrounds
        
        if (useFullScreen) {
            insetsController.hide(WindowInsets.Type.statusBars())
        } else {
            insetsController.show(WindowInsets.Type.statusBars())
        }
    }
}