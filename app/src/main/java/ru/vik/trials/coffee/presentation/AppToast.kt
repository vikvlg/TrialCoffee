package ru.vik.trials.coffee.presentation

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes

object AppToast {
    fun make(context: Context, @StringRes textId: Int, showLong: Boolean = false) {
        val duration = if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, context.getString(textId), duration).apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
                setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            show()
        }
    }
}