package ru.vik.trials.coffee.domain

import ru.vik.trials.coffee.domain.entities.UserAuthData
import javax.inject.Inject

/** Сценарий для регистрации на сервере. */
class SignUpUseCase @Inject constructor(
    private val repository: SignUpRepository
) {
    operator fun invoke(data: UserAuthData) = repository.signUp(data)
}