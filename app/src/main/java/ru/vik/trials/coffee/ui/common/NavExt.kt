package ru.vik.trials.coffee.ui.common

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.get
import ru.vik.trials.coffee.R

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

fun NavGraphBuilder.composable(
    route: String,
    label: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    addDestination(
        ComposeNavigator.Destination(provider[ComposeNavigator::class], content).apply {
            this.route = route
            this.label = label
            Log.d("", "composable2. id: $id, route: $route")
            Log.d("", "   string: ${R.string.screen_auth_title}")
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}

