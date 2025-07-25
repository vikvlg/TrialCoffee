package ru.vik.trials.coffee.domain

import ru.vik.trials.coffee.domain.entities.GeoPoint

/** Репозиторий получения расстояния между точками. */
interface GetGeoDistanceRepository {
    /** Возвращает расстояние между точками, км. */
    fun getDistance(a: GeoPoint, b: GeoPoint): Double
}