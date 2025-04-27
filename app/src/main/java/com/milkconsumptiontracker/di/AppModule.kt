package com.milkconsumptiontracker.di

import android.app.Application
import androidx.room.Room
import com.milkconsumptiontracker.data.local.AppDatabase
import com.milkconsumptiontracker.data.local.ConsumptionDao
import com.milkconsumptiontracker.data.local.BasePriceDao
import com.milkconsumptiontracker.data.local.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "milkconsumption.db").build()
  }

  @Provides
  @Singleton
  fun provideMilkConsumptionDao(database: AppDatabase): ConsumptionDao {
    return database.milkConsumptionDao()
  }
  
  @Provides
  @Singleton
  fun provideBasePriceDao(database: AppDatabase): BasePriceDao {
    return database.basePriceDao()
  }
  
  @Provides
  @Singleton
  fun provideHistoryDao(database: AppDatabase): HistoryDao {
    return database.historyDao()
  }
}
