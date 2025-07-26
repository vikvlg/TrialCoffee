package ru.vik.trials.coffee.data

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.data.model.BearerToken
import ru.vik.trials.coffee.data.preferences.UserDataPreferences
import ru.vik.trials.coffee.domain.GetLocationsRepository
import ru.vik.trials.coffee.domain.entities.GeoPoint
import ru.vik.trials.coffee.domain.entities.Location
import ru.vik.trials.coffee.domain.entities.Resp
import javax.inject.Inject

/** Реализация репозитория получения списка кофеен. */
class GetLocationsRepositoryImpl @Inject constructor(
    private val service: CoffeeApi,
    private val pref: UserDataPreferences
) : GetLocationsRepository {

    override fun getLocations(): Flow<Resp<Location>> = flowResp {
        val res = service.getLocations(BearerToken(pref.token))
        if (res.isSuccessful) {
            res.body()?.let {
                for (loc in it) {
                    emit(Resp(Location(loc.id, loc.name, GeoPoint(loc.point.latitude, loc.point.longitude))))
                }
            } ?: run {
                throw Exception()
            }
        } else {
            emit(Resp(res.code()))
        }
    }
}