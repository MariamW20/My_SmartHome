package com.example.navigation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <ImageVector> ActionButton(s: String, info: ImageVector) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingsPage(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top Yellow Bar
        TopAppBar(
            title = { Text("My Smart Home", color = Color.White, style = TextStyle(textAlign = TextAlign.Center, fontSize = 24.sp ),modifier = Modifier.fillMaxWidth()) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFFFFC107)),
            actions = {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                }
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
            }
            )

    Box(
        modifier = modifier.fillMaxSize().background(Color(0xFFF8F8F8)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "No Devices",
                tint = Color.Gray,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "No Things!", fontSize = 30.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "It looks like we didn't discover any devices.\n",
                fontSize = 20.sp,
                color = Color.Gray
            )
            Text(text = "Try an option below", fontSize = 20.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Column {
                ActionButton("Run Discovery", Icons.Default.Refresh)
                ActionButton("Add a Cloud Account", Icons.Default.Add)
                ActionButton("View Our Supported Devices", Icons.Default.Phone)
                ActionButton("Contact Support", Icons.Default.Info)
            }
        }
    }
    }
}

@Composable
fun ActionButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Button(
        onClick = { /* No action needed */ },
        modifier = Modifier.fillMaxWidth(0.8f).padding(4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color.White)
    }
}
