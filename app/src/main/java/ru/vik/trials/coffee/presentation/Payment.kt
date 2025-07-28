package ru.vik.trials.coffee.presentation

/** Позиция в чеке. */
data class Payment(
    /** Идентифкатор товара ([ru.vik.trials.coffee.domain.entities.MenuItem]). */
    val id: Int,
    /** Название. */
    val name: String,
    /** Цена. */
    val price: Float,
    /** Количество. */
    var count: Int
)