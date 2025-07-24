package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.MenuItem
import ru.vik.trials.coffee.domain.entities.Resp

interface GetMenuRepository {
    fun getMenu(shopId: Int): Flow<Resp<MenuItem>>
}