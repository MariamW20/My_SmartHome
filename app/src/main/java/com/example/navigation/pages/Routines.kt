package com.example.navigation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navigation.data.RoutineEntity
import com.example.navigation.data.RoutineViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.navigation.ui.theme.LocalAppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesPage(
    navController: NavHostController,
    routineViewModel: RoutineViewModel = viewModel()
) {
    val routines by routineViewModel.routines.collectAsState()
    val hasRoutines = routines.isNotEmpty()
    val appColor = LocalAppColor.current

    var showEditDialog by remember { mutableStateOf(false) }
    var editingRoutine by remember { mutableStateOf<RoutineEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var routineToDelete by remember { mutableStateOf<RoutineEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
            containerColor = Color.White,
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
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = appColor)
                )
            },
            //bottomBar = { RoutinesBottomNav(navController) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (!hasRoutines) {
                    EmptyRoutinesState(Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
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

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = appColor,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp)
                .size(56.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Routine", modifier = Modifier.size(24.dp))
        }
    }

    if (showEditDialog && editingRoutine != null) {
        RoutineDialog(
            isAdd = false,
            routine = editingRoutine!!,
            onDismiss = { showEditDialog = false },
            onSave = {
                routineViewModel.updateRoutine(it)
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog && routineToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Routine") },
            text = { Text("Are you sure you want to delete \"${routineToDelete!!.name}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    routineViewModel.deleteRoutine(routineToDelete!!)
                    showDeleteDialog = false
                }) {
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

    if (showAddDialog) {
        RoutineDialog(
            isAdd = true,
            routine = RoutineEntity(
                id = (routines.maxOfOrNull { it.id } ?: 0) + 1,
                name = "",
                time = "",
                recurrence = ""
            ),
            onDismiss = { showAddDialog = false },
            onSave = {
                routineViewModel.insertRoutine(it)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDialog(
    isAdd: Boolean,
    routine: RoutineEntity,
    onDismiss: () -> Unit,
    onSave: (RoutineEntity) -> Unit
) {
    var name by remember { mutableStateOf(routine.name) }
    var time by remember { mutableStateOf(routine.time) }
    var recurrence by remember { mutableStateOf(routine.recurrence) }

    // Parse the time into hour and minute (12-hour format) without using Calendar
    val initialTimeData = try {
        if (time.isNotEmpty()) {
            val parsedTime = SimpleDateFormat("h:mm a", Locale.getDefault()).parse(time)
            Pair(parsedTime.hours, parsedTime.minutes) // Use hours and minutes directly
        } else {
            Pair(12, 0) // Default time
        }
    } catch (e: Exception) {
        Pair(12, 0) // Default time if parsing fails
    }

    val timePickerState = rememberTimePickerState(
        initialHour = initialTimeData.first,
        initialMinute = initialTimeData.second,
        is24Hour = false
    )

    var showTimePickerDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val recurrenceOptions = listOf("Day", "Week", "Month", "Year", "Week days", "Weekend", "Every day")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isAdd) "Add Routine" else "Edit Routine") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Task Name*") },
                    isError = name.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = time,
                    onValueChange = {},
                    label = { Text("Time*") },
                    readOnly = true,
                    isError = time.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePickerDialog = true },
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Select Time",
                            modifier = Modifier.clickable { showTimePickerDialog = true }
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = recurrence,
                        onValueChange = {},
                        label = { Text("Recurrence*") },
                        readOnly = true,
                        isError = recurrence.isBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select Recurrence",
                                modifier = Modifier.clickable { expanded = true }
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        recurrenceOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    recurrence = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && time.isNotBlank() && recurrence.isNotBlank()) {
                        onSave(routine.copy(name = name, time = time, recurrence = recurrence))
                    }
                },
                enabled = name.isNotBlank() && time.isNotBlank() && recurrence.isNotBlank()
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

    // Time Picker Dialog logic
    if (showTimePickerDialog) {
        AlertDialog(
            onDismissRequest = { showTimePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    // Format only the time in 12-hour format with AM/PM
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    val displayHour = if (hour % 12 == 0) 12 else hour % 12
                    val amPm = if (hour < 12) "AM" else "PM"
                    time = "$displayHour:${String.format("%02d", minute)} $amPm"
                    showTimePickerDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Select Time") },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}


@Composable
fun RoutineItem(routine: RoutineEntity, onEdit: () -> Unit, onDelete: () -> Unit) {
    val appColor = LocalAppColor.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Task Name: ${routine.name}")
            Text("Timing: ${routine.time}")
            Text("Recurrence: ${routine.recurrence}")
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = appColor)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE91E63))
            }
        }
    }
}

@Composable
fun EmptyRoutinesState(modifier: Modifier = Modifier) {
    val appColor = LocalAppColor.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Refresh, contentDescription = "No Routines", modifier = Modifier.size(64.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No Routines!", fontSize = 20.sp, fontWeight = FontWeight.Medium,color = appColor)
        Text("Click the '+' button below to get started")
        Spacer(modifier = Modifier.height(16.dp))
        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down Arrow", modifier = Modifier.size(32.dp), tint = Color(0xFF03A9F4))
    }
}

@Composable
fun RoutinesNavItem(icon: ImageVector, label: String, navController: NavHostController, route: String, isSelected: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(4.dp).clickable {
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }) {
        Icon(icon, contentDescription = label, tint = if (isSelected) Color(0xFF03A9F4) else Color.Gray, modifier = Modifier.size(24.dp))
        Text(label, fontSize = 10.sp, color = if (isSelected) Color(0xFF03A9F4) else Color.Gray)
    }
}
