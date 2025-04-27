package com.milkconsumptiontracker.di

import com.milkconsumptiontracker.data.repository.BasePriceRepositoryImpl
import com.milkconsumptiontracker.data.repository.HistoryRepositoryImpl
import com.milkconsumptiontracker.data.repository.MilkConsumptionRepositoryImpl
import com.milkconsumptiontracker.domain.repository.BasePriceRepository
import com.milkconsumptiontracker.domain.repository.HistoryRepository
import com.milkconsumptiontracker.domain.repository.MilkConsumptionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Singleton
  @Binds
  abstract fun bindAddQuantityRepository(
      impl: MilkConsumptionRepositoryImpl
  ): MilkConsumptionRepository

  @Singleton
  @Binds
  abstract fun bindPriceRepository(impl: BasePriceRepositoryImpl): BasePriceRepository

  @Singleton
  @Binds
  abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository
}
