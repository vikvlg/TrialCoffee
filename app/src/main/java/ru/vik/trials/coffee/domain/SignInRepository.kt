package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.Resp
import ru.vik.trials.coffee.domain.entities.UserAuthData

/** Репозиторий авторизации на сервере. */
interface SignInRepository {
    /**
     * Авторизуется на сервере.
     *
     * @param authData Учетные данные.
     * */
    fun signIn(authData: UserAuthData): Flow<Resp<Nothing>>
}