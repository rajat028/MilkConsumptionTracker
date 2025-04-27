package com.milkconsumptiontracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.milkconsumptiontracker.domain.model.Consumption
import com.milkconsumptiontracker.domain.model.PriceSnapshot

@Database(entities = [Consumption::class, PriceSnapshot::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun milkConsumptionDao(): ConsumptionDao

  abstract fun basePriceDao(): BasePriceDao
  
  abstract fun historyDao(): HistoryDao
}
