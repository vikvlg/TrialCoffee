package ru.vik.trials.coffee.data.permissions

import android.content.Context
import android.content.pm.PackageManager

/** Помощник для работы с разрешениями приложения. */
object PermissionsHelper {
    /**
     * Проверяет выданы ли приложению разрешения.
     *
     * @param context Контекст приложения.
     * @param permissions Список разрешений.
     * @return Флаг, что хотя бы одно из указанных разрешений дано.
     */
    fun hasAnyPermissions(context: Context, permissions: List<String>): Boolean {
        for (p in permissions) {
            if (PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(p))
                return true
        }

        return false
    }
}