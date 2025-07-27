package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.MenuItem
import ru.vik.trials.coffee.domain.entities.Resp

/** Репозиторий получения меню кофейни. */
interface GetMenuRepository {
    /**
     * Получает меню кофейни.
     *
     * @param shopId Идентификатор кофейни.
     * */
    fun getMenu(shopId: Int): Flow<Resp<List<MenuItem>>>
}