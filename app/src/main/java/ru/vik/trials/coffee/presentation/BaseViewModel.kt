package ru.vik.trials.coffee.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.entities.Resp

/** Базовая ViewModel для обеспечения работы экранов приложения. */
abstract class BaseViewModel : ViewModel() {

    /** Состояние выполнения запроса на получение данных. */
    protected val _uiState = MutableUIStateFlow<Unit>()
    /** Состояние выполнения запроса на получение данных. */
    val uiState = _uiState.asStateFlow()

    /** Сброс состояния [uiState] в стандартное состояние. */
    fun resetState() {
        _uiState.value = UIState.Idle()
    }

    /**
     * Преобразователь кодов ошибок с APIшного на человеческий.
     *
     * @param code Код ошибки, возвращаемый сервером.
     * @return Описание ошибки ([@StringRes]).
     * */
    open fun mapErrorCodes(code: Int): Int {
        return R.string.auth_error_unk
    }

    /** Магия подсмотренная в интернете. */
    protected fun <T> Flow<Resp<T>>.collectNetworkRequest(
        state: MutableStateFlow<UIState<T>>,
        /** Преобразователь кодов ошибок с APIшного на человеческий [@StringRes]. */
        mapCodes: (Int) -> Int
    ) = collectResp(state, mapCodes) {
        UIState.Success(it)
    }

    /** Магия подсмотренная в интернете. */
    protected fun <T, S> Flow<Resp<T>>.collectNetworkRequest(
        state: MutableStateFlow<UIState<S>>,
        /** Преобразователь кодов ошибок с APIшного на человеческий [@StringRes]. */
        mapCodes: (Int) -> Int,
        mapToUI: (T?) -> S
    ) = collectResp(state, mapCodes) {
        UIState.Success(mapToUI(it))
    }

    /** Магия подсмотренная в интернете. */
    protected fun <T, S> Flow<Resp<T>>.collectResp(
        state: MutableStateFlow<UIState<S>>,
        /** Преобразователь кодов ошибок с APIшного на человеческий [@StringRes]. */
        mapCodes: (Int) -> Int,
        successful: (T?) -> UIState.Success<S>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@collectResp.collect {
                if (it.isSuccess) {
                    state.value = successful(it.value)
                } else {
                    state.value = UIState.Error(mapCodes(it.error))
                }
            }
        }
    }
}