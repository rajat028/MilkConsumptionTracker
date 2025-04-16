package com.milkconsumptiontracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.milkconsumptiontracker.domain.model.PriceSnapshot
import kotlinx.coroutines.flow.Flow

@Dao
interface BasePriceDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPrice(priceSnapshot: PriceSnapshot)
  
  @Query("SELECT price FROM PRICESNAPSHOT WHERE monthAndYear = :monthAndYear")
  suspend fun getPrice(monthAndYear: String): Flow<Int>
}
