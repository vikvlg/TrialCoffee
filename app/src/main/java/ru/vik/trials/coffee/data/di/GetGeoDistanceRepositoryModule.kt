package ru.vik.trials.coffee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.coffee.data.GetGeoDistanceRepositoryImpl
import ru.vik.trials.coffee.domain.GetGeoDistanceRepository

@Module
@InstallIn(SingletonComponent::class)
interface GetGeoDistanceRepositoryModule {
    @Binds
    fun bindGetGeoDistanceRepositoryImpl(implementation: GetGeoDistanceRepositoryImpl): GetGeoDistanceRepository
}