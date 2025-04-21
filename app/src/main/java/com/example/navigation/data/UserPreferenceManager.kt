package com.example.navigation.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit

class UserPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    // User information
    fun saveUserName(name: String) {
        sharedPreferences.edit {
            putString(KEY_USER_NAME, name)
        }
    }

    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, "John Doe") ?: "John Doe"
    }

    fun saveUserEmail(email: String) {
        sharedPreferences.edit {
            putString(KEY_USER_EMAIL, email)
        }
    }

    fun getUserEmail(): String {
        return sharedPreferences.getString(KEY_USER_EMAIL, "john@someorg.com") ?: "john@someorg.com"
    }

    // Improved App color handling
    fun saveAppColor(color: Int) {
        sharedPreferences.edit {
            putLong(KEY_APP_COLOR, color.toLong())
        }
    }


    fun getAppColor(): Color {
        return try {
            val defaultColor = Color(0xFFFFC107) // Default amber color
            val colorValue = sharedPreferences.getLong(KEY_APP_COLOR, defaultColor.value.toLong())
            Color(colorValue)
        } catch (e: Exception) {
            Color(0xFFFFC107) // Fallback to default if any error occurs
        }
    }

    // Toggles
    fun saveAutoArmSecurity(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_AUTO_ARM_SECURITY, enabled)
        }
    }

    fun getAutoArmSecurity(): Boolean {
        return sharedPreferences.getBoolean(KEY_AUTO_ARM_SECURITY, true)
    }

    fun saveAppNotifications(enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_APP_NOTIFICATIONS, enabled)
        }
    }

    fun getAppNotifications(): Boolean {
        return sharedPreferences.getBoolean(KEY_APP_NOTIFICATIONS, false)
    }

    companion object {
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_APP_COLOR = "app_color"
        private const val KEY_AUTO_ARM_SECURITY = "auto_arm_security"
        private const val KEY_APP_NOTIFICATIONS = "app_notifications"

        // Default colors for the app
        val DEFAULT_COLORS = listOf(
            Color(0xFFFFC107), // Amber
            Color(0xFF03A9F4), // Blue
            Color(0xFF4CAF50), // Green
            Color(0xFF9C27B0), // Purple
            Color(0xFFFF5722), // Orange
            Color(0xFFE91E63)  // Pink
        )
    }
}