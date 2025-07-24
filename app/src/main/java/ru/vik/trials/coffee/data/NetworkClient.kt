package ru.vik.trials.coffee.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vik.trials.coffee.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Клиент для работы с сетью.
 *
 * Позаимствовано с Хабра.
 * */
@Singleton
class NetworkClient @Inject constructor() {

    val provideRetrofit = buildRetrofit()

    inline fun <reified T> provideApiService(): T = provideRetrofit.create(T::class.java)

    internal fun buildRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}