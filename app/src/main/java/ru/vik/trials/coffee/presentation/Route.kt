package ru.vik.trials.coffee.presentation

/** Навигация между экранами. */
class Route(val route: String) {

    companion object {
        /** Экран авторизации. */
        val SignIn = Route("signin")
        /** Экран регистрации. */
        val SignUp = Route("signup")
        /** Экран списка кофеен. */
        val Shops = Route("shops")
        /** Экран кофеен на карте. */
        val Map = Route("map")
        /** Экран меню кофейни. */
        val Menu = Route("menu/{$ARG_MENU_ID}")
        /** Экран заказа. */
        val Payment = Route("payment/{$ARG_PAYMENT_DATA}")

        /** Аргумент: идентифкатор кофейни. */
        const val ARG_MENU_ID = "id"
        /**
         * Аргумент: данные по заказу.
         *
         * Передается из [Menu] в [Payment].
         * */
        const val ARG_PAYMENT_DATA = "data"
        /**
         * Аргумент: измененные данные по заказу.
         *
         * [Payment] передает измененные данные обратно в [Menu].
         * */
        const val ARG_ORDER_DATA = "order"
    }

    /** Возвращает пункт назначения для навигации. */
    operator fun invoke(): String {
        return route
    }

    /**
     *  Возвращает пункт назначения для навигации.
     *
     *  @param argName Название аргумента в пути.
     *  @param argValue Значение аргумента в пути.
     * */
    operator fun invoke(argName: String, argValue: Any): String {
        return route.replace("{$argName}", "$argValue")
    }

    /**
     *  Возвращает пункт назначения для навигации.
     *
     *  @param arg Пара названия и значения аргумента в пути.
     * */
    operator fun invoke(arg: Pair<String, Any>): String {
        return route.replace("{${arg.first}}", "${arg.second}")
    }

    /**
     *  Возвращает пункт назначения для навигации.
     *
     *  @param args Список пар названий и значений аргументов в пути.
     * */
    operator fun invoke(args: List<Pair<String, Any>>): String {
        var result = route
        for (arg in args) {
            result = result.replace("{${arg.first}}}", "${arg.second}")
        }
        return result
    }
}