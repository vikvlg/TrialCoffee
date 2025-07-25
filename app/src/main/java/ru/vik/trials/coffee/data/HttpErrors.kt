package ru.vik.trials.coffee.data

/** Коды ошибок, возвращаемых при работе с сетью. */
object HttpErrors {
    /** Превышено время ожидания. */
    const val TIMEOUT = 408
    /** Неизвестная ошибка. */
    const val UNKNOWN = 418
}