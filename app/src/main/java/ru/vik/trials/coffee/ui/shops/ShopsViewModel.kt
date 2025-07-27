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
    var shops = MutableStateFlow(ArrayList<Location>().toList())

    /** Текущее местоположение пользователя. */
    private var _userLocation = MutableStateFlow<GeoPoint?>(null)
    /** Текущее местоположение пользователя. */
    val userLocation: StateFlow<GeoPoint?> = _userLocation

    /** Получает список кофеен. */
    fun refresh() {
        // Запрос списка точек
        getLocationsUseCase().collectNetworkRequest(_uiState, ::mapErrorCodes) {
            shops.value = it ?: ArrayList()
        }

        viewModelScope.launch(Dispatchers.IO) {
            getGeoLocationUseCase().collect {
                if (it.isSuccess) {
                    // Оповестим об изменении положении пользователя
                    _userLocation.emit(it.value)
                    Log.d(TAG, "geo location: ${it.value?.latitude}; ${it.value?.longitude}")
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
     * @param userLoc Кордината пользователя. Берется из [userLocation], нужна передача сюда, чтобы корректно обновлялся интерфейс.
     * @param loc Интересуемая точка.
     * @return Расстояние в км. Если местоположении неизвестно, то null.
     * */
    fun getDistance(userLoc: GeoPoint?, loc: Location): Double? {
        if (userLoc == null)
            return null
        return getGeoDistanceUseCase(userLoc, loc.point)
    }

    override fun mapErrorCodes(code: Int): Int {
        return when (code) {
            ERROR_NEED_AUTH -> R.string.auth_need_auth
            else -> super.mapErrorCodes(code)
        }
    }
}