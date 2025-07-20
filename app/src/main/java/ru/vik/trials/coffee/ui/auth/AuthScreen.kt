package ru.vik.trials.coffee.ui.auth

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ru.vik.trials.coffee.ui.common.InputText
import ru.vik.trials.coffee.ui.common.Screen
import ru.vik.trials.coffee.ui.common.composable
import ru.vik.trials.coffee.ui.register.RegisterScreen

class AuthScreen: Screen(ROUTE) {
    companion object {
        private const val TAG = "AuthScreen"
        const val ROUTE = "auth"

        private var instance: AuthScreen? = null
        fun getInstance(): AuthScreen {
            var inst = instance
            if (inst == null) {
                inst = AuthScreen()
                instance = inst
            }
            return inst
        }
    }

    fun onRegisterCLick() {
        Log.d(TAG, "onRegisterCLick")
        navController.navigate(RegisterScreen.ROUTE)
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

private const val CLICK_REG_TAG = "AUTH_TO_REG"

@Composable
fun AuthBlock(modifier: Modifier, screen: AuthScreen) {
    val viewModel: AuthViewModel = hiltViewModel()
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize(),
    ) {
        InputText(R.string.input_mail, R.string.input_mail_hint, viewModel.email)
        InputText(R.string.input_password, R.string.input_password_hint, viewModel.password, true)
        Button(
            onClick = {
                val error = viewModel.onAuthClick()
                if (error != 0) {
                    Toast.makeText(context, context.getString(error), Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        show()
                    }
                }
            },
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
                    tag = CLICK_REG_TAG,
                    linkInteractionListener = {
                        screen.onRegisterCLick()
                    },
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
