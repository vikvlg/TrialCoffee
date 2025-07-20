package ru.vik.trials.coffee.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.ui.common.InputText
import ru.vik.trials.coffee.ui.common.Screen

class AuthScreen: Screen(ROUTE) {
    companion object {
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

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(route) {
            AuthBlock(
                modifier = modifier
            )
        }
    }

}

@Preview
@Composable
fun AuthBlock(modifier: Modifier, viewModel: AuthViewModel = AuthViewModel.getInstance()) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        InputText(R.string.input_mail, "example@example.ru", {})
        InputText(R.string.input_password, "password", {}, true)
        Button(
            onClick = { /*viewModel.onRegisterClick()*/ },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f),
            content = {
                Text(
                    text = stringResource(R.string.auth_login)
                )
            },
            //contentDescription = stringResource(R.string.favourite)
        )
    }
}
