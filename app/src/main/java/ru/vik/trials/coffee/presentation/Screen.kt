package ru.vik.trials.coffee.presentation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

abstract class Screen(val route: String) {
    protected lateinit var navController: NavHostController

    abstract fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier = Modifier
    )
}
