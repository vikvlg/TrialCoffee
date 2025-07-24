package ru.vik.trials.coffee.data.model

/** Ответ с товарами в кофейне. */
class MenuResp(
    /** Идентифкатор. */
    val id: Int,
    /** Название. */
    val name: String,
    /** Ссылка на картинку товара. */
    val imageURL: String,
    /** Цена. */
    val price: Float
)