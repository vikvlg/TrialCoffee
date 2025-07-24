package ru.vik.trials.coffee.domain

import javax.inject.Inject

/** Сценарий для получения меню кофейни. */
class GetMenuUseCase @Inject constructor(
    private val repository: GetMenuRepository
) {
    operator fun invoke(shopId: Int) = repository.getMenu(shopId)
}