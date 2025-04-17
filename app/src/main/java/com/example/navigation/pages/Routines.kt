package com.example.navigation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Routine(
    val id: Int,
    val name: String,
    val time: String,
    val recurrence: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesPage(modifier: Modifier = Modifier) {
    // State for routines list
    var routines by remember {
        mutableStateOf(
            listOf(
                Routine(1, "Security Lights on", "8:00pm", "Every day"),
                Routine(2, "Security Lights off", "6:00am", "Every day"),
                Routine(3, "Coffee Maker on", "7:00am", "Week days")
            )
        )
    }

    // Derived state for empty check
    val hasRoutines = routines.isNotEmpty()

    // State for edit dialog
    var showEditDialog by remember { mutableStateOf(false) }
    var editingRoutine by remember { mutableStateOf<Routine?>(null) }

    // State for delete confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var routineToDelete by remember { mutableStateOf<Routine?>(null) }

    // State for add dialog
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Smart Home",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        style = TextStyle(textAlign = TextAlign.Center, fontSize = 24.sp),
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFFFC107))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF03A9F4),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomNav()
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!hasRoutines) {
                EmptyRoutinesState(Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(routines) { routine ->
                        RoutineItem(
                            routine = routine,
                            onEdit = {
                                editingRoutine = routine
                                showEditDialog = true
                            },
                            onDelete = {
                                routineToDelete = routine
                                showDeleteDialog = true
                            }
                        )
                        Divider(color = Color.LightGray)
                    }
                }
            }
        }
    }

    // Edit Dialog
    if (showEditDialog && editingRoutine != null) {
        RoutineDialog(
            isAdd = false,
            routine = editingRoutine!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedRoutine ->
                routines = routines.map {
                    if (it.id == updatedRoutine.id) updatedRoutine else it
                }
                showEditDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && routineToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Routine") },
            text = { Text("Are you sure you want to delete \"${routineToDelete!!.name}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        routines = routines.filter { it.id != routineToDelete!!.id }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Dialog
    if (showAddDialog) {
        RoutineDialog(
            isAdd = true,
            routine = Routine(
                id = (routines.maxOfOrNull { it.id } ?: 0) + 1,
                name = "",
                time = "",
                recurrence = ""
            ),
            onDismiss = { showAddDialog = false },
            onSave = { newRoutine ->
                routines = routines + newRoutine
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDialog(
    isAdd: Boolean,
    routine: Routine,
    onDismiss: () -> Unit,
    onSave: (Routine) -> Unit
) {
    var name by remember { mutableStateOf(routine.name) }
    var time by remember { mutableStateOf(routine.time) }
    var recurrence by remember { mutableStateOf(routine.recurrence) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isAdd) "Add Routine" else "Edit Routine") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = recurrence,
                    onValueChange = { recurrence = it },
                    label = { Text("Recurrence") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        Routine(
                            id = routine.id,
                            name = name,
                            time = time,
                            recurrence = recurrence
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RoutineItem(
    routine: Routine,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = routine.name,
                fontSize = 16.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )
            Text("Timing: ${routine.time}")
            Text("Recurrence: ${routine.recurrence}")
        }

        // Edit and Delete icons
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF03A9F4)
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFFE91E63)
            )
        }
    }
}

@Composable
fun EmptyRoutinesState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "No Routines",
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("No Routines!", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
        Text("Click the '+' button below to get started")
    }
}

@Composable
fun BottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem(Icons.Default.Favorite, "Favorites")
        BottomNavItem(Icons.Default.Build, "Things")
        BottomNavItem(Icons.Default.List, "Routines")
        BottomNavItem(Icons.Default.DateRange, "Ideas")
        BottomNavItem(Icons.Default.Settings, "Settings")
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = Color.Gray)
        Text(label, fontSize = 10.sp)
    }
}