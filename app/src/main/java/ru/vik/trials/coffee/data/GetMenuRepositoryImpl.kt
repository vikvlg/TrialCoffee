package ru.vik.trials.coffee.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.vik.trials.coffee.data.model.Token
import ru.vik.trials.coffee.data.preferences.UserDataPreferences
import ru.vik.trials.coffee.domain.GetMenuRepository
import ru.vik.trials.coffee.domain.entities.MenuItem
import ru.vik.trials.coffee.domain.entities.Resp
import javax.inject.Inject

class GetMenuRepositoryImpl @Inject constructor(
    private val service: CoffeeApi,
    private val pref: UserDataPreferences
) : GetMenuRepository {

    override fun getMenu(shopId: Int): Flow<Resp<MenuItem>> = flow {
        val res = service.getMenu(Token(pref.token), shopId)
        if (res.isSuccessful) {
            res.body()?.let {
                for (menu in it) {
                    emit(Resp(MenuItem(menu.id, menu.name, menu.imageURL, menu.price)))
                }
            } ?: run {
                emit(Resp(400))
            }
        } else {
            Log.d("GetMenuRepositoryImpl", "error: ${res.code()}")
            emit(Resp(res.code()))
        }
    }
}