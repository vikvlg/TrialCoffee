package ru.vik.trials.coffee.domain

import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    private val repository: GetLocationsRepository
) {
    operator fun invoke() = repository.getLocations()
}