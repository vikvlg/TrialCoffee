package ru.vik.trials.coffee.ui.payment

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.vik.trials.coffee.R
import ru.vik.trials.coffee.presentation.AppToast
import ru.vik.trials.coffee.presentation.Payment
import ru.vik.trials.coffee.presentation.Route
import ru.vik.trials.coffee.presentation.Screen
import ru.vik.trials.coffee.presentation.composable
import ru.vik.trials.coffee.ui.HorizontalNumberPicker

/** Экран с оплатой заказа. */
class PaymentScreen : Screen(Route.Payment()) {

    /** Обработчик кнопки "Оплатить". */
    fun onPayClick() {
        val context = navController.context
        AppToast.show(context, R.string.payment_pay_feature, true)
    }

    /**
     * Обработчик изменения количества в заказе.
     *
     * @param newOrder Полные данные по заказу.
     */
    fun onChangeCount(newOrder: String) {
        val entry = navController.previousBackStackEntry
        if (entry == null) {
            return
        }

        // Сохраним измененный заказ для предыдущего окна.
        // TODO: Нужно это делать на событии типа OnBackPressedCallback,
        // но пока не нашел хорошего решения.
        entry.savedStateHandle[Route.ARG_ORDER_DATA] = newOrder
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        this.navController = navController
        navGraphBuilder.composable(
            route,
            label = navController.context.getString(R.string.screen_payment_title),
            arguments = listOf(navArgument(Route.ARG_PAYMENT_DATA) { type = NavType.StringType })
        ) { stackEntry ->
            PaymentBlock(
                modifier = modifier,
                payment = stackEntry.arguments?.getString(Route.ARG_PAYMENT_DATA),
                screen = this
            )
        }
    }
}

/** Верстка экрана заказа. */
@Composable
fun PaymentBlock(modifier: Modifier, payment: String?, screen: PaymentScreen) {
    val viewModel: PaymentViewModel = hiltViewModel()
    val items by viewModel.items.collectAsState()
    viewModel.setPayment(payment)

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = screen::onPayClick,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.9f),
                    enabled = items.isNotEmpty(),
                    content = {
                        Text(
                            text = stringResource(R.string.payment_pay)
                        )
                    },
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (items.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.payment_empty),
                    textAlign = TextAlign.Center,
                    style = typography.titleLarge
                )
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items) {
                        PaymentItem(it, screen)
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    text = stringResource(R.string.payment_hint),
                    textAlign = TextAlign.Center,
                    style = typography.titleLarge
                )
            }
        }
    }
}

/** Верстка элемента списка заказа. */
@Composable
fun PaymentItem(item: Payment, screen: PaymentScreen) {
    val viewModel: PaymentViewModel = hiltViewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1.0f)
            ) {
                Text(text = item.name, style = typography.headlineLarge)
                val price = String.format(stringResource(R.string.menu_price_format), item.price)
                Text(text = price, style = typography.bodySmall)
            }
            Column {
                HorizontalNumberPicker(
                    height = 18.dp,
                    default = item.count,
                    onValueChange = {
                        item.count = it
                        screen.onChangeCount(viewModel.getPayment())
                    }
                )
            }
        }
    }
}