package ru.vik.trials.coffee.data

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.vik.trials.coffee.data.location.GpsClient
import ru.vik.trials.coffee.domain.GetGeoLocationRepository
import ru.vik.trials.coffee.domain.entities.GeoPoint
import ru.vik.trials.coffee.domain.entities.Resp
import javax.inject.Inject

/** Реализация репозитория получения текущего местоположения пользователя. */
class GetGeoLocationRepositoryImpl @Inject constructor(
    private val client: GpsClient
) : GetGeoLocationRepository {

    override fun getCurrentLocation(): Flow<Resp<GeoPoint>> = flow {
        // Сделаем запрос на получение местоположения
        var currentLoc: Location? = null
        var error: Int? = null
        client.getCurrentLocation(object : GpsClient.GeoLocationCallback {
            override fun onSuccess(location: Location) {
                currentLoc = location
            }

            override fun onFail(errorCode: Int) {
                error = errorCode
            }
        })

        // Будем ждать пока данные не придут
        while (true) {
            Thread.sleep(100)
            if (currentLoc != null) {
                emit(Resp(GeoPoint(currentLoc.latitude, currentLoc.longitude)))
                break
            }
            if (error != null) {
                emit(Resp(error))
                break
            }
        }
    }
}