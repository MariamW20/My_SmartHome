package com.example.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
//import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.navigation.pages.*
import com.example.navigation.ui.theme.LocalAppColor

@Composable
fun MainScreen(modifier: Modifier = Modifier,onColorChange: (Color) -> Unit
) {
    val navController = rememberNavController()
    val appColor = LocalAppColor.current

    val navItems = listOf(
        NavItem("Favorites", Icons.Outlined.Star, "favorites"),
        NavItem("Things", Icons.Filled.Apps, "things"),
        NavItem("Routines", Icons.Filled.Refresh, "routines"),
        NavItem("Ideas", Icons.Outlined.Lightbulb, "ideas"),
        NavItem("Settings", Icons.Filled.Settings, "settings")
    )

    // Bottom navigation bar
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
                            selectedIconColor = appColor,
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
            composable("settings") {
                SettingsPage(
                    navController = navController,
                    onColorChange = onColorChange
                )
            }
        }
    }
}
