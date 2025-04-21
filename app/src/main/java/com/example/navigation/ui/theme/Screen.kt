package com.example.navigation.ui.theme


sealed class Screens(val route: String) {
    object Favorites : Screens("favorites")
    object Things : Screens("things")
    object Routines : Screens("routines")
    object Ideas : Screens("ideas")
    object Settings : Screens("settings")
}
