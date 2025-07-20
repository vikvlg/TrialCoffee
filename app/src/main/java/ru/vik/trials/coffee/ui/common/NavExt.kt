package ru.vik.trials.coffee.ui.common

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

fun NavGraphBuilder.register(
    screenApi: Screen,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    screenApi.registerGraph(
        navGraphBuilder = this,
        navController = navController,
        modifier = modifier
    )
}