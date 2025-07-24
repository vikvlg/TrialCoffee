package ru.vik.trials.coffee.ui.shops

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.GetLocationsUseCase
import ru.vik.trials.coffee.domain.entities.Location
import ru.vik.trials.coffee.presentation.BaseViewModel
import javax.inject.Inject

/** ViewModel для работы со списком кофеен. */
@HiltViewModel
class ShopsViewModel @Inject constructor(
    /** Сценарий получения списка кофеен. */
    private val getLocationsUseCase: GetLocationsUseCase
) : BaseViewModel() {

        companion object {
            /** Код ошибки сервера: Пользователь не авторизован. */
            private const val ERROR_NEED_AUTH = 401
        }

    /** Список кофеен. */
    private var _shops = ArrayList<Location>()
    /** Список кофеен. */
    var shops = MutableStateFlow(_shops)

    /** Получает список кофеен. */
    fun refresh() {
        clear()
        // Запрос списка точек
        getLocationsUseCase().collectNetworkRequest(_uiState, ::mapErrorCodes) {
            if (it != null)
                add(it)
        }
    }

    fun getDistance(loc: Location): Int? {
        return null
    }

    /** Очищает список кофеен. */
    private fun clear() {
        _shops.clear()
        updateList()
    }

    /**
     * Добавляет новую кофейню.
     *
     * Вызывает перерисовку всего списка.
     * */
    private fun add(loc: Location) {
        _shops.add(loc)
        updateList()
    }

    /**
     * Обновляет список кофеен.
     *
     * Даже при изменении только 1 элемента списка, приходится обновлять весь список,
     * т.к. нормального решения не нашел.
     * */
    private fun updateList() {
        val newList = ArrayList<Location>()
        for (shop in _shops) {
            val newLoc = Location(shop.id, shop.name, shop.point)
            newList.add(newLoc)
        }

        shops.value = newList
    }

    override fun mapErrorCodes(code: Int): Int {
        return when (code) {
            ERROR_NEED_AUTH -> R.string.auth_need_auth
            else -> super.mapErrorCodes(code)
        }
    }
}