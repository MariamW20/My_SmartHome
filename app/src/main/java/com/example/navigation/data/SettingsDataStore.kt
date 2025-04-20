package com.example.navigation.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private const val DATASTORE_NAME = "user_settings"
val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class SettingsDataStore(private val context: Context) {
    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val APP_COLOR = intPreferencesKey("app_color")
        val AUTO_ARM = booleanPreferencesKey("auto_arm")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
    }

    val userName: Flow<String> = context.dataStore.data.map { it[USER_NAME] ?: "" }
    val userEmail: Flow<String> = context.dataStore.data.map { it[USER_EMAIL] ?: "" }
    val appColor: Flow<Int> = context.dataStore.data.map { it[APP_COLOR] ?: 0xFF2196F3.toInt() }
    val autoArm: Flow<Boolean> = context.dataStore.data.map { it[AUTO_ARM] ?: false }
    val notifications: Flow<Boolean> = context.dataStore.data.map { it[NOTIFICATIONS] ?: true }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { it[USER_NAME] = name }
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { it[USER_EMAIL] = email }
    }

    suspend fun saveAppColor(color: Int) {
        context.dataStore.edit { it[APP_COLOR] = color }
    }

    suspend fun saveAutoArm(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_ARM] = enabled }
    }

    suspend fun saveNotifications(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS] = enabled }
    }
}