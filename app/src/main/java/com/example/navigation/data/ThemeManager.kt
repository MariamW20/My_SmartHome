package com.example.navigation.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color


object AppTheme {
    var primaryColor by mutableStateOf(UserPreferencesManager.DEFAULT_COLORS[0])
        private set

    fun updateColor(newColor: Color, preferencesManager: UserPreferencesManager) {
        primaryColor = newColor
        preferencesManager.saveAppColor(newColor.value.toInt())
    }

    fun initialize(preferencesManager: UserPreferencesManager) {
        primaryColor = Color(preferencesManager.getAppColor())
    }
}