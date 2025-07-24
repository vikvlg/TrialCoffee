package ru.vik.trials.coffee.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Клиент для хранения настроек приложения. */
@Singleton
class PreferencesClient @Inject constructor(
    @ApplicationContext context: Context,
) {
    /** Хранилище настроек. */
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    /** Редактор хранилища настроек. */
    val editor: SharedPreferences.Editor = preferences.edit()
}