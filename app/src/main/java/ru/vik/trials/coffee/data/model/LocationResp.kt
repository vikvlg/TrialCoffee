package ru.vik.trials.coffee.data.model

/** Ответ с данными по кофейне. */
class LocationResp(
    /** Идентификатор. */
    val id: Int,
    /** Название. */
    val name: String,
    /** Координаты. */
    val point: Point
) {

    class Point(
        /** Широта. */
        val latitude: Double,
        /** Долгота */
        val longitude: Double
    )
}