package ru.vik.trials.coffee.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnSuccessListener
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.vik.trials.coffee.data.permissions.PermissionsHelper
import javax.inject.Inject
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/** Клиент для работы с геолокацией. */
class GpsClient {
    companion object {
        /** Код ошибки: у приложения нет разрешения на работу с геолокацией. */
        const val ERROR_PERMISSIONS_REQUIRED = 1

        private const val UPDATE_INTERVAL = 5000L
        private const val UPDATE_INTERVAL_MIN = 5000L
        private const val UPDATE_INTERVAL_MAX = 10000L

        /* Возвращает расстояние между двумя точками, км. */
        fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val theta = lon1 - lon2
            var dist = (sin(deg2rad(lat1)) * sin(deg2rad(lat2))
                    + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta)))
            dist = acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60 * 1.853159616
            return (dist)
        }

        private fun deg2rad(deg: Double): Double {
            return (deg * Math.PI / 180.0)
        }

        private fun rad2deg(rad: Double): Double {
            return (rad * 180.0 / Math.PI)
        }
    }

    private val context: Context
    private val locationClient: FusedLocationProviderClient
    private val locationRequest: LocationRequest

    @Inject constructor(@ApplicationContext context: Context) {
        this.context = context
        locationClient = LocationServices.getFusedLocationProviderClient(context)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,UPDATE_INTERVAL)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(UPDATE_INTERVAL_MIN)
            .setMaxUpdateDelayMillis(UPDATE_INTERVAL_MAX)
            .build()
    }

    /** Получает последнее известное местоположение пользователя. */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(callback: GeoLocationCallback) {
        if (!PermissionsHelper.hasAnyPermissions(context, listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))) {
            callback.onFail(ERROR_PERMISSIONS_REQUIRED)
            return
        }

        // Попробуем получить последние известные данные
        locationClient.lastLocation
            .addOnSuccessListener(OnSuccessListener { location ->
                if (location != null) {
                    // Местоположение получено
                    callback.onSuccess(location)
                } else {
                    // Местоположение неизвестно, запросим обновление данных
                    startLocationUpdates(callback)
                }
            })
    }

    /** Запускает обновление местоположения пользователя. */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(callback: GeoLocationCallback) {
        if (!PermissionsHelper.hasAnyPermissions(context, listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))) {
            callback.onFail(ERROR_PERMISSIONS_REQUIRED)
            return
        }

        locationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // Вернем первые полученные данные
                    callback.onSuccess(location)
                    // Завершим обновление данных
                    locationClient.removeLocationUpdates(this)
                    return
                }
            }
        }, null)
    }

    /** Оповещение о получении данных по местоположению пользователя. */
    interface GeoLocationCallback {
        /** Местоположение получено. */
        fun onSuccess(location: Location)

        /** Ошибка получения данных. */
        fun onFail(errorCode: Int)
    }
}