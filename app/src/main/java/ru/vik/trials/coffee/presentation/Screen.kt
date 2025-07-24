package ru.vik.trials.coffee.presentation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/** Базовый экран приложения. */
abstract class Screen(val route: String) {
    /** Контроллер навигации. */
    protected lateinit var navController: NavHostController

    /** Регистрирует экран в графе навигации. */
    abstract fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier = Modifier
    )
}
