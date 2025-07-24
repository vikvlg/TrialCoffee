package ru.vik.trials.coffee.domain

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.domain.entities.Resp
import ru.vik.trials.coffee.domain.entities.UserAuthData

interface SignUpRepository {
    fun signUp(authData: UserAuthData): Flow<Resp<Nothing>>
}