package ru.vik.trials.coffee.domain

import ru.vik.trials.coffee.domain.entities.GeoPoint
import javax.inject.Inject

/** Сценарий для получения расстония между точками. */
class GetGeoDistanceUseCase @Inject constructor(
    private val repository: GetGeoDistanceRepository
) {
    operator fun invoke(a: GeoPoint, b: GeoPoint) = repository.getDistance(a, b)
}