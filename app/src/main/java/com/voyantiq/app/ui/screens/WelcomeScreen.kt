package com.voyantiq.app.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.voyantiq.app.BuildConfig
import com.voyantiq.app.DevModeManager
import com.voyantiq.app.R
import com.voyantiq.app.navigation.NavigationRoutes
import com.voyantiq.app.ui.theme.VoyantColors

@Composable
fun WelcomeScreen(
    navController: NavController,
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.voyant_logo),
            contentDescription = "Voyant IQ Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = "VOYANT IQ",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = VoyantColors.Primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tagline
        Text(
            text = "See Your Journey Ahead",
            fontSize = 20.sp,
            color = VoyantColors.Primary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Get Started Button
        Button(
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VoyantColors.Primary
            )
        ) {
            Text("Get Started")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        TextButton(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Already have an account? Sign in",
                color = VoyantColors.Primary
            )
        }

        // Dev Bypass Button
        if (BuildConfig.DEBUG) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    Log.d("WelcomeScreen", "Dev Bypass Clicked")
                    Log.d("WelcomeScreen", "Current Destination: ${navController.currentDestination?.route}")
                    Log.d("WelcomeScreen", "Attempting to navigate to: ${NavigationRoutes.Home.route}")

                    DevModeManager.enableDevMode()

                    try {
                        navController.navigate(NavigationRoutes.Home.route) {
                            // Clear back stack to prevent going back to welcome screen
                            popUpTo(NavigationRoutes.Welcome.route) { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Log.e("WelcomeScreen", "Navigation Error", e)
                        e.printStackTrace()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green
                )
            ) {
                Text("DEV BYPASS", color = Color.White)
            }
        }
    }
}