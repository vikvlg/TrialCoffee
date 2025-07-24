package ru.vik.trials.coffee.ui.register

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.SignUpUseCase
import ru.vik.trials.coffee.domain.entities.UserAuthData
import ru.vik.trials.coffee.presentation.BaseViewModel
import ru.vik.trials.coffee.presentation.MutableUIStateFlow
import ru.vik.trials.coffee.presentation.UIState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel() {

    companion object {
        private const val TAG = "RegisterViewModel"
        /** Код ошибки сервера: Некорректные данные в логине или пароле. */
        private const val ERROR_BLANK_DATA = 400
        /** Код ошибки сервера: Такой пользователь уже существует. */
        private const val ERROR_USER_EXISTS = 406
    }

    /** Введенный логин. */
    val email: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    /** Введенный пароль. */
    val password: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    val repPassword: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())

    fun onRegisterClick() {
        if (_uiState.value !is UIState.Idle)
            return

        var errorMsg = 0
        if (email.value.text.isBlank())
            errorMsg = R.string.auth_email_empty
//        // Простенькая проверка на корректность почты
//        else if (!Regex("^([.a-zA-z0-9_]+)@([.a-zA-z0-9_]+)\\.(\\w+)\$").matches(email.value.text))
//            errorMsg = R.string.auth_email_wrong
        else if (password.value.text.isEmpty())
            errorMsg = R.string.auth_password_empty
        else if (password.value.text != repPassword.value.text)
            errorMsg = R.string.auth_password_different

        if (errorMsg != 0) {
            _uiState.value = UIState.Error(errorMsg)
            return
        }

        // Запрос на регистрацию
        signUpUseCase(UserAuthData(email.value.text, password.value.text)).collectNetworkRequest(_uiState, ::mapErrorCodes) {
            Log.d(TAG, "signUpUseCase: $it")
        }

    }

    /** Преобразователь кодов ошибок с APIшного на человеческий [@StringRes]. */
    override fun mapErrorCodes(code: Int): Int {
        return when (code) {
            ERROR_BLANK_DATA -> R.string.auth_error_wrong
            ERROR_USER_EXISTS -> R.string.auth_error_user_exists
            else -> super.mapErrorCodes(code)
        }
    }
}