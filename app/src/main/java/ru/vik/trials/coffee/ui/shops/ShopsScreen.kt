package ru.vik.trials.coffee.ui.shops

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.entities.Location
import ru.vik.trials.coffee.presentation.Route
import ru.vik.trials.coffee.presentation.Screen
import ru.vik.trials.coffee.presentation.composable


/** Экран со списком кофеен. */
class ShopsScreen : Screen(Route.Shops()) {

    /** Обработчик выбора кофейни. */
    fun onShopClick(id: Int) {
        navController.navigate(Route.Menu(Pair(Route.ARG_MENU_ID, id)))
    }

    /** Обработик кнопки "На карте". */
    fun onMapClick() {
        navController.navigate(Route.Map())
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        this.navController = navController
        navGraphBuilder.composable(route, navController.context.getString(R.string.screen_shops_title)) {
            ShopsBlock(
                modifier = modifier,
                screen = this
            )
        }
    }
}

/** Верстка экрана со списком кофеен. */
@Composable
fun ShopsBlock(modifier: Modifier, screen: ShopsScreen) {
    val viewModel: ShopsViewModel = hiltViewModel()
    val items by viewModel.shops.collectAsState()
    viewModel.refresh()

    Scaffold(
        modifier = modifier
            .fillMaxWidth(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = screen::onMapClick,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.9f),
                    content = {
                        Text(
                            text = stringResource(R.string.shops_to_map)
                        )
                    },
                )
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.shops_empty),
                textAlign = TextAlign.Center,
                style = typography.titleLarge
            )
        }
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items) {
                    ShopItem(it, screen)
                }
            }
        }
    }
}

/** Верстка элементов списка. */
@Composable
fun ShopItem(shop: Location, screen: ShopsScreen) {
    val viewModel: ShopsViewModel = hiltViewModel()
    // Не нашел лучшего способа обновления элемента списка, когда приходят данные по гео-локации,
    // поэтому использую userLocation как аргумент, чтобы интерфейс автоматом изменился при измении этого поля.
    val userLocation by viewModel.userLocation.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 4.dp)
            .clickable {
                screen.onShopClick(shop.id)
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            val dist = viewModel.getDistance(userLocation, shop)
            Text(text = shop.name, style = typography.headlineLarge)
            if (dist != null) {
                val format = stringResource(R.string.shops_distance)
                Text(text = String.format(format, dist), style = typography.bodySmall)
            }
        }
    }
}