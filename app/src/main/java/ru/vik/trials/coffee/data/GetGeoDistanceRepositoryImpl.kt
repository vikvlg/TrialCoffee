package ru.vik.trials.coffee.data

import ru.vik.trials.coffee.data.location.GpsClient
import ru.vik.trials.coffee.domain.GetGeoDistanceRepository
import ru.vik.trials.coffee.domain.entities.GeoPoint
import javax.inject.Inject

/** Реализация репозитория получения расстояния между точками. */
class GetGeoDistanceRepositoryImpl @Inject constructor() : GetGeoDistanceRepository {

    override fun getDistance(a: GeoPoint, b: GeoPoint): Double {
        return GpsClient.getDistance(a.latitude, a.longitude, b.latitude, b.longitude)
    }
}
