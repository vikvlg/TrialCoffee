package ru.vik.trials.coffee

import android.app.Application
import android.util.Log
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import java.util.Properties

@HiltAndroidApp
class CoffeeApplication
    : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO: Проверить наличие ключа.
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
    }
}