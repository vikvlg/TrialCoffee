package ru.vik.trials.coffee.ui.payment

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.vik.trials.coffee.presentation.BaseViewModel
import ru.vik.trials.coffee.presentation.Payment
import java.lang.reflect.Type
import javax.inject.Inject


@HiltViewModel
class PaymentViewModel @Inject constructor()
    : BaseViewModel() {

//    private var _items = ArrayList<Payment>()
    var items = MutableStateFlow<List<Payment>>(listOf())

    fun setPayment(payment: String?) {
        if (payment == null)
            return

        val listType: Type? = object : TypeToken<ArrayList<Payment?>?>() {}.type
        items.value = Gson().fromJson(payment, listType)

//        Log.d("TAG", "payment size: ${items.value.size}")
//        for (it in items.value) {
//            Log.d("TAG", "   ${it.name} ${it.price} ${it.count}")
//        }
    }
}