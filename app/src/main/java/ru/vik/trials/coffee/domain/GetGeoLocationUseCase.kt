package ru.vik.trials.coffee.domain

import javax.inject.Inject

/** Сценарий для получения текущего местоположения пользователя. */
class GetGeoLocationUseCase @Inject constructor(
    private val repository: GetGeoLocationRepository
) {
    operator fun invoke() = repository.getCurrentLocation()
}