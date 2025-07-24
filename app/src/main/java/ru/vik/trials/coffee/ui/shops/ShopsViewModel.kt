package ru.vik.trials.coffee.ui.shops

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.GetLocationsUseCase
import ru.vik.trials.coffee.domain.entities.Location
import ru.vik.trials.coffee.presentation.BaseViewModel
import ru.vik.trials.coffee.presentation.MutableUIStateFlow
import ru.vik.trials.coffee.presentation.UIState
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase
) : BaseViewModel() {

        companion object {
            private var idCounter: Int = 10
            private const val ERROR_NEED_AUTH = 401
        }

    private val _uiState = MutableUIStateFlow<Unit>()
    val uiState = _uiState.asStateFlow()

    private var _shops = ArrayList<Location>()
    var shops = MutableStateFlow(_shops)

    fun refresh() {
        clear()
        // Запрос списка точек
        getLocationsUseCase().collectNetworkRequest(_uiState, ::mapErrorCodes) {
            if (it != null) {
                add(it)
            }
            //Log.d("TAG", "getLocationsUseCase: $it")
        }
    }

    fun getDistance(loc: Location): Int? {
        return null
    }

    fun resetState() {
        _uiState.value = UIState.Idle()
    }

    private fun clear() {
        _shops.clear()
        updateList()
    }

    private fun add(loc: Location) {
        _shops.add(loc)
        updateList()
    }

    private fun updateList() {
        val newList = ArrayList<Location>()
        for (shop in _shops) {
            val newLoc = Location(shop.id, shop.name, shop.point)
            newList.add(newLoc)
        }

        shops.value = newList
    }

    private fun mapErrorCodes(code: Int): Int {
        return when (code) {
            ERROR_NEED_AUTH -> R.string.auth_need_auth
            else -> R.string.auth_error_unk
        }
    }
}