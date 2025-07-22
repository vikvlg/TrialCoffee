package ru.vik.trials.coffee.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vik.trials.coffee.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkClient @Inject constructor() {

    val provideRetrofit = buildRetrofit()

    inline fun <reified T> provideApiService(): T = provideRetrofit.create(T::class.java)

    /**
     * Builds and configures Retrofit instance.
     *
     * @see Retrofit
     */
    internal fun buildRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    class AuthenticatorClient @Inject constructor() {
//
//        private val provideRetrofit = buildRetrofit(
//            createOkHttpClientBuilder().build()
//        )
//
//        fun provideAuthenticatorApiService(): AuthenticatorApiService = provideRetrofit.create(
//            AuthenticatorApiService::class.java
//        )
//    }
}