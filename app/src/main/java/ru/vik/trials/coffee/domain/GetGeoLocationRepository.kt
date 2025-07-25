package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.GeoPoint
import ru.vik.trials.coffee.domain.entities.Resp

/** Репозиторий получения текущего местоположения пользователя. */
interface GetGeoLocationRepository {
    /** Возвращает текущее местоположение пользователя. */
    fun getCurrentLocation(): Flow<Resp<GeoPoint>>
}