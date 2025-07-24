package ru.vik.trials.coffee.domain.entities

/** Кофейня. */
class Location(
    /** Идентификатор. */
    val id: Int,
    /** Название. */
    val name: String,
    /** Координаты. */
    val point: Point
) {

    /** Координаты кофейни. */
    class Point(
        /** Широта. */
        val latitude: Double,
        /** Долгота. */
        val longitude: Double
    )
}