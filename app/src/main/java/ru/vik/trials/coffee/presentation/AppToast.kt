package ru.vik.trials.coffee.presentation

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes

/** Помощник по работе с Toast. */
object AppToast {
    /**
     * Отображает toast-подсказку.
     *
     * Toast несколько нестабилен:
     *  - в SDK 30+ отключили метод setGravity;
     *  - на некоторых версиях Андроида, при подгрузке иконки срабатывает исключение IOException: Failed to load asset path.
     *
     *  Так что лучше, чтобы был единый код, в котором можно будет все это учесть.
     * */
    fun show(context: Context, @StringRes textId: Int, showLong: Boolean = false) {
        val duration = if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, context.getString(textId), duration).apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
                setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            show()
        }
    }
}