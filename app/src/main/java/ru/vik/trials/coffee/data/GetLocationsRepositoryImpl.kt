package ru.vik.trials.coffee.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.vik.trials.coffee.data.model.BearerToken
import ru.vik.trials.coffee.data.preferences.UserDataPreferences
import ru.vik.trials.coffee.domain.GetLocationsRepository
import ru.vik.trials.coffee.domain.entities.Location
import ru.vik.trials.coffee.domain.entities.Resp
import javax.inject.Inject

/** Реализация репозитория получения списка кофеен. */
class GetLocationsRepositoryImpl @Inject constructor(
    private val service: CoffeeApi,
    private val pref: UserDataPreferences
) : GetLocationsRepository {

    override fun getLocations(): Flow<Resp<Location>> = flow {
        val res = service.getLocations(BearerToken(pref.token))
        if (res.isSuccessful) {
            res.body()?.let {
                for (loc in it) {
                    emit(Resp(Location(loc.id, loc.name, Location.Point(loc.point.latitude, loc.point.longitude))))
                }
            } ?: run {
                // "Неизвестная ошибка"
                emit(Resp(400))
            }
        } else {
            emit(Resp(res.code()))
        }
    }
}