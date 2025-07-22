package ru.vik.trials.coffee.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.vik.trials.coffee.domain.entities.Resp

abstract class BaseViewModel : ViewModel() {

    protected fun <T> Flow<Resp<T>>.collectNetworkRequest(
        state: MutableStateFlow<UIState<T>>,
        mapCodes: (Int) -> Int
    ) = collectResp(state, mapCodes) {
        UIState.Success(it)
    }

    protected fun <T, S> Flow<Resp<T>>.collectNetworkRequest(
        state: MutableStateFlow<UIState<S>>,
        mapCodes: (Int) -> Int,
        mapToUI: (T?) -> S
    ) = collectResp(state, mapCodes) {
        UIState.Success(mapToUI(it))
    }

    protected fun <T, S> Flow<Resp<T>>.collectResp(
        state: MutableStateFlow<UIState<S>>,
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