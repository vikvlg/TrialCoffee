package ru.vik.trials.coffee.data.model

import com.google.gson.GsonBuilder

/** Запрос на авторизацию/регистрацию. */
data class AuthReq(
    /** Логин пользователя. */
    val login: String,
    /** Пароль пользователя. */
    val password: String) {

    override fun toString(): String {
        return GsonBuilder().create().toJson(this)
    }
}