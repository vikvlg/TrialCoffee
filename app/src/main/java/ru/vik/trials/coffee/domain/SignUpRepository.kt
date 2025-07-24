package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.Resp
import ru.vik.trials.coffee.domain.entities.UserAuthData

/** Репозиторий регистрации на сервере. */
interface SignUpRepository {
    /**
     * Создает нового пользователя на сервере.
     *
     * @param authData Учетные данные.
     * */
    fun signUp(authData: UserAuthData): Flow<Resp<Nothing>>
}