package ru.vik.trials.coffee.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.vik.trials.coffee.data.model.AuthReq
import ru.vik.trials.coffee.data.preferences.UserDataPreferences
import ru.vik.trials.coffee.domain.SignInRepository
import ru.vik.trials.coffee.domain.entities.Resp
import ru.vik.trials.coffee.domain.entities.UserAuthData
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val service: CoffeeApi,
    private val userDataPreferences: UserDataPreferences
) : SignInRepository {

    override fun signIn(authData: UserAuthData): Flow<Resp<Nothing>> = flow {
        Log.d("AuthRepositoryImpl", "[${Thread.currentThread().name}] signIn...")
        val res = service.signIn2(AuthReq(authData.login, authData.password))
        Log.d("AuthRepositoryImpl", "[${Thread.currentThread().name}] signIn.OK, isSuccessful: ${res.isSuccessful}")

        if (res.isSuccessful) {
            val token = res.body()?.token ?: ""
            userDataPreferences.token = token
            //emit(Either.Left(AuthToken(token)))
            emit(Resp(null))
        } else {
            Log.d("AuthRepositoryImpl", "error: ${res.code()}")
            //emit(Either.Right(res.code()))
            emit(Resp(res.code()))
        }
    }
}
