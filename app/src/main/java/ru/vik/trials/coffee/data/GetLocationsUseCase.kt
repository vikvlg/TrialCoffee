package ru.vik.trials.coffee.data

import ru.vik.trials.coffee.domain.GetLocationsRepository
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    private val repository: GetLocationsRepository
) {
    operator fun invoke() = repository.getLocations()
}