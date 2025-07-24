package ru.vik.trials.coffee.data.preferences

import javax.inject.Inject
import javax.inject.Singleton

/** Настройки приложения. */
@Singleton
class UserDataPreferences @Inject constructor(
    preferencesClient: PreferencesClient,
) {

    /** Хранилище настроек. */
    private val preferences = preferencesClient.preferences

    /** Редактор хранилища настроек. */
    private val editor = preferencesClient.editor

    /** Токен сессии. */
    var token: String
        get() = preferences.getString(PreferencesConstants.PREF_TOKEN, "") ?: ""
        set(value) = editor.putString(PreferencesConstants.PREF_TOKEN, value).apply()
}