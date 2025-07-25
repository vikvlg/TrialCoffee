package ru.vik.trials.coffee.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.vik.trials.coffee.domain.GetImageRepository
import ru.vik.trials.coffee.domain.entities.Image
import ru.vik.trials.coffee.domain.entities.Resp
import java.net.SocketTimeoutException
import javax.inject.Inject

/** Реализация репозитория загрузки изображений. */
class GetImageRepositoryImpl @Inject constructor(
    private val service: CoffeeApi
) : GetImageRepository {

    override fun getImage(url: String): Flow<Resp<Image>> = flow {
        try {
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
        catch (_: SocketTimeoutException) {
            emit(Resp(HttpErrors.TIMEOUT))
        }
        catch (_: Exception) {
            emit(Resp(HttpErrors.UNKNOWN))
        }
    }
}
