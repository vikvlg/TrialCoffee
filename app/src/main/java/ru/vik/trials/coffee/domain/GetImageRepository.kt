package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.Image
import ru.vik.trials.coffee.domain.entities.Resp

/** Репозиторий загрузки изображений. */
interface GetImageRepository {
    /**
     * Загружает изображение.
     *
     * @param url Ссылка на изображение.
     * */
    fun getImage(url: String): Flow<Resp<Image>>
}