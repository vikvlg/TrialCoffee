package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.Image
import ru.vik.trials.coffee.domain.entities.Resp

interface GetImageRepository {
    fun getImage(url: String): Flow<Resp<Image>>
}