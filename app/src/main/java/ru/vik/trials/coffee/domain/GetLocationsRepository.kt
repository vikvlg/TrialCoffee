package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.Location
import ru.vik.trials.coffee.domain.entities.Resp

/** Репозиторий получения списка кофеен. */
interface  GetLocationsRepository {
    /** Получает список кофеен. */
    fun getLocations(): Flow<Resp<Location>>
}