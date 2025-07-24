package ru.vik.trials.coffee

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.vik.trials.coffee.presentation.Consts
import ru.vik.trials.coffee.presentation.Route
import ru.vik.trials.coffee.presentation.register
import ru.vik.trials.coffee.ui.auth.AuthScreen
import ru.vik.trials.coffee.ui.common.checkPermissions
import ru.vik.trials.coffee.ui.menu.MenuScreen
import ru.vik.trials.coffee.ui.payment.PaymentScreen
import ru.vik.trials.coffee.ui.register.RegisterScreen
import ru.vik.trials.coffee.ui.shops.MapScreen
import ru.vik.trials.coffee.ui.shops.ShopsScreen
import ru.vik.trials.coffee.ui.theme.TrialCoffeeTheme

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionList = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        checkPermissions(permissionList)

        enableEdgeToEdge()
        setContent {
            TrialCoffeeTheme {
                val navController = rememberNavController()
                val showBackArrow = remember { mutableStateOf(false) }
                val topBarTitle = remember { mutableStateOf(getString(R.string.screen_title)) }

                navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
                    // Отследим навигацию приложения
                    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
                        // Изменим название экрана
                        val label = destination.label?.toString()
                        topBarTitle.value = if (label.isNullOrBlank()) getString(R.string.screen_title) else label
                        // Изменим флаг отображения кнопки "Назад"
                        showBackArrow.value = (controller.previousBackStackEntry != null)
                    }
                })

                //MapTest(Modifier.fillMaxSize())
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
                                            // BUG: Без этой проверки глючит (в самом начале backQueue содержит 2 элемента).
                                            if (navController.previousBackStackEntry != null)
                                                navController.popBackStack()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = getString(R.string.navigation_back)
                                        )
                                    }
                            },
//                            // Меню экрана.
//                            actions = {
//                                IconButton(onClick = { /* doSomething() */ }) {
//                                    Icon(
//                                        imageVector = Icons.Filled.Menu,
//                                        contentDescription = getString(R.string.navigation_menu_item)
//                                    )
//                                }
//                            },
                        )
                    },
                    content = { padding ->
                        val modifier = Modifier.padding(padding)
                        NavHost(
                            navController = navController,
                            startDestination = Route.SignIn()
                        ) {
                            register(
                                AuthScreen(),
                                navController,
                                modifier)
                            register(
                                RegisterScreen(),
                                navController,
                                modifier)
                            register(
                                MapScreen(),
                                navController,
                                modifier)
                            register(
                                ShopsScreen(),
                                navController,
                                modifier)
                            register(
                                MenuScreen(),
                                navController,
                                modifier)
                            register(
                                PaymentScreen(),
                                navController,
                                modifier)
                        }
                    },
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Consts.PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d(TAG, "Permission is granted")
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Log.d(TAG, "Permission is NOT granted")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                Log.d(TAG, "Permission is unknown")
                // Ignore all other requests.
            }
        }

        Log.d(TAG, "onRequestPermissionsResult. requestCode: $requestCode")
        Log.d(TAG, "permissions:")
        for (p in permissions) {
            Log.d(TAG, "   $p")
        }
        Log.d(TAG, "permissions grantResults:")
        for (r in grantResults) {
            Log.d(TAG, "   $r")
        }
    }
}
