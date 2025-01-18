package com.voyantiq.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voyantiq.app.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var isLogoVisible by remember { mutableStateOf(false) }
    var isTextVisible by remember { mutableStateOf(false) }
    var isSloganVisible by remember { mutableStateOf(false) }

    // Launch animation sequence
    LaunchedEffect(Unit) {
        isLogoVisible = true
        delay(500) // Wait for 500ms
        isTextVisible = true
        delay(300) // Wait for 300ms
        isSloganVisible = true
        delay(2000) // Wait for 2 seconds
        onSplashComplete() // Navigate after animations
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        AnimatedVisibility(
            visible = isLogoVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -100 })
        ) {
            Image(
                painter = painterResource(id = R.drawable.voyant_logo),
                contentDescription = "Voyant IQ Logo",
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App Name
        AnimatedVisibility(
            visible = isTextVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
        ) {
            Text(
                text = "VOYANT IQ",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0072BC)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Slogan
        AnimatedVisibility(
            visible = isSloganVisible,
            enter = fadeIn()
        ) {
            Text(
                text = "See Your Journey Ahead",
                fontSize = 20.sp,
                color = Color(0xFF0072BC)
            )
        }
    }
}