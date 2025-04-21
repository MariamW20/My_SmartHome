package com.example.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.navigation.pages.*

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Initialize the NavController
    val navController = rememberNavController()

    // Define your navigation items (bottom navigation menu)
    val navItems = listOf(
        NavItem("Favorite", Icons.Default.Favorite, "favorites"),
        NavItem("Things", Icons.Default.List, "things"),
        NavItem("Routines", Icons.Default.DateRange, "routines"),
        NavItem("Ideas", Icons.Default.Build, "ideas"),
        NavItem("Settings", Icons.Default.Settings, "settings")
    )

    // Scaffold to manage the app structure with a bottom navigation bar
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach { navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                        onClick = {
                            navController.navigate(navItem.route) {
                                // Pop up to the start destination
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.label
                            )
                        },
                        label = { Text(navItem.label) },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Yellow,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Black,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHost defines the navigation graph with routes
        NavHost(
            navController = navController,
            startDestination = "favorites",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Define the composables for each screen
            composable("favorites") { FavoritesPage(navController) }
            composable("things") { ThingsPage(navController) }
            composable("routines") { RoutinesPage(navController) }
            composable("ideas") { IdeasPage(navController) }
            composable("settings") { SettingsPage(navController) }
        }
    }
}
