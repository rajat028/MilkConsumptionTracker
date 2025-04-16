package com.milkconsumptiontracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.milkconsumptiontracker.domain.model.Consumption
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumptionDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertConsumption(consumption: Consumption)
  
  @Query("SELECT * FROM consumption WHERE date BETWEEN :startDate AND :endDate")
  fun getConsumptionBetweenDates(startDate: String, endDate: String): Flow<List<Consumption>>
}
