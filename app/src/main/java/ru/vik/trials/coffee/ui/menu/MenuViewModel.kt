package ru.vik.trials.coffee.ui.menu

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.GetImageUseCase
import ru.vik.trials.coffee.domain.GetMenuUseCase
import ru.vik.trials.coffee.domain.entities.Image
import ru.vik.trials.coffee.domain.entities.MenuItem
import ru.vik.trials.coffee.presentation.BaseViewModel
import ru.vik.trials.coffee.presentation.MutableUIStateFlow
import ru.vik.trials.coffee.presentation.Payment
import javax.inject.Inject

/** ViewModel для работы с меню кофейни. */
@HiltViewModel
class MenuViewModel @Inject constructor(
    /** Сценарий получения меню. */
    private val getMenuUseCase: GetMenuUseCase,
    /** Сценарий загрузки изображений. */
    private val getImageUseCase: GetImageUseCase
): BaseViewModel() {

    companion object {
        /** Код ошибки сервера: Пользователь не авторизован. */
        private const val ERROR_NEED_AUTH = 401
    }

    /** Состояние загрузки изображения. */
    private val _uiImageState = MutableUIStateFlow<Unit>()

    /** Меню кофейни. */
    private val _menu = ArrayList<MenuItem>()
    /** Меню кофейни. */
    var menu = MutableStateFlow(_menu)

    /** Список загруженных изображений. */
    private val images = ArrayList<Image>()

    /** Количество выбранных позиций в меню <идентификатор, количество>. */
    private val counts: MutableMap<Int, Int> = mutableMapOf()

    /** Запрашивает меню кофейни. */
    fun refresh(shopId: Int) {
        clear()
        // Запрос меню кофейни.
        getMenuUseCase(shopId).collectNetworkRequest(_uiState, ::mapErrorCodes) {
            if (it == null)
                return@collectNetworkRequest
            add(it)

            // Запрос картинки
            getImageUseCase(it.imageURL).collectNetworkRequest(_uiImageState,::mapImageErrorCodes) { image ->
                if (image == null)
                    return@collectNetworkRequest

                images.add(image)
                updateList()
            }
        }
    }

    /** Возвращает загруженное изображение товара. */
    fun getImage(context: Context, url: String): ImageBitmap {
        val image = images.find { it.url == url }
            ?: return ImageBitmap.imageResource(context.resources, R.drawable.coffee_unk_128)

        val bitmap = BitmapFactory.decodeByteArray(image.bytes, 0, image.bytes.size)
        return bitmap.asImageBitmap()
    }

    /**
     * Возвращает сколько пользователь выбрал товара.
     *
     * @param item Товар.
     * @return Количество товара.
     * */
    fun getItemCount(item: MenuItem): Int {
        return counts[item.id] ?: 0
    }

    /**
     * Устанавливает сколько пользователь выбрал товара.
     *
     * @param item Товар.
     * @param value Количество товара.
     * */
    fun setItemCount(item: MenuItem, value: Int) {
        counts[item.id] = value
    }

    /** Возвращает сериализованные данные заказа.
     *
     * @return Список выбранных товаров в json-формате.
     * */
    fun getSerializedPayment(): String {
        val list = ArrayList<Payment>()
        for (item in _menu) {
            val count = getItemCount(item)
            if (count == 0)
                continue

            list.add(Payment(item.id, item.name, item.price, count))
        }
        return Uri.encode(Gson().toJson(list))
    }

    /** Очищает меню. */
    private fun clear() {
        _menu.clear()
        updateList()
    }

    /**
     * Добавляет товар к меню.
     *
     * Вызывает полное обновление меню.
     * */
    private fun add(item: MenuItem) {
        _menu.add(item)
        updateList()
    }

    /**
     * Обновляет список меню.
     *
     * Даже при изменении только 1 элемента списка, приходится обновлять весь список,
     * т.к. нормального решения не нашел.
     */
    private fun updateList() {
        val newList = ArrayList<MenuItem>()
        for (item in _menu) {
            val newItem = MenuItem(item.id, item.name, item.imageURL, item.price)
            newList.add(newItem)
        }

        menu.value = newList
    }

    override fun mapErrorCodes(code: Int): Int {
        return when (code) {
            ERROR_NEED_AUTH -> R.string.auth_need_auth
            else -> super.mapErrorCodes(code)
        }
    }

    private fun mapImageErrorCodes(code: Int): Int {
        return super.mapErrorCodes(code)
    }
}