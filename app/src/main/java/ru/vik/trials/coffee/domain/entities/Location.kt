package ru.vik.trials.coffee.domain.entities

class Location(
    val id: Int,
    val name: String,
    val point: Point
) {

    class Point(
        val latitude: Double,
        val longitude: Double
    )
}