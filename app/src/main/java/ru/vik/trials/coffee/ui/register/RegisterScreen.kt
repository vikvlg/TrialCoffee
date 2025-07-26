package ru.vik.trials.coffee.ui.register

import android.util.Log
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
import ru.vik.trials.coffee.presentation.AppToast
import ru.vik.trials.coffee.presentation.Route
import ru.vik.trials.coffee.presentation.Screen
import ru.vik.trials.coffee.presentation.UIState
import ru.vik.trials.coffee.presentation.composable
import ru.vik.trials.coffee.ui.InputText
import javax.inject.Inject

/** Экран регистрации пользователя. */
class RegisterScreen @Inject constructor()
    : Screen(Route.SignUp()) {

    companion object {
        internal const val TAG = "Register"
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        this.navController = navController
        navGraphBuilder.composable(route, navController.context.getString(R.string.screen_reg_title)) {
            RegisterBlock(
                modifier = modifier,
                screen = this
            )
        }
    }

    /** Обработчик успешной регистрации. */
    fun onRegisterSuccess() {
        navController.navigate(Route.Shops())
    }
}

/** Верстка экрана регистрации. */
@Composable
fun RegisterBlock(modifier: Modifier, screen: RegisterScreen) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Обработчик событий запроса регистрации
        viewModel.uiState.collect { newValue ->
            if (newValue is UIState.Idle || newValue is UIState.Loading)
                return@collect

            when (newValue) {
                // Ошибка регистрации
                is UIState.Error -> {
                    AppToast.show(context, newValue.error)
                }

                // Успешная регистрация
                is UIState.Success -> {
                    screen.onRegisterSuccess()
                }

                else -> Log.d(RegisterScreen.TAG, "uiState: $newValue")
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
