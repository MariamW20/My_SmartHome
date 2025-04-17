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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(modifier: Modifier = Modifier) {
    // State variables for user settings
    var userName by remember { mutableStateOf("John Doe") }
    var userEmail by remember { mutableStateOf("john@someorg.com") }
    var selectedColor by remember { mutableStateOf(Color(0xFFFFC107)) }
    var autoArmSecurityEnabled by remember { mutableStateOf(true) }
    var appNotificationsEnabled by remember { mutableStateOf(false) }

    // State for edit dialog
    var showEditUserDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    // Available app colors
    val appColors = listOf(
        Color.Yellow,
        Color.Red,
        Color(0xFF03A9F4), // Blue
        Color(0xFF4CAF50), // Green
        Color(0xFFFF9800), // Orange
        Color(0xFF9C27B0)  // Purple
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
        bottomBar = {
            BottomNav(selectedColor)
        }
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
                        tint = Color(0xFFFFC107)
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
                        onCheckedChange = { autoArmSecurityEnabled = it },
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
                            checkedTrackColor = Color(0xFFFFC107),
                            checkedIconColor = Color(0xFFFFC107)
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
                        onCheckedChange = { appNotificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFFFC107),
                            checkedIconColor = Color(0xFFFFC107)
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
                    tint = Color(0xFFFFC107)
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

    // Color Picker Dialog
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
                                onClick = { selectedColor = color }
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
                                onClick = { selectedColor = color }
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
fun BottomNav(selectedColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem(Icons.Default.Favorite, "Favorites")
        BottomNavItem(Icons.Default.List, "Things")
        BottomNavItem(Icons.Default.DateRange, "Routines")
        BottomNavItem(Icons.Default.Build, "Ideas")
        BottomNavItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            tint = selectedColor
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