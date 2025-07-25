package ru.vik.trials.coffee.ui.shops

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.entities.GeoPoint
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
        val shop = map.userData as? Location ?: return false
        navController.navigate(Route.Menu(Pair(Route.ARG_MENU_ID, shop.id)))
        return true
    }

    fun showError(@StringRes id: Int) {
        val context = navController.context
        val text = context.getString(id)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            show()
        }
    }
}

/** Верстка экрана кофеен на карте. */
@Composable
fun MapBlock(modifier: Modifier, screen: MapScreen) {
    val viewModel: ShopsViewModel = hiltViewModel()
    val context = LocalContext.current
    val mapView = remember { mutableStateOf<MapView?>(null) }
    val userLocation by viewModel.userLocation.collectAsState()
    val userMark = remember { mutableStateOf<PlacemarkMapObject?>(null) }

    /** Очистка кофеен на карте. */
    fun clearShops() {
        userMark.value = null
        val map = mapView.value?.mapWindow?.map ?: return
        map.mapObjects.clear()
    }
    /** Добавляет метку на карту. */
    fun addMapMark(point: GeoPoint, name: String, @DrawableRes pinId: Int, data: Any? = null): PlacemarkMapObject? {
        val map = mapView.value?.mapWindow?.map ?: return null
        return map.mapObjects.addPlacemark().apply {
            geometry = Point(point.latitude, point.longitude)
            setIcon(ImageProvider.fromResource(context, pinId))
            setText(name, TextStyle().apply {
                placement = TextStyle.Placement.BOTTOM
            })
            userData = data
            addTapListener(screen)
        }
    }
    /** Добавление кофейни на карту. */
    fun addCoffeeShop(shop: Location) {
        addMapMark(shop.point, shop.name, R.drawable.coffee_pin_48, shop)
    }
    /** Добавление пользователя на карту. */
    fun updateUserMark(point: GeoPoint?) {
        val map = mapView.value?.mapWindow?.map ?: return
        // Удалим старую метку, если она есть
        val mapUserMark = userMark.value
        if (mapUserMark != null) {
            map.mapObjects.remove(mapUserMark)
        }
        userMark.value = null
        if (point == null)
            return

        // Добавим метку с новым положением пользователя
        userMark.value = addMapMark(point, context.getString(R.string.map_you), R.drawable.user_pin_48)
    }

    /** Перемещение камеры к указанной точке. */
    fun moveTo(point: GeoPoint) {
        val map = mapView.value?.mapWindow?.map ?: return
        map.move(
            CameraPosition(
                Point(point.latitude, point.longitude),
                15.0f,
                0.0f, // Направление на север
                30.0f // Наклон вертикали
            )
        )
    }
    /** Перемещение камеры к пользователю. */
    fun moveToUser() {
        val userLocation = viewModel.userLocation.value
        if (userLocation == null) {
            screen.showError(R.string.map_empty_geo)
            return
        }

        moveTo(userLocation)
    }

    /** Перемещение камеры к кофейням. */
    fun moveToShops() {
        // Найдем центр кофеен
        var count = 0
        var latitude = 0.0
        var longitude = 0.0
        for (shop in viewModel.shops.value) {
            count++
            latitude += shop.point.latitude
            longitude += shop.point.longitude
        }

        if (count == 0)
            return

        moveTo(GeoPoint(latitude / count, longitude / count))
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = ::moveToUser
                ) {
                    Icon(Icons.Filled.Person, stringResource(R.string.map_to_user))
                }
                FloatingActionButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = ::moveToShops
                ) {
                    Icon(Icons.Filled.Place, stringResource(R.string.map_to_locations))
                }
            }
        }
    ) { padding ->
        // INFO: Способ подключения ЯндексКарт в Copmose-приложение подсмотрено на github у Kratos1013,
        // https://github.com/yandex/mapkit-android-demo/issues/317,
        // но работает не везде: на эмуляторе Pixel 6 API 28, программа вылетает с исключением.
        AndroidView(
            factory = {MapView(it)},
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            mapView.value = it
        }
    }

    // Обновление положения пользователя
    LaunchedEffect(key1 = userLocation) {
        if (userLocation != null) {
            updateUserMark(userLocation)
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
                    screen.showError(newValue.error)
                }

                // Данные пришли
                is UIState.Success -> {
                    clearShops()
                    for (shop in viewModel.shops.value) {
                        addCoffeeShop(shop)
                    }
                    updateUserMark(viewModel.userLocation.value)
                    moveToShops()
                }

                else -> Log.d(MapScreen.TAG, "uiState: $newValue")
            }
            viewModel.resetState()
        }
    }
}
