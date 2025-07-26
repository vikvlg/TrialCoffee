package ru.vik.trials.coffee

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoffeeApplication
    : Application() {

    override fun onCreate() {
        super.onCreate()

        // Ключ для ЯндексКарты
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
    }
}