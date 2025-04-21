package com.example.navigation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigation.data.UserPreferencesManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    navController: androidx.navigation.NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferencesManager = remember { UserPreferencesManager(context) }

    // State variables for user settings, initialized from SharedPreferences
    var userName by remember { mutableStateOf(preferencesManager.getUserName()) }
    var userEmail by remember { mutableStateOf(preferencesManager.getUserEmail()) }
    var selectedColor by remember { mutableStateOf(preferencesManager.getAppColor()) }
    var autoArmSecurityEnabled by remember { mutableStateOf(preferencesManager.getAutoArmSecurity()) }
    var appNotificationsEnabled by remember { mutableStateOf(preferencesManager.getAppNotifications()) }

    // State for edit dialog
    var showEditUserDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    // Available app colors
    val appColors = listOf(
        Color.Yellow,
        Color.Red,
        Color(0xFF03A9F4), // Blue
        Color(0xFF4CAF50), // Green
        Color(0xFF9C27B0), // Purple
        Color(0xFFFF9800)  // Orange
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Smart Home",
                        style = TextStyle(textAlign = TextAlign.Center, fontSize = 24.sp),
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = selectedColor
                )
            )
        },
        //bottomBar = {
            //BottomNav(selectedColor, navController)
       // }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFFDFCFC))
        ) {
            // User Settings Section
            SectionHeader(title = "User Settings")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFDFCFC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = selectedColor
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = userName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = userEmail,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                IconButton(onClick = { showEditUserDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit User",
                        tint = Color.Gray
                    )
                }
            }

            Divider()

            // App Settings Section
            SectionHeader(title = "App Settings")

            // App Color Setting
            SettingItem(
                title = "App Color:",
                content = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(selectedColor)
                            .padding(4.dp)
                    )
                },
                onClick = { showColorPicker = true }
            )

            // Auto Arm Security Alarm Setting
            SettingItem(
                title = "Auto Arm Security Alarm",
                content = {
                    Switch(
                        checked = autoArmSecurityEnabled,
                        onCheckedChange = {
                            autoArmSecurityEnabled = it
                            preferencesManager.saveAutoArmSecurity(it)
                        },
                        thumbContent = if (autoArmSecurityEnabled) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = selectedColor,
                            checkedIconColor = selectedColor
                        )
                    )
                }
            )

            // App Notifications Setting
            SettingItem(
                title = "App Notifications",
                content = {
                    Switch(
                        checked = appNotificationsEnabled,
                        onCheckedChange = {
                            appNotificationsEnabled = it
                            preferencesManager.saveAppNotifications(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = selectedColor,
                            checkedIconColor = selectedColor
                        )
                    )
                }
            )

            Divider()

            // Voice Section (for visual purposes)
            SectionHeader(title = "Voice")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Voice",
                    tint = selectedColor
                )

                Text(
                    text = "Voice Assistants",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Medium
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = Color.Gray
                )
            }

            Divider()

            // App Permissions Section (for visual purposes)
            SectionHeader(title = "App Permissions")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Permissions",
                    tint = Color.Gray
                )

                Text(
                    text = "Notifications & Permissions",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Medium
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = Color.Gray
                )
            }
        }
    }

    // Edit User Dialog
    if (showEditUserDialog) {
        var tempName by remember { mutableStateOf(userName) }
        var tempEmail by remember { mutableStateOf(userEmail) }

        AlertDialog(
            onDismissRequest = { showEditUserDialog = false },
            title = { Text("Edit User") },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        userName = tempName
                        userEmail = tempEmail

                        // Save to SharedPreferences
                        preferencesManager.saveUserName(tempName)
                        preferencesManager.saveUserEmail(tempEmail)

                        showEditUserDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditUserDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Color Picker Dialog - FIXED to update both state and preferences immediately
    if (showColorPicker) {
        AlertDialog(
            onDismissRequest = { showColorPicker = false },
            title = { Text("Select App Color") },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        appColors.take(3).forEach { color ->
                            ColorOption(
                                color = color,
                                isSelected = color == selectedColor,
                                onClick = {
                                    // Update both state and SharedPreferences
                                    selectedColor = color // Assuming color is already a Color object
                                    preferencesManager.saveAppColor(color.toArgb()) // Convert Color to Int for storage
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        appColors.takeLast(3).forEach { color ->
                            ColorOption(
                                color = color,
                                isSelected = color == selectedColor,
                                onClick = {
                                    // Update both state and SharedPreferences
                                    selectedColor = color // Assuming color is already a Color object
                                    preferencesManager.saveAppColor(color.toArgb()) // Convert Color to Int for storage
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showColorPicker = false }) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
fun BottomNav(selectedColor: Color, navController: androidx.navigation.NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem(Icons.Default.Favorite, "Favorites", navController, "favorites")
        BottomNavItem(Icons.Default.List, "Things", navController, "things")
        BottomNavItem(Icons.Default.DateRange, "Routines", navController, "routines")
        BottomNavItem(Icons.Default.Build, "Ideas", navController, "ideas")
        BottomNavItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            navController = navController,
            route = "settings",
            tint = selectedColor
        )
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    navController: androidx.navigation.NavController,
    route: String,
    tint: Color = Color.Gray
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = tint
        )
    }
}

// Keep these helper composables as they were
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEEEEEE))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        fontWeight = FontWeight.Medium,
        color = Color.Gray
    )
}

@Composable
fun SettingItem(
    title: String,
    content: @Composable () -> Unit,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )

        content()
    }
}

@Composable
fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color, CircleShape)
                .then(
                    if (isSelected) {
                        Modifier.border(2.dp, Color.Black, CircleShape)
                    } else {
                        Modifier
                    }
                )
        )
    }
}


@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    tint: Color = Color.Gray
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = tint
        )
    }
}