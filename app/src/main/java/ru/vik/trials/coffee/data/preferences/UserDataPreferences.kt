package ru.vik.trials.coffee.data.preferences

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataPreferences @Inject constructor(
    preferencesClient: PreferencesClient,
) {

    private val preferences = preferencesClient.preferences
    private val editor = preferencesClient.editor

    var token: String
        get() = preferences.getString(PreferencesConstants.PREF_TOKEN, "") ?: ""
        set(value) = editor.putString(PreferencesConstants.PREF_TOKEN, value).apply()
}