package ru.vik.trials.coffee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.coffee.data.GetGeoLocationRepositoryImpl
import ru.vik.trials.coffee.domain.GetGeoLocationRepository

@Module
@InstallIn(SingletonComponent::class)
interface GetGeoLocationRepositoryModule {
    @Binds
    fun bindGetGeoLocationRepositoryImpl(implementation: GetGeoLocationRepositoryImpl): GetGeoLocationRepository
}