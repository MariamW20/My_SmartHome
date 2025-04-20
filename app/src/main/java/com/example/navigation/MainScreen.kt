package com.example.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navigation.data.RoutineViewModel
import com.example.navigation.pages.FavoritesPage
import com.example.navigation.pages.IdeasPage
import com.example.navigation.pages.RoutinesPage
import com.example.navigation.pages.SettingsPage
import com.example.navigation.pages.ThingsPage

@Composable
fun MainScreen( modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem("Favorites", Icons.Default.Favorite, 0),
        NavItem("Things",   Icons.Outlined.List,  1),
        NavItem("Routines", Icons.Outlined.Build, 2),
        NavItem("Ideas",    Icons.Filled.AddCircle,  3),
        NavItem("Settings", Icons.Sharp.Settings, 4),
    )


    var selectedIndex by remember {
    mutableIntStateOf(0)
}
    Scaffold(
        modifier = Modifier.fillMaxSize() ,
        bottomBar = {
           NavigationBar{
             navItemList.forEachIndexed { index, navItem ->
                 NavigationBarItem(
                     selected = selectedIndex == index,
                     onClick = { selectedIndex = index },
                     icon = {
                         Icon(
                             imageVector = navItem.icon,
                             contentDescription = "Icon",
                             tint = if (selectedIndex == index) Color.Yellow else Color.Gray
                         )
                     },
                     label = {
                         Text(
                             text = navItem.label,
                             color = Color.Black
                         )
                     },
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
      ContentScreen(modifier = Modifier.padding(innerPadding),selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int,    routineViewModel: RoutineViewModel = viewModel()
) {
    when(selectedIndex){
        0-> FavoritesPage()
        1-> ThingsPage()
        2-> RoutinesPage(modifier = Modifier,routineViewModel)
        3-> IdeasPage()
        4-> SettingsPage()
    }
}