package ru.vik.trials.coffee.domain.entities

class Resp<T> {
    var isSuccess: Boolean

    val value: T?

    val error: Int

    constructor(value: T?) {
        isSuccess = true
        this.value = value
        error = 0
    }

    constructor(error: Int) {
        isSuccess = false
        value = null
        this.error = error
    }
}