package com.milkconsumptiontracker.presentation.history

data class HistoryViewState(
    val basePrice: String = "",
    val consumedQuantity: String = "",
    val dueAmount: String = "",
    val month : String = "",
)
