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
  
  @Query("SELECT SUM(quantity) FROM consumption WHERE month LIKE :month")
  fun getConsumedQuantityOfMonth(month: String): Flow<Float>
  
  @Query("SELECT COUNT(*) FROM consumption WHERE month LIKE :month")
  fun getConsumedDaysInAMonth(month: String): Flow<Int>
  
  @Query("SELECT COUNT(*) FROM consumption WHERE date LIKE :date")
  fun getConsumptionAsPerDate(date: String): Flow<Int>
}
