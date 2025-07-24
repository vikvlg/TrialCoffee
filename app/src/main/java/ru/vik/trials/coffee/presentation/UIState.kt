package ru.vik.trials.coffee.presentation

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Состояние процесса получения данных.
 *
 * Позаимствовано с Хабра.
 *
 * @param T Тип данных ожидаемого результата.
 */
sealed class UIState<T> {

    /** Стандартное состояние, когда нет выполняющегося запроса. */
    class Idle<T> : UIState<T>()

    /** Состояние после отправки запроса и ожидания получения ответа. */
    class Loading<T> : UIState<T>()

    /** Ошибка во время выполнения запроса, либо пришел ответ с ошибкой. */
    class Error<T>(val error: Int) : UIState<T>()

    /** Успешный запрос, ответ получен. */
    class Success<T>(val data: T?) : UIState<T>()
}

@Suppress("FunctionName")
fun <T> BaseViewModel.MutableUIStateFlow() = MutableStateFlow<UIState<T>>(UIState.Idle())
