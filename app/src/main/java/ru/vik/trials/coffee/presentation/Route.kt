package ru.vik.trials.coffee.presentation

import android.util.Log

class Route(val route: String) {

    companion object {
        val SignIn = Route("signin")
        val SignUp = Route("signup")
        val Shops = Route("shops")
        val Map = Route("map")
        val Menu = Route("menu/{id}")
    }

    operator fun invoke(): String {
        return route
    }

    operator fun invoke(argName: String, argValue: Any): String {
        return route.replace("{$argName}", "$argValue")
    }

    operator fun invoke(arg: Pair<String, Any>): String {
        return route.replace("{${arg.first}}", "${arg.second}")
    }

    operator fun invoke(args: List<Pair<String, Any>>): String {
        var result = route
        for (arg in args) {
            result = result.replace("{${arg.first}}}", "${arg.second}")
        }
        return result
    }
}