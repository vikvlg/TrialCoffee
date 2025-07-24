package ru.vik.trials.coffee.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.vik.trials.coffee.data.model.AuthReq
import ru.vik.trials.coffee.data.preferences.UserDataPreferences
import ru.vik.trials.coffee.domain.SignUpRepository
import ru.vik.trials.coffee.domain.entities.Resp
import ru.vik.trials.coffee.domain.entities.UserAuthData
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val service: CoffeeApi,
    private val userDataPreferences: UserDataPreferences
) : SignUpRepository {

    override fun signUp(authData: UserAuthData): Flow<Resp<Nothing>> = flow {
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
