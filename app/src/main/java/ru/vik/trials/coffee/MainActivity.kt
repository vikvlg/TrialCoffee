package ru.vik.trials.coffee

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import dagger.hilt.android.AndroidEntryPoint
import ru.vik.trials.coffee.presentation.AppToast
import ru.vik.trials.coffee.ui.CoffeeApp
import ru.vik.trials.coffee.ui.theme.TrialCoffeeTheme


@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    // Работа с разрешениями приложения
    val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var granted = false
        permissions.entries.forEach {
            granted = granted || it.value
        }
        if (!granted) {
            AppToast.make(this, R.string.permissions_not_granted, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запросим разрешения для работы с местоположением
        requestMultiplePermissions.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))

        enableEdgeToEdge()
        setContent {
            TrialCoffeeTheme {
                CoffeeApp()
            }
        }
    }
}
