package ru.vik.trials.coffee.data.model

/** Ответ на успешную атворизацию/регистрацию. */
class AuthResp(
    /** Токен сессии. */
    val token: String,
    /** Время дейстивя токена. */
    val tokenLifetime: Int
)