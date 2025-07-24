package ru.vik.trials.coffee.domain.entities

/** Позиция в меню. */
class MenuItem(
    /** Идентифкатор. */
    val id: Int,
    /** Название. */
    val name: String,
    /** Ссылка на изображение. */
    val imageURL: String,
    /** Цена. */
    val price: Float
)