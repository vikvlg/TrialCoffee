package ru.vik.trials.coffee.ui.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
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

/** Экран с авторизацией. */
class AuthScreen: Screen(Route.SignIn()) {
    companion object {
        internal const val TAG = "AuthScreen"
    }

    /** Обработчик кнопки "Регистрация". */
    fun onRegisterClick() {
        navController.navigate(Route.SignUp())
    }

    /** Обработчик успешной авторизации на сервере. */
    fun onAuthSuccess() {
        navController.navigate(Route.Shops())
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        this.navController = navController
        navGraphBuilder.composable(route, navController.context.getString(R.string.screen_auth_title)) {
            AuthBlock(
                modifier = modifier,
                screen = this
            )
        }
    }
}

/** Тег слушателя нажатия ссылки "Регистрация". */
private const val TAG_CLICK_REG = "AUTH_TO_REG"

/** Верстка экрана авторизации. */
@Composable
fun AuthBlock(modifier: Modifier, screen: AuthScreen) {
    val viewModel: AuthViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Обработчик событий из viewModel
        viewModel.uiState.collect { newValue ->
            if (newValue is UIState.Idle || newValue is UIState.Loading)
                return@collect

            when (newValue) {
                // Ошибка авторизации
                is UIState.Error -> {
                    AppToast.make(context, newValue.error)
                }

                // Успешная авторизация
                is UIState.Success -> {
                    screen.onAuthSuccess()
                }

                else -> Log.d(AuthScreen.TAG, "uiState: $newValue")
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
        Button(
            onClick = viewModel::onAuthClick,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f),
            content = {
                Text(
                    text = stringResource(R.string.auth_login)
                )
            },
        )
        val textLinkReg = buildAnnotatedString {
            withLink(
                link = LinkAnnotation.Clickable(
                    tag = TAG_CLICK_REG,
                    linkInteractionListener = { screen.onRegisterClick() } ,
                ),
            ) {
                append(stringResource(R.string.auth_to_reg))
            }
        }
        Text(
            modifier = Modifier
                .padding(top = 24.dp),
            text = textLinkReg,
            style = TextStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}
