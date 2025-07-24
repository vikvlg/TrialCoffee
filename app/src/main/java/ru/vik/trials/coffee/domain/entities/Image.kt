package ru.vik.trials.coffee.domain.entities

/** Изображение товара. */
class Image(
    /** Ссылка на Изображение. */
    val url: String,
    /** Данные изображения. */
    val bytes: ByteArray)