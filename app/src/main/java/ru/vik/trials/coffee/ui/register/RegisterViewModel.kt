package ru.vik.trials.coffee.ui.register

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // State as StateFlow for reactivity
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    var _password: String = ""
    val _repPassword: String = ""

    val email: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    val password: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    val repPassword: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())

    fun setPassword(value: String) {
        _password = value
    }

    fun onRegisterClick() {
        Log.d(TAG, "onRegisterClick; email: ${email.value.text}; password: ${password.value.text}")
    }
}