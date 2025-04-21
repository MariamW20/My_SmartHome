package com.example.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.navigation.data.AppTheme
import com.example.navigation.data.UserPreferencesManager
import com.example.navigation.ui.theme.NavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // This will allow edge-to-edge on devices that support it
        val preferencesManager = UserPreferencesManager(this)
        AppTheme.initialize(preferencesManager)
        setContent {
            NavigationTheme {
                // Set the MainScreen composable as the content
                MainScreen()
            }
        }
    }
}
