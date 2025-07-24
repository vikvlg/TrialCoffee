package ru.vik.trials.coffee.ui.register

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.presentation.Route
import ru.vik.trials.coffee.presentation.Screen
import ru.vik.trials.coffee.presentation.UIState
import ru.vik.trials.coffee.ui.InputText
import ru.vik.trials.coffee.ui.common.composable
import javax.inject.Inject

class RegisterScreen @Inject constructor()
    : Screen(Route.SignUp()) {

    companion object {
        private const val TAG = "Register"

        private var instance: RegisterScreen? = null
        fun getInstance(): RegisterScreen {
            var inst = instance
            if (inst == null) {
                inst = RegisterScreen()
                instance = inst
            }
            return inst
        }
    }

    @Inject lateinit var viewModel: RegisterViewModel

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        this.navController = navController
//        val arguments = listOf(
//            navArgument("id") {
//                type = NavType.LongType
//            },
//            navArgument("name") {
//                type = NavType.StringType
//                nullable = true
//            }
//        )
        navGraphBuilder.composable(route, navController.context.getString(R.string.screen_reg_title)) {
            RegisterBlock(
                modifier = modifier,
                screen = this
            )
        }
    }

    fun onRegisterSuccess() {
        navController.navigate(Route.Shops())
    }
}

@Composable
fun RegisterBlock(modifier: Modifier, screen: RegisterScreen) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Обработчик событий из viewModel
        viewModel.uiState.collect { newValue ->
            if (newValue is UIState.Idle || newValue is UIState.Loading)
                return@collect

            when (newValue) {
                is UIState.Error -> {
                    val text = context.getString(newValue.error)
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        show()
                    }
                }

                is UIState.Success -> {
                    screen.onRegisterSuccess()
                }

                else -> Log.d("TAG", "LaunchedEffect uiState: $newValue")
            }
            viewModel.resetState()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize(),
    ) {
        InputText(R.string.input_mail, R.string.input_mail_hint, viewModel.email)
        InputText(R.string.input_password, R.string.input_password_hint, viewModel.password, true)
        InputText(R.string.input_rep_password, R.string.input_rep_password_hint, viewModel.repPassword, true)
        Button(
            onClick = {
                viewModel.onRegisterClick()
            },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f),
            content = {
                Text(
                    text = stringResource(R.string.reg_register)
                )
            },
        )
    }
}
