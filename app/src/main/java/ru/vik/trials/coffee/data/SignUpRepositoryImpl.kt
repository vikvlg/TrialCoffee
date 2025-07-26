package ru.vik.trials.coffee.data

import kotlinx.coroutines.flow.Flow
import ru.vik.trials.coffee.data.model.AuthReq
import ru.vik.trials.coffee.data.preferences.UserDataPreferences
import ru.vik.trials.coffee.domain.SignUpRepository
import ru.vik.trials.coffee.domain.entities.Resp
import ru.vik.trials.coffee.domain.entities.UserAuthData
import javax.inject.Inject

/** Реализация репозитория регистрации на сервере. */
class SignUpRepositoryImpl @Inject constructor(
    private val service: CoffeeApi,
    private val userDataPreferences: UserDataPreferences
) : SignUpRepository {

    override fun signUp(authData: UserAuthData): Flow<Resp<Nothing>> = flowResp {
        val res = service.signUp(AuthReq(authData.login, authData.password))

        if (res.isSuccessful) {
            val token = res.body()?.token ?: ""
            userDataPreferences.token = token
            emit(Resp(null))
        } else {
            emit(Resp(res.code()))
        }
    }
}
