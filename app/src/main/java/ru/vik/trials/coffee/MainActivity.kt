package ru.vik.trials.coffee

import android.os.Bundle
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.vik.trials.coffee.ui.auth.AuthScreen
import ru.vik.trials.coffee.ui.common.register
import ru.vik.trials.coffee.ui.register.RegisterScreen
import ru.vik.trials.coffee.ui.theme.TrialCoffeeTheme

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(topBarTitle.value)
                            },
                            navigationIcon = {
                                //if (navController.previousBackStackEntry != null)
                                if (showBackArrow.value)
                                    IconButton(
                                        onClick = {
//                                            Log.d(TAG, "backQueue: ${navController.backQueue.size}")
//                                            for (q in navController.backQueue) {
//                                                Log.d(TAG, "   id: ${q.id}; destination: ${q.destination}")
//                                            }
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
                            // TODO: Для экранов нужно использовать DI.
                            register(
                                AuthScreen.getInstance(),
                                navController,
                                modifier)
                            register(
                                RegisterScreen.getInstance(),
                                navController,
                                modifier)
//                            composable("test") {
//                                Greeting(
//                                    name = "Android",
//                                    modifier = Modifier.padding(padding)
//                                )
//                                Button(
//                                    onClick = {
//                                        showBackArrow.value = true
//                                        navController.navigate("test2")
//                                    },
//                                    modifier = Modifier
//                                        .padding(padding)
//                                        .fillMaxWidth(fraction = 0.9f)
//                                        .background(Color.Black)
//                                        .padding(top = 32.dp),
//                                    content = {
//                                        Text(
//                                            text = "Action"
//                                        )
//                                    }
//                                )
//                            }
//                            composable("test2") {
//                                Text(
//                                    text = "Second view",
//                                    modifier = Modifier.padding(padding)
//                                )
//                            }
                        }
                    },
                )
//                { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}

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