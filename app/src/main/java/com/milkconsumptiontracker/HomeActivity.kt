package com.milkconsumptiontracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.milkconsumptiontracker.presentation.NavGraphs
import com.milkconsumptiontracker.ui.theme.MilkConsumptionTrackerTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MilkConsumptionTrackerTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}