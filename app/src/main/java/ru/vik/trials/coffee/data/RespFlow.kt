package ru.vik.trials.coffee.data

import android.util.Log
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.FlowCollector
import ru.vik.trials.coffee.domain.entities.Resp
import java.net.SocketTimeoutException

/**
 * Обертка flow для запросов в сеть.
 *
 * Отлавливает исключения и корректно выбрасывает ([FlowCollector.emit]) их в поток с кодом ошибки.
 * */
fun <T> flowResp(block: suspend FlowCollector<Resp<T>>.() -> Unit): AbstractFlow<Resp<T>> = RespFlow(block)

private class RespFlow<T>(private val block: suspend FlowCollector<Resp<T>>.() -> Unit) : AbstractFlow<Resp<T>>() {
    companion object {
        private const val TAG = "RespFlow"
    }

    override suspend fun collectSafely(collector: FlowCollector<Resp<T>>) {
        collector.safeBlock()
    }
    suspend fun FlowCollector<Resp<T>>. safeBlock() {
        try {
            block()
        }
        catch (_: SocketTimeoutException) {
            Log.d(TAG, "exception: timeout")
            emit(Resp(HttpErrors.TIMEOUT))
        }
        catch (_: Exception) {
            Log.d(TAG, "exception: unknow")
            emit(Resp(HttpErrors.UNKNOWN))
        }
    }
}
