package ru.vik.trials.coffee

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.vik.trials.coffee.presentation.Consts
import ru.vik.trials.coffee.data.GetLocationsUseCase
import ru.vik.trials.coffee.data.SignInUseCase
import ru.vik.trials.coffee.domain.entities.Resp
import ru.vik.trials.coffee.domain.entities.UserAuthData
import ru.vik.trials.coffee.ui.auth.AuthScreen
import ru.vik.trials.coffee.ui.common.checkPermissions
import ru.vik.trials.coffee.ui.common.register
import ru.vik.trials.coffee.ui.map.MapScreen
import ru.vik.trials.coffee.ui.register.RegisterScreen
import ru.vik.trials.coffee.ui.theme.TrialCoffeeTheme
import javax.inject.Inject

class TestClass<T> {
    val errorCode: Int

    val value: T?

    constructor(error: Int) {
        errorCode = error
        value = null
    }
}

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    @Inject lateinit var signIn: SignInUseCase
    @Inject lateinit var getLocations: GetLocationsUseCase


    /**
     * [launch] coroutine with [repeatOnLifecycle] API
     *
     * @param state [Lifecycle.State][androidx.lifecycle.Lifecycle.State] in which `block` runs in a new coroutine. That coroutine
     * will cancel if the lifecycle falls below that state, and will restart if it's in that state
     * again.
     * @param block The block to run when the lifecycle is at least in [state] state.
     */
    inline fun LifecycleOwner.launchWithRepeatOnLifecycle(
        state: Lifecycle.State,
        crossinline block: suspend CoroutineScope.() -> Unit
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(state) {
                block()
            }
        }
    }

    /**
     * [collect] flow safely with [launchWithRepeatOnLifecycle]
     */
    inline fun <T> Flow<T>.launchAndCollectIn(
        viewLifecycleOwner: LifecycleOwner,
        state: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline collector: suspend CoroutineScope.(T) -> Unit
    ) = viewLifecycleOwner.launchWithRepeatOnLifecycle(state) {
        collect { collector(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //LifecycleOwner()

        val permissionList = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        checkPermissions(permissionList)

//        val t = TestClass<AuthToken>(123)
//        Log.d("AuthRepositoryImpl", "test: ${t.errorCode}")
//        val t = Resp<AuthToken>(null, 404)
//        Log.d("AuthRepositoryImpl", "test: ${t.error}")

//        this.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                signIn(UserAuthData("test", "test2")).collect {
//                    Log.d("AuthRepositoryImpl", "it: $it")
//                    when (it) {
//                        is Either.Left -> {
//                            Log.d("AuthRepositoryImpl", "token: ${it.value.token}")
//                        }
//                        is Either.Right -> {
//                            Log.d("AuthRepositoryImpl", "error: ${it.value}")
//                        }
//                    }
//                }
//            }
//        }



//        signIn(UserAuthData("test", "test")).launchAndCollectIn(this) {
////            when (it) {
////                is Either.Left -> {
////                    Log.d("AuthRepositoryImpl", "token: ${it.value.token}")
////                }
////                is Either.Right -> {
////                    Log.d("AuthRepositoryImpl", "error: ${it.value}")
////                }
////            }
//
////            Log.d("AuthRepositoryImpl", "it: $it")
//            if (it.isSuccess) {
//                Log.d("AuthRepositoryImpl", "success sign in")
//                getLocations().launchAndCollectIn(this@MainActivity) { loc ->
//                    if (loc.isSuccess) {
//                        Log.d("AuthRepositoryImpl", "locations: ${loc.value?.name}")
//                    } else {
//                        Log.d("AuthRepositoryImpl", "error get locations: ${loc.error}")
//                    }
//                }
//            } else {
//                Log.d("AuthRepositoryImpl", "error signIn: ${it.error}")
//            }
//        }

//            .onStart {
//                Log.d("AuthRepositoryImpl", "onStart")
//            }
//            .onCompletion {
//                Log.d("AuthRepositoryImpl", "onCompletion")
//            }
//            .onEach { it ->
//                Log.d("AuthRepositoryImpl", "onEach: ${it.token}")
//            }
//            .run {
//                Log.d("AuthRepositoryImpl", "run")
//            }

        //val client = CoffeeClient()
        //client.signIn("vik", "derparol")
        //client.getLocations()
        //client.signUp("vik", "derparol")
//        val req = AuthReq("vik@vlg.ru", "derparol")
//        Log.d(TAG, "req: $req")

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
                            title = {
                                Text(topBarTitle.value)
                            },
                            navigationIcon = {
                                if (showBackArrow.value)
                                    IconButton(
                                        onClick = {
                                            // BUG: Без проверки глючит (в самом начале backQueue содержит 2 элемента).
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
                            actions = {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = getString(R.string.navigation_menu_item)
                                    )
                                }
                            },
                        )
                    },
                    content = { padding ->
                        val modifier = Modifier.padding(padding)
                        NavHost(
                            navController = navController,
                            startDestination = AuthScreen.ROUTE
                        ) {
                            //val authScreen: AuthScreen
                            // TODO: Для экранов нужно использовать DI.
                            register(
                                AuthScreen.getInstance(),
                                navController,
                                modifier)
                            register(
                                RegisterScreen.getInstance(),
                                navController,
                                modifier)
                            register(
                                MapScreen.getInstance(),
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

//@Composable
//fun MapTest(modifier: Modifier) {
//    val context = LocalContext.current
//    val mapView = remember { mutableStateOf<MapView?>(null) }
//
//    Scaffold(modifier) { _ ->
//        AndroidView(
//            factory = {MapView(it)},
//            modifier = Modifier.fillMaxSize()
//        ) {
//            mapView.value = it
//        }
//    }
//
//    LaunchedEffect(key1 = "loadMapView") {
//        snapshotFlow { mapView.value }.collect {
//            it?.let {
//                MapKitFactory.initialize(context)
//                MapKitFactory.getInstance().onStart()
//                it.onStart()
//            }
//        }
//    }
//}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = hiltViewModel()
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    val textValue = remember { mutableStateOf(TextFieldValue()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = "Hello $name! Again!",
            modifier = modifier
                .background(Color.Red)
        )
        BasicTextField(
            value = viewModel.textValue.value,
            onValueChange = { viewModel.textValue.value = it  },
            modifier = modifier
                .background(Color.Blue)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrialCoffeeTheme {
        Greeting("Android")
    }
}