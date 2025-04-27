package com.milkconsumptiontracker.data.local

import androidx.room.Dao
import androidx.room.Query
import com.milkconsumptiontracker.domain.model.Consumption
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
  @Query("SELECT * FROM consumption WHERE quantity > 0 AND month LIKE :month")
  fun getConsumptionDatesOfMonth(month: String): Flow<List<Consumption>>
}
