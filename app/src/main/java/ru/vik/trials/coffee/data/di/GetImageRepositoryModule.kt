package ru.vik.trials.coffee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.coffee.data.GetImageRepositoryImpl
import ru.vik.trials.coffee.domain.GetImageRepository

@Module
@InstallIn(SingletonComponent::class)
interface GetImageRepositoryModule {
    @Binds
    fun bindGetImageRepositoryImpl(implementation: GetImageRepositoryImpl): GetImageRepository
}