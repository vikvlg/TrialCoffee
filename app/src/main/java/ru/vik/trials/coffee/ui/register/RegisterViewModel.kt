package ru.vik.trials.coffee.ui.register

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.vik.trials.coffee.R
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor()
    : ViewModel() {

    companion object {
        private const val TAG = "RegisterViewModel"

        private var instance: RegisterViewModel? = null
        fun getInstance(): RegisterViewModel {
            var inst = instance
            if (inst == null) {
                inst = RegisterViewModel()
                instance = inst
            }
            return inst
        }
    }

    val email: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    val password: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    val repPassword: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())

    fun onRegisterClick(): Int {
        Log.d(TAG, "onRegisterClick; email: ${email.value.text}; password: ${password.value.text}")

        if (email.value.text.isBlank())
            return R.string.auth_email_empty

        // Простенькая проверка на корректность почты
        val regexEmail = Regex("^([.a-zA-z0-9_]+)@([.a-zA-z0-9_]+)\\.(\\w+)\$")
        if (!regexEmail.matches(email.value.text))
            return R.string.auth_email_wrong

        if (password.value.text.isEmpty())
            return R.string.auth_password_empty

        if (password.value.text != repPassword.value.text)
            return R.string.auth_password_different

        // TODO: Запрос на регистрацию

        return 0

    }
}