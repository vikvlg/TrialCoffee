package ru.vik.trials.coffee.domain.entities

/** Возврат запрашиваемых данных. */
class Resp<T> {
    /** Флаг успешного запроса. */
    var isSuccess: Boolean

    /**
     * Полученные данные по запросу.
     *
     * Если запрос неудачный ([isSuccess]), то Nothing.
     * */
    val value: T?

    /**
     * Код ошибки.
     *
     * Если запрос удачный ([isSuccess]), то 0.
     * */
    val error: Int

    /**
     * Конструктор для успешного ответа.
     *
     * @param value Полученные данные по запросу.
     * */
    constructor(value: T?) {
        isSuccess = true
        this.value = value
        error = 0
    }

    /**
     * Конструктор для неуспешного ответа.
     *
     * @param error Код ошибки.
     * */
    constructor(error: Int) {
        isSuccess = false
        value = null
        this.error = error
    }
}