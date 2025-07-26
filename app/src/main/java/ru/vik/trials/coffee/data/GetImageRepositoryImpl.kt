package ru.vik.trials.coffee.data

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.GetImageRepository
import ru.vik.trials.coffee.domain.entities.Image
import ru.vik.trials.coffee.domain.entities.Resp
import javax.inject.Inject

/** Реализация репозитория загрузки изображений. */
class GetImageRepositoryImpl @Inject constructor(
    private val service: CoffeeApi
) : GetImageRepository {

    override fun getImage(url: String): Flow<Resp<Image>> = flowResp {
        val res = service.downloadImage(url)
        if (res.isSuccessful) {
            res.body()?.let {
                emit(Resp(Image(url, it.bytes())))
            } ?: run {
                throw Exception()
            }
        } else {
            emit(Resp(res.code()))
        }
    }
}
