package ru.vik.trials.coffee.data

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.data.model.BearerToken
import ru.vik.trials.coffee.data.preferences.UserDataPreferences
import ru.vik.trials.coffee.domain.GetMenuRepository
import ru.vik.trials.coffee.domain.entities.MenuItem
import ru.vik.trials.coffee.domain.entities.Resp
import javax.inject.Inject

/** Реализация репозитория получения меню кофейни. */
class GetMenuRepositoryImpl @Inject constructor(
    private val service: CoffeeApi,
    private val pref: UserDataPreferences
) : GetMenuRepository {

    override fun getMenu(shopId: Int): Flow<Resp<MenuItem>> = flowResp {
        val res = service.getMenu(BearerToken(pref.token), shopId)
        if (res.isSuccessful) {
            res.body()?.let {
                for (menu in it) {
                    emit(Resp(MenuItem(menu.id, menu.name, menu.imageURL, menu.price)))
                }
            } ?: run {
                throw Exception()
            }
        } else {
            emit(Resp(res.code()))
        }
    }
}