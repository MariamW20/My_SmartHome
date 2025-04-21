package com.example.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.navigation.data.UserPreferencesManager
import com.example.navigation.ui.theme.LocalAppColor
import com.example.navigation.ui.theme.NavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = UserPreferencesManager(applicationContext)
        val initialColor = Color(prefs.getAppColor())

        setContent {
            var appColor by remember { mutableStateOf(initialColor) }

            CompositionLocalProvider(LocalAppColor provides appColor) {
                NavigationTheme {
                    MainScreen(
                        onColorChange = { newColor ->
                            appColor = newColor
                            prefs.saveAppColor(newColor.toArgb())
                        }
                    )
                }
            }
        }
    }
}