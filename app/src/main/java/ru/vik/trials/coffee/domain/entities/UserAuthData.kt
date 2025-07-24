package ru.vik.trials.coffee.domain.entities

/** Учетные данные пользователя. */
data class UserAuthData(
    /** Логин. */
    val login: String,
    /** Пароль. */
    val password: String)