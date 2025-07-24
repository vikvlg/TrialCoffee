package ru.vik.trials.coffee.domain

import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val repository: GetImageRepository
) {
    operator fun invoke(url: String) = repository.getImage(url)
}