package ru.vik.trials.coffee.ui.payment

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.vik.trials.coffee.presentation.BaseViewModel
import ru.vik.trials.coffee.presentation.Payment
import java.lang.reflect.Type
import javax.inject.Inject


/** ViewModel для работы с заказом. */
@HiltViewModel
class PaymentViewModel @Inject constructor()
    : BaseViewModel() {

    /** Список заказа. */
    var items = MutableStateFlow<List<Payment>>(listOf())

    /**
     * Сериализует данные по заказу.
     *
     * @return Измененные данные по заказу в json-формате.
     * */
    fun getPayment(): String {
        return Gson().toJson(items.value)
    }

    /**
     * Десериализует данные по заказу.
     *
     * @param payment Данные по заказу в json-формате.
     * */
    fun setPayment(payment: String?) {
        if (payment == null)
            return

        val listType: Type? = object : TypeToken<ArrayList<Payment?>?>() {}.type
        items.value = Gson().fromJson(payment, listType)
    }
}