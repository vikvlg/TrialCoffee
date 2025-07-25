package ru.vik.trials.coffee.domain.entities

/** Кофейня. */
class Location(
    /** Идентификатор. */
    val id: Int,
    /** Название. */
    val name: String,
    /** Координаты. */
    val point: GeoPoint
)