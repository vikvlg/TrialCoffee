package ru.vik.trials.coffee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.coffee.data.GetLocationsRepositoryImpl
import ru.vik.trials.coffee.domain.GetLocationsRepository

@Module
@InstallIn(SingletonComponent::class)
interface GetLocationsRepositoryModule {
    @Binds
    fun bindGetLocationsRepositoryImpl(implementation: GetLocationsRepositoryImpl): GetLocationsRepository
}