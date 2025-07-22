package ru.vik.trials.coffee.data.model

data class Token(val token: String) {
    override fun toString(): String {
        return "Bearer $token"
    }
}