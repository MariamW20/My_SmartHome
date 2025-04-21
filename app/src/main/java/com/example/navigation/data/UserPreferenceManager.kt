package com.example.navigation.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.edit

class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    companion object {
        const val PREFS_NAME = "user_prefs"
        private const val KEY_COLOR = "app_color"

        val DEFAULT_COLORS = listOf(
            Color(0xFFFFC107),
            Color(0xFF4CAF50),
            Color(0xFF03A9F4)
        )
    }
    fun getUserName(): String = prefs.getString("user_name", "John Doe") ?: "John Doe"
    fun saveUserName(name: String) = prefs.edit { putString("user_name", name) }

    fun getUserEmail(): String = prefs.getString("user_email", "johndoe@example.com") ?: "johndoe@example.com"
    fun saveUserEmail(email: String) = prefs.edit { putString("user_email", email) }

    fun getAppColor(): Int = prefs.getInt("app_color", Color(0xFF000080).toArgb()) // default Navy
    fun saveAppColor(colorInt: Int) = prefs.edit { putInt("app_color", colorInt) }

    fun getAutoArmSecurity(): Boolean = prefs.getBoolean("auto_arm", false)
    fun saveAutoArmSecurity(enabled: Boolean) = prefs.edit { putBoolean("auto_arm", enabled) }

    fun getAppNotifications(): Boolean = prefs.getBoolean("notifications", true)
    fun saveAppNotifications(enabled: Boolean) = prefs.edit { putBoolean("notifications", enabled) }
}