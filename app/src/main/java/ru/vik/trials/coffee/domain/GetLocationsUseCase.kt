package ru.vik.trials.coffee.domain

import javax.inject.Inject

/** Сценарий для получения списка кофеен. */
class GetLocationsUseCase @Inject constructor(
    private val repository: GetLocationsRepository
) {
    operator fun invoke() = repository.getLocations()
}