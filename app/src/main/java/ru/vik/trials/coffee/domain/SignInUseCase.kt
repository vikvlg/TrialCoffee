package ru.vik.trials.coffee.domain

import ru.vik.trials.coffee.domain.entities.UserAuthData
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: SignInRepository
) {
    operator fun invoke(data: UserAuthData) = repository.signIn(data)
}