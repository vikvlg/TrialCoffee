package ru.vik.trials.coffee.ui.menu

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.entities.MenuItem
import ru.vik.trials.coffee.presentation.Route
import ru.vik.trials.coffee.presentation.Screen
import ru.vik.trials.coffee.presentation.UIState
import ru.vik.trials.coffee.presentation.composable
import ru.vik.trials.coffee.ui.HorizontalNumberPicker

/** Экран с меню кофейни. */
class MenuScreen()
    : Screen(Route.Menu()) {

    /** Обработчик кнопки "К оплате". */
    fun onPaymentClick(payment: String) {
        // Данные будем передавать в json-сериализованном виде через путь навигации.
        // Пробовал другие варианты (BackStack), но не получилось.
        navController.navigate(route = Route.Payment(Route.ARG_PAYMENT_DATA, payment))
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        this.navController = navController
        navGraphBuilder.composable(
            route = route,
            label = navController.context.getString(R.string.screen_menu_title),
            arguments = listOf(navArgument(Route.ARG_MENU_ID) { type = NavType.IntType })
        ) { stackEntry ->
            val id = stackEntry.arguments?.getInt(Route.ARG_MENU_ID) ?: 0
            MenuBlock(
                modifier = modifier,
                shopId = id,
                screen = this
            )
        }
    }
}

/** Верстка экрана меню. */
@Composable
fun MenuBlock(modifier: Modifier, shopId: Int, screen: MenuScreen) {
    val context = LocalContext.current
    val viewModel: MenuViewModel = hiltViewModel()
    val items by viewModel.menu.collectAsState()
    // Запросим меню кофейни
    viewModel.refresh(shopId)

    LaunchedEffect("onLoad") {
        // Обработчик событий из viewModel
        viewModel.uiState.collect { newValue ->
            if (newValue is UIState.Idle || newValue is UIState.Loading)
                return@collect

            // Ошибка получения данных
            if (newValue is UIState.Error) {
                    val text = context.getString(newValue.error)
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        show()
                    }
                }
            viewModel.resetState()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { screen.onPaymentClick(viewModel.getSerializedPayment()) },
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.9f),
                    content = {
                        Text(
                            text = stringResource(R.string.menu_to_payment)
                        )
                    },
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(0.9f),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(items) {
                        MenuItem(it, screen)
                    }
                }
            )
        }
    }
}

/** Верстка пунктов меню. */
@Composable
fun MenuItem(item: MenuItem, screen: MenuScreen) {
    val viewModel: MenuViewModel = hiltViewModel()

    Card {
        Column {
            val bmp = viewModel.getImage(LocalContext.current, item.imageURL)
            Image(
                bitmap = bmp,
                //painter = ColorPainter(Color.DarkGray),
                modifier = Modifier.size(200.dp),
                contentDescription = item.name
            )
            Text(
                text = item.name
            )
            Row {
                val price = String.format(stringResource(R.string.menu_price_format), item.price)
                Text(
                    modifier = Modifier.weight(1.0f),
                    text = price
                )
                HorizontalNumberPicker(
                    height = 18.dp,
                    default = viewModel.getItemCount(item),
                    onValueChange = { value -> viewModel.setItemCount(item, value)}
                )
            }
        }
    }
}
