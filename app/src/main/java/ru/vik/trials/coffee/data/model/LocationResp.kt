package ru.vik.trials.coffee.data.model

class LocationResp(
    val id: Int,
    val name: String,
    val point: Point
) {

    class Point(
        val latitude: Double,
        val longitude: Double
    )
}