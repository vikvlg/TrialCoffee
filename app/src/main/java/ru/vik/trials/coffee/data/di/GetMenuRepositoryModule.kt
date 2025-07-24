package ru.vik.trials.coffee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.coffee.data.GetMenuRepositoryImpl
import ru.vik.trials.coffee.domain.GetMenuRepository

@Module
@InstallIn(SingletonComponent::class)
interface GetMenuRepositoryModule {
    @Binds
    fun bindGetMenuRepositoryImpl(implementation: GetMenuRepositoryImpl): GetMenuRepository
}