package com.example.navigation.pages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
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
import com.example.navigation.data.SettingsViewModel
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val selectedColor by viewModel.selectedColor.collectAsState()
    val autoArmSecurityEnabled by viewModel.autoArm.collectAsState()
    val appNotificationsEnabled by viewModel.notifications.collectAsState()

    var showEditUserDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    val appColors = listOf(
        Color(0xFFFFEB3B), // Yellow
        Color(0xFFF44336), // Red
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
            // User Info
            SectionHeader("User Settings")
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFFDFCFC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "User", tint = Color(0xFFFFC107))
                }

                Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                    Text(text = userName, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Text(text = userEmail, color = Color.Gray, fontSize = 14.sp)
                }

                IconButton(onClick = { showEditUserDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit User", tint = Color.Gray)
                }
            }

            Divider()

            // App Settings
            SectionHeader("App Settings")

            SettingItem("App Color:", {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(selectedColor)
                        .padding(4.dp)
                )
            }, onClick = { showColorPicker = true })

            SettingItem("Auto Arm Security Alarm", {
                Switch(
                    checked = autoArmSecurityEnabled,
                    onCheckedChange = { viewModel.saveAutoArm(it) },
                    thumbContent = if (autoArmSecurityEnabled) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(SwitchDefaults.IconSize)) }
                    } else null,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFFC107),
                        checkedIconColor = Color(0xFFFFC107)
                    )
                )
            })

            SettingItem("App Notifications", {
                Switch(
                    checked = appNotificationsEnabled,
                    onCheckedChange = { viewModel.saveNotifications(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFFC107),
                        checkedIconColor = Color(0xFFFFC107)
                    )
                )
            })

            Divider()

            SectionHeader("Voice")
            SettingItem("Voice Assistants", {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow", tint = Color.Gray)
            })

            Divider()

            SectionHeader("App Permissions")
            SettingItem("Notifications & Permissions", {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow", tint = Color.Gray)
            })
        }
    }

    // Edit user dialog
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
                TextButton(onClick = {
                    viewModel.saveUserName(tempName)
                    viewModel.saveUserEmail(tempEmail)
                    showEditUserDialog = false
                }) {
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

    // Color picker dialog
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
                                    viewModel.saveAppColor(color)
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
                                    viewModel.saveAppColor(color)
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