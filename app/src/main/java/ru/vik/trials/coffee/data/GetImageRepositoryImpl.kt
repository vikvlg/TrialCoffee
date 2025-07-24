package ru.vik.trials.coffee.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.vik.trials.coffee.domain.GetImageRepository
import ru.vik.trials.coffee.domain.entities.Image
import ru.vik.trials.coffee.domain.entities.Resp
import javax.inject.Inject

/** Реализация репозитория загрузки изображений. */
class GetImageRepositoryImpl @Inject constructor(
    private val service: CoffeeApi
) : GetImageRepository {

    override fun getImage(url: String): Flow<Resp<Image>> = flow {
        val res = service.downloadImage(url)
        if (res.isSuccessful) {
            res.body()?.let {
                emit(Resp(Image(url,it.bytes())))
            } ?: run {
                // "Неизвестная ошибка"
                emit(Resp(400))
            }
        } else {
            emit(Resp(res.code()))
        }
    }
}