package com.milkconsumptiontracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity data class PriceSnapshot(val price: Int, @PrimaryKey val monthAndYear: String)
