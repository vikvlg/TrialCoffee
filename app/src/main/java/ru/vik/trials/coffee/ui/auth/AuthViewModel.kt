package ru.vik.trials.coffee.ui.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.vik.trials.coffee.R
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor()
    : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"

        private var instance: AuthViewModel? = null
        fun getInstance(): AuthViewModel {
            var inst = instance
            if (inst == null) {
                inst = AuthViewModel()
                instance = inst
            }
            return inst
        }
    }

    val email: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    val password: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())

    fun onAuthClick(): Int {
        if (email.value.text.isBlank())
            return R.string.auth_email_empty

        // Простенькая проверка на корректность почты
        val regexEmail = Regex("^([.a-zA-z0-9_]+)@([.a-zA-z0-9_]+)\\.(\\w+)\$")
        if (!regexEmail.matches(email.value.text))
            return R.string.auth_email_wrong

        if (password.value.text.isEmpty())
            return R.string.auth_password_empty

        // TODO: Запрос на авторизацию

        return 0
    }
}