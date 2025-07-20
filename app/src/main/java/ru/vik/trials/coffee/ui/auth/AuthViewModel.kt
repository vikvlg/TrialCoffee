package ru.vik.trials.coffee.ui.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.vik.trials.coffee.ui.register.RegisterViewModel
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

    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")
}