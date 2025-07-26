package ru.vik.trials.coffee.ui

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.presentation.Route
import ru.vik.trials.coffee.presentation.register
import ru.vik.trials.coffee.ui.auth.AuthScreen
import ru.vik.trials.coffee.ui.menu.MenuScreen
import ru.vik.trials.coffee.ui.payment.PaymentScreen
import ru.vik.trials.coffee.ui.register.RegisterScreen
import ru.vik.trials.coffee.ui.shops.MapScreen
import ru.vik.trials.coffee.ui.shops.ShopsScreen


@ExperimentalMaterial3Api
@Composable
fun CoffeeApp() {
    val navController = rememberNavController()
    val showBackArrow = remember { mutableStateOf(false) }
    val appTile = stringResource(R.string.screen_title)
    val topBarTitle = remember { mutableStateOf(appTile) }

    navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
        // Отследим навигацию приложения
        override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
            // Изменим название экрана
            val label = destination.label?.toString()
            topBarTitle.value = if (label.isNullOrBlank()) appTile else label
            // Изменим флаг отображения кнопки "Назад"
            showBackArrow.value = (controller.previousBackStackEntry != null)
        }
    })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                // Заголовок экрана.
                title = {
                    Text(topBarTitle.value)
                },
                // Кнопка "Назад"
                navigationIcon = {
                    if (showBackArrow.value)
                        IconButton(
                            onClick = {
                                // BUG: Без этой проверки глючит, т.к. в самом начале backQueue содержит 2 элемента.
                                if (navController.previousBackStackEntry != null)
                                    navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.navigation_back)
                            )
                        }
                },
//                // Топ-меню экрана.
//                actions = {
//                    IconButton(onClick = { /* doSomething() */ }) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = stringResource(R.string.navigation_menu_item)
//                        )
//                    }
//                },
            )
        },
        content = { padding ->
            val modifier = Modifier.padding(padding)
            NavHost(
                navController = navController,
                startDestination = Route.SignIn()
            ) {
                register(
                    classes = arrayOf(
                        AuthScreen::class.java,
                        RegisterScreen::class.java,
                        MapScreen::class.java,
                        ShopsScreen::class.java,
                        MenuScreen::class.java,
                        PaymentScreen::class.java,
                    ),
                    navController,
                    modifier
                )
            }
        },
    )
}
