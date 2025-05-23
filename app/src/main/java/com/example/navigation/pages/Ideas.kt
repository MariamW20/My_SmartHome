package com.example.navigation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.navigation.ui.theme.LocalAppColor

@Composable
fun IdeasPage(navController: NavHostController, modifier: Modifier = Modifier) {
    val selectedColor = LocalAppColor.current
    Column(
        modifier = modifier.fillMaxSize()
            .background(Color(0xFFFDFCFC)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Ideas Page",
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            color = selectedColor
        )
    }
}
