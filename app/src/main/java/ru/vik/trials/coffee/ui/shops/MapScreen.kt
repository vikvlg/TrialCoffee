package ru.vik.trials.coffee.ui.shops

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.entities.Location
import ru.vik.trials.coffee.presentation.Route
import ru.vik.trials.coffee.presentation.Screen
import ru.vik.trials.coffee.presentation.UIState
import ru.vik.trials.coffee.presentation.composable

/** Экран кофеен на карте. */
class MapScreen
    : Screen(Route.Map()),
    MapObjectTapListener {

    companion object {
        internal const val TAG = "MapScreen"
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        this.navController = navController
        navGraphBuilder.composable(route, navController.context.getString(R.string.screen_map_title)) {
            MapBlock(
                modifier = modifier,
                screen = this
            )
        }
    }

    /** Обработчик клика по кофейне. */
    override fun onMapObjectTap(map: MapObject, point: Point): Boolean {
        val shop = map.userData as Location
        navController.navigate(Route.Menu(Pair(Route.ARG_MENU_ID, shop.id)))
        return true
    }
}

/** Верстка экрана кофеен на карте. */
@Composable
fun MapBlock(modifier: Modifier, screen: MapScreen) {
    val viewModel: ShopsViewModel = hiltViewModel()
    val context = LocalContext.current
    val mapView = remember { mutableStateOf<MapView?>(null) }

    /** Очистка коффен на карте. */
    fun clearShops() {
        val map = mapView.value?.mapWindow?.map ?: return
        map.mapObjects.clear()
    }
    /** Добавление кофейни на карту. */
    fun addCoffeeShop(shop: Location) {
        val map = mapView.value?.mapWindow?.map ?: return
        map.mapObjects.addPlacemark().apply {
            geometry = Point(shop.point.latitude, shop.point.longitude)
            setIcon(ImageProvider.fromResource(context, R.drawable.coffee_pin_48))
            setText(shop.name, TextStyle().apply {
                placement = TextStyle.Placement.BOTTOM
            })
            userData = shop
            addTapListener(screen)
        }
    }
    /** Перемещение карты к точке. */
    fun moveTo(shop: Location) {
        val map = mapView.value?.mapWindow?.map ?: return
        map.move(
            CameraPosition(
                Point(shop.point.latitude, shop.point.longitude),
                //Point(48.742417, 44.537088),
                15.0f,
                0.0f, // Направление на север
                30.0f // Наклон вертикали
            )
        )
    }

    // INFO: Способ подключения ЯндексКарт в Copmose-приложение подсмотрено на github у Kratos1013,
    // https://github.com/yandex/mapkit-android-demo/issues/317,
    // но работает не везде: на эмуляторе Pixel 6 API 28, программа вылетает с исключением.
    Scaffold(modifier) { padding ->
        AndroidView(
            factory = {MapView(it)},
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            mapView.value = it
        }
    }

    LaunchedEffect(key1 = "loadMapView") {
        // Проинициализируем карту
        snapshotFlow { mapView.value }.collect {
            it?.let {
                MapKitFactory.initialize(context)
                MapKitFactory.getInstance().onStart()
                it.onStart()
            }
        }
    }

    LaunchedEffect("onLoad") {
        // Запросим список кофеен
        viewModel.refresh()

        // Обработчик событий из viewModel
        viewModel.uiState.collect { newValue ->
            if (newValue is UIState.Idle || newValue is UIState.Loading)
                return@collect

            when (newValue) {
                // Ошибка получения данных
                is UIState.Error -> {
                    val text = context.getString(newValue.error)
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        show()
                    }
                }

                // Данные пришли
                is UIState.Success -> {
                    Log.d("TAG", "LaunchedEffect uiState success")
                    clearShops()
                    for (shop in viewModel.shops.value) {
                        addCoffeeShop(shop)
                    }
                    val shop = viewModel.shops.value.firstOrNull()
                    if (shop != null)
                        moveTo(shop)
                }

                else -> Log.d(MapScreen.TAG, "uiState: $newValue")
            }
            viewModel.resetState()
        }
    }
}
