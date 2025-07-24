package ru.vik.trials.coffee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.coffee.data.SignUpRepositoryImpl
import ru.vik.trials.coffee.domain.SignUpRepository

@Module
@InstallIn(SingletonComponent::class)
interface SingUpRepositoryModule {
    @Binds
    fun bindSignUpRepositoryImpl(implementation: SignUpRepositoryImpl): SignUpRepository
}