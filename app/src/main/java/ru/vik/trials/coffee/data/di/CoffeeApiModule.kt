package ru.vik.trials.coffee.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.coffee.data.CoffeeApi
import ru.vik.trials.coffee.data.NetworkClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoffeeApiModule {
    @Provides
    @Singleton
    fun provideCoffeeApiService(client: NetworkClient): CoffeeApi = client.provideApiService()
}