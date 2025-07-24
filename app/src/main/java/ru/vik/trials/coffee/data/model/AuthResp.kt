package ru.vik.trials.coffee.data.model

/** Ответ на успешную авторизацию/регистрацию. */
class AuthResp(
    /** Токен сессии. */
    val token: String,
    /** Время дейстивя токена. */
    val tokenLifetime: Int
)