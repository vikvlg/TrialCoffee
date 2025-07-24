package ru.vik.trials.coffee.ui.menu

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.GetImageUseCase
import ru.vik.trials.coffee.domain.GetMenuUseCase
import ru.vik.trials.coffee.domain.entities.Image
import ru.vik.trials.coffee.domain.entities.MenuItem
import ru.vik.trials.coffee.presentation.BaseViewModel
import ru.vik.trials.coffee.presentation.MutableUIStateFlow
import ru.vik.trials.coffee.presentation.Payment
import ru.vik.trials.coffee.presentation.UIState
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMenuUseCase: GetMenuUseCase,
    private val getImageUseCase: GetImageUseCase
): BaseViewModel() {

    companion object {
        private const val ERROR_NEED_AUTH = 401
    }

    private val _uiState = MutableUIStateFlow<Unit>()
    val uiState = _uiState.asStateFlow()
    private val _uiImageState = MutableUIStateFlow<Unit>()

    private val _menu = ArrayList<MenuItem>()
    var menu = MutableStateFlow(_menu)

    private val images = ArrayList<Image>()

    /** Количество выбранных позиций <идентификатор, количество>. */
    private val counts: MutableMap<Int, Int> = mutableMapOf()

//    init {
//        _menu.apply {
//            add(MenuItem(1, "Эспрессо", "", 200.0f))
//            add(MenuItem(2, "Капучино", "", 300.0f))
//            add(MenuItem(3, "Горячий шоколад", "", 250.0f))
//            add(MenuItem(4, "Латте", "", 300.0f))
//            add(MenuItem(5, "Американо", "", 200.0f))
//            add(MenuItem(6, "Вода со льдом", "", 100.0f))
//            add(MenuItem(7, "Чай черный", "", 200.0f))
//        }
//    }
//    MenuItem(1, "Кофе 1", "https://tea.ru/upload/blog/0821/3108/coffee/01.jpg", 100.0f)
//    MenuItem(2, "Кофе 2", "https://www.cre.ru/content/upload/news/15528982809641.jpeg", 150.0f)
//    MenuItem(3, "Кофе 3", "https://ichef.bbci.co.uk/news/640/cpsprodpb/B079/production/_117677154_gettyimages-156586025.jpg", 240.0f)

    fun refresh(shopId: Int) {
        clear()
        // Запрос меню кофейни
        getMenuUseCase(shopId).collectNetworkRequest(_uiState, ::mapErrorCodes) {
            if (it == null)
                return@collectNetworkRequest
            Log.d("TAG", "MenuItem(${it.id}, \"${it.name}\", \"${it.imageURL}\", ${it.price}f)")
            add(it)

            // Запрос картинки
            getImageUseCase(it.imageURL).collectNetworkRequest(_uiImageState,::mapImageErrorCodes) { image ->
                if (image == null)
                    return@collectNetworkRequest

                images.add(image)
                Log.d("TAG", "image[${image.bytes.size}]: ${image.url}")
                updateList()
            }
        }
    }

    fun resetState() {
        _uiState.value = UIState.Idle()
    }

    fun getImage(context: Context, url: String): ImageBitmap {
        val image = images.find { it.url == url }
            ?: return ImageBitmap.imageResource(context.resources, R.drawable.coffee_unk_128)

        val bitmap = BitmapFactory.decodeByteArray(image.bytes, 0, image.bytes.size)
        return bitmap.asImageBitmap()
    }

    fun getItemCount(item: MenuItem): Int {
        return counts[item.id] ?: 0
    }

    fun setItemCount(item: MenuItem, value: Int) {
        counts[item.id] = value
    }

    fun getPayment(): String {
        val list = ArrayList<Payment>()
        for (item in _menu) {
            val count = getItemCount(item)
            if (count == 0)
                continue

            list.add(Payment(item.id, item.name, item.price, count))
        }
        return Uri.encode(Gson().toJson(list))
    }

    private fun clear() {
        _menu.clear()
        updateList()
    }

    private fun add(item: MenuItem) {
        _menu.add(item)
        updateList()
    }

    /**
     * Обновляет список меню.
     *
     * Даже при изменении только 1 элемента списка, приходится обновлять весь список.
     * TODO: Найти решение для обновления конкретных элементов.
     */
    private fun updateList() {
        val newList = ArrayList<MenuItem>()
        for (item in _menu) {
            val newItem = MenuItem(item.id, item.name, item.imageURL, item.price)
            newList.add(newItem)
        }

        menu.value = newList
    }

    private fun mapErrorCodes(code: Int): Int {
        return when (code) {
            ERROR_NEED_AUTH -> R.string.auth_need_auth
            else -> R.string.auth_error_unk
        }
    }

    private fun mapImageErrorCodes(code: Int): Int {
        return R.string.auth_error_unk
    }
}