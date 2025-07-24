package ru.vik.trials.coffee.data.model

/** Bearer-токен сессии. */
data class BearerToken(
    /** Токен. */
    val token: String
) {
    /** Представление токена. */
    override fun toString(): String {
        return "Bearer $token"
    }
}