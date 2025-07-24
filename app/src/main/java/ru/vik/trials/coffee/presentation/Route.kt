package ru.vik.trials.coffee.presentation

class Route(val route: String) {

    companion object {
        val SignIn = Route("signin")
        val SignUp = Route("signup")
        val Shops = Route("shops")
        val Map = Route("map")
        val Menu = Route("menu/{$ARG_MENU_ID}")
        val Payment = Route("payment/{$ARG_PAYMENT_DATA}")

        const val ARG_MENU_ID = "id"
        const val ARG_PAYMENT_DATA = "data"
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