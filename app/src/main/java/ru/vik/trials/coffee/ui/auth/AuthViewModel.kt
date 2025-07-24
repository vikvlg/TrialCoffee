package ru.vik.trials.coffee.ui.auth

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.domain.SignInUseCase
import ru.vik.trials.coffee.domain.entities.UserAuthData
import ru.vik.trials.coffee.presentation.BaseViewModel
import ru.vik.trials.coffee.presentation.MutableUIStateFlow
import ru.vik.trials.coffee.presentation.UIState
import javax.inject.Inject

/** ViewModel для работы с экраном авторизации. */
@HiltViewModel
class AuthViewModel @Inject constructor(
    /** Сценарий авторизации. */
    private val signInUseCase: SignInUseCase
)
    : BaseViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"

        /** Код ошибки сервера: Некорректные данные в логине или пароле. */
        private const val ERROR_BLANK_DATA = 400
        /** Код ошибки сервера: Логин/пароль не подходят. */
        private const val ERROR_WRONG_DATA = 404
    }

    // TODO: Проверить работу MutableSharedFlow.
    // TODO: Попробовать отследить изменение email или password.
//    private val _uiState2 = MutableSharedFlow<Unit>()
//    val uiState2 = _uiState2.asSharedFlow()

    /** Введенный логин. */
    val email: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    /** Введенный пароль. */
    val password: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())

    /** Обработчик кнопки "Войти". */
    fun onAuthClick() {
        if (_uiState.value !is UIState.Idle)
            return

        // Базовые проверки введенных данных
        var errorMsg = 0
        if (email.value.text.isBlank())
            errorMsg = R.string.auth_email_empty
//        // Простенькая проверка на корректность почты
//        else if (!Regex("^([.a-zA-z0-9_]+)@([.a-zA-z0-9_]+)\\.(\\w+)\$").matches(email.value.text))
//            errorMsg = R.string.auth_email_wrong
        else if (password.value.text.isBlank())
            errorMsg = R.string.auth_password_empty

        if (errorMsg != 0) {
            _uiState.value = UIState.Error(errorMsg)
            return
        }

        // Запрос на авторизацию
        signInUseCase(UserAuthData(email.value.text, password.value.text)).collectNetworkRequest(_uiState, ::mapErrorCodes) {
            Log.d("TAG", "signInUseCase: $it")
        }
    }

    override fun mapErrorCodes(code: Int): Int {
        return when (code) {
            ERROR_BLANK_DATA -> R.string.auth_error_wrong
            ERROR_WRONG_DATA -> R.string.auth_error_deny
            else -> super.mapErrorCodes(code)
        }
    }
}