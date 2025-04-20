package com.example.navigation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navigation.data.RoutineEntity
import com.example.navigation.data.RoutineViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesPage(modifier: Modifier = Modifier, routineViewModel: RoutineViewModel = viewModel()) {
    val routines by routineViewModel.routines.collectAsState()
    val hasRoutines = routines.isNotEmpty()

    var showEditDialog by remember { mutableStateOf(false) }
    var editingRoutine by remember { mutableStateOf<RoutineEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var routineToDelete by remember { mutableStateOf<RoutineEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
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
            bottomBar = { RoutinesBottomNav() }
        ) { paddingValues ->
            Box(modifier = modifier.fillMaxSize().padding(paddingValues)) {
                if (!hasRoutines) {
                    EmptyRoutinesState(Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp) // Add padding at bottom for FAB visibility
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

        // Add routine FAB positioned at the bottom center, above the navigation bar
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = Color(0xFF03A9F4),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp)
                .size(56.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Routine",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if (showEditDialog && editingRoutine != null) {
        RoutineDialog(
            isAdd = false,
            routine = editingRoutine!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedRoutine ->
                routineViewModel.updateRoutine(updatedRoutine)
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
                TextButton(
                    onClick = {
                        routineViewModel.deleteRoutine(routineToDelete!!)
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
            onSave = { newRoutine ->
                routineViewModel.insertRoutine(newRoutine)
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

    // Safely parse time or use defaults to avoid crashes
    val initialTimeData = try {
        if (time.isNotEmpty()) {
            val parsedTime = SimpleDateFormat("h:mm a", Locale.getDefault()).parse(time)
            val calendar = Calendar.getInstance().apply {
                if (parsedTime != null) {
                    this.time = parsedTime
                }
            }
            Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        } else {
            Pair(12, 0)
        }
    } catch (e: Exception) {
        Pair(12, 0)
    }

    // Initialize time picker with safe values
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

                // Time Picker Field
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

                // Recurrence Dropdown
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
                                modifier = Modifier.clickable { expanded = !expanded }
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(IntrinsicSize.Max)
                    ) {
                        recurrenceOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    recurrence = option
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
                        onSave(
                            routine.copy(
                                name = name,
                                time = time,
                                recurrence = recurrence
                            )
                        )
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

    // Time Picker Dialog
    if (showTimePickerDialog) {
        TimePickerDialog(
            onDismiss = { showTimePickerDialog = false },
            onConfirm = {
                try {
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }
                    time = SimpleDateFormat("h:mm a", Locale.getDefault())
                        .format(calendar.time)
                } catch (e: Exception) {
                    // Fallback in case of formatting error
                    time = "${timePickerState.hour}:${String.format("%02d", timePickerState.minute)} ${if(timePickerState.hour < 12) "AM" else "PM"}"
                }
                showTimePickerDialog = false
            },
            timePickerState = timePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    timePickerState: TimePickerState
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                TimePicker(state = timePickerState)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
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
    routine: RoutineEntity,
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
            //Text(text = routine.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text("Task Name: ${routine.name}")
            Text("Timing: ${routine.time}")
            Text("Recurrence: ${routine.recurrence}")
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF03A9F4))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE91E63))
            }
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
        Text("No Routines!", fontSize = 20.sp, fontWeight = FontWeight.Medium)
        Text("Click the '+' button below to get started")

        // Add a downward arrow pointing to the FAB
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Down Arrow",
            modifier = Modifier.size(32.dp),
            tint = Color(0xFF03A9F4)
        )
    }
}

@Composable
fun RoutinesBottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        RoutinesNavItem(Icons.Default.Favorite, "Favorites")
        RoutinesNavItem(Icons.Default.Build, "Things")
        RoutinesNavItem(Icons.Default.List, "Routines", isSelected = true)
        RoutinesNavItem(Icons.Default.DateRange, "Ideas")
        RoutinesNavItem(Icons.Default.Settings, "Settings")
    }
}

@Composable
fun RoutinesNavItem(icon: ImageVector, label: String, isSelected: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (isSelected) Color(0xFF03A9F4) else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Text(
            label,
            fontSize = 10.sp,
            color = if (isSelected) Color(0xFF03A9F4) else Color.Gray
        )
    }
}