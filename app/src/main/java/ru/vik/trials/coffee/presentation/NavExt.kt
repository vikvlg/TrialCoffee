package ru.vik.trials.coffee.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.get

// Идея взята с https://habr.com/ru/companies/moex/articles/586192/.

/** Регистрирует экран в графе навигации. */
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

/**
 * Добавляет экран в граф навигации.
 *
 * @param route Маршрут навигации к экрану.
 * @param label Заголовок экрана в top-баре.
 * */
fun NavGraphBuilder.composable(
    route: String,
    label: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
    //content: @Composable AnimatedContentScope.(@JvmSuppressWildcards NavBackStackEntry) -> Unit
) {
    addDestination(
        ComposeNavigator.Destination(provider[ComposeNavigator::class]) { entry -> content(entry) }.apply {
            this.route = route
            this.label = label
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}

