package ru.vik.trials.coffee.ui.register

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.ui.auth.AuthScreen
import ru.vik.trials.coffee.ui.common.InputText
import ru.vik.trials.coffee.ui.common.Screen
import javax.inject.Inject

class RegisterScreen @Inject constructor(): Screen(ROUTE) {
    companion object {
        private const val TAG = "Register"
        const val ROUTE = "register"

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
//        val arguments = listOf(
//            navArgument("id") {
//                type = NavType.LongType
//            },
//            navArgument("name") {
//                type = NavType.StringType
//                nullable = true
//            }
//        )
        navGraphBuilder.composable(route) {
            RegisterBlock(
                modifier = modifier
            )
        }
    }

    fun onRegisterClick() {
        Log.d(TAG, "onRegisterClick")
        navController.navigate(AuthScreen.ROUTE)
    }
}

// TODO: Переделать.
val view: RegisterScreen = RegisterScreen.getInstance()

@Composable
fun RegisterBlock(modifier: Modifier) {
    val viewModel: RegisterViewModel = hiltViewModel()
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
            //onClick = view::onRegisterClick,
            onClick = viewModel::onRegisterClick,
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
