package ru.vik.trials.coffee

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor()
    : ViewModel() {

    val textValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
}