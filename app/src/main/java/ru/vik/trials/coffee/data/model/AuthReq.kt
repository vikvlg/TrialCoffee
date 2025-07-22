package ru.vik.trials.coffee.data.model

import com.google.gson.GsonBuilder

data class AuthReq(val login: String, val password: String) {
    override fun toString(): String {
        return GsonBuilder().create().toJson(this)
    }
}