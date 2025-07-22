package ru.vik.trials.coffee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vik.trials.coffee.data.SignInRepositoryImpl
import ru.vik.trials.coffee.domain.SignInRepository

@Module
@InstallIn(SingletonComponent::class)
interface SingInRepositoryModule {
    @Binds
    fun bindSignInRepositoryImpl(implementation: SignInRepositoryImpl): SignInRepository
}