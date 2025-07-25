package ru.vik.trials.coffee.ui.shops

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.GetGeoDistanceUseCase
import ru.vik.trials.coffee.domain.GetGeoLocationUseCase
import ru.vik.trials.coffee.domain.GetLocationsUseCase
import ru.vik.trials.coffee.domain.entities.GeoPoint
import ru.vik.trials.coffee.domain.entities.Location
import ru.vik.trials.coffee.presentation.BaseViewModel
import javax.inject.Inject

/** ViewModel для работы со списком кофеен. */
@HiltViewModel
class ShopsViewModel @Inject constructor(
    /** Сценарий получения списка кофеен. */
    private val getLocationsUseCase: GetLocationsUseCase,
    /** Сценарий получения местоположения пользователя. */
    private val getGeoLocationUseCase: GetGeoLocationUseCase,
    /** Сценарий получения расстояния между точками. */
    private val getGeoDistanceUseCase: GetGeoDistanceUseCase
) : BaseViewModel() {

        companion object {
            private const val TAG = "ShopsViewModel"

            /** Код ошибки сервера: Пользователь не авторизован. */
            private const val ERROR_NEED_AUTH = 401
        }

    /** Список кофеен. */
    private var _shops = ArrayList<Location>()
    /** Список кофеен. */
    var shops = MutableStateFlow(_shops)

    /** Текущее местоположение пользователя. */
    private var _userLocation = MutableStateFlow<GeoPoint?>(null)
    /** Текущее местоположение пользователя. */
    val userLocation: StateFlow<GeoPoint?> = _userLocation

    /** Получает список кофеен. */
    fun refresh() {
        clear()
        // Запрос списка точек
        getLocationsUseCase().collectNetworkRequest(_uiState, ::mapErrorCodes) {
            if (it != null)
                add(it)
        }

        viewModelScope.launch(Dispatchers.IO) {
            getGeoLocationUseCase().collect {
                if (it.isSuccess) {
                    // Оповестим об изменении положении пользователя
                    _userLocation.emit(it.value)
                    // Для списка коффен приходится полностью обновлять список,
                    // чтобы отобразить расстояние до них, т.к. более лучшеего решения пока не нашел.
                    updateList()
                } else {
                    // Сообщить об ошибке?
                    Log.d(TAG, "geo location error: ${it.error}")
                }
            }
        }
    }

    /**
     * Возвращает расстояние от пользователя до точки.
     *
     * @param loc Интересуемая точка.
     * @return Расстояние в км. Если местоположении неизвестно, то null.
     * */
    fun getDistance(loc: Location): Double? {
        val curLoc = _userLocation.value ?: return null
        return getGeoDistanceUseCase(curLoc, loc.point)
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