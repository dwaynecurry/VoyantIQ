package com.voyantiq.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.voyantiq.app.navigation.VoyantNavigation
import com.voyantiq.app.ui.theme.VoyantTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VoyantTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Determine if we should show bottom nav based on current route
                val shouldShowBottomBar = remember(currentRoute) {
                    when (currentRoute) {
                        "splash", "welcome", "signup", "login",
                        "email_verification", "forgot_password" -> false
                        else -> true
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Home, "Home") },
                                    label = { Text("Home") },
                                    selected = currentRoute == "home",
                                    onClick = {
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Search, "Explore") },
                                    label = { Text("Explore") },
                                    selected = currentRoute == "search",
                                    onClick = {
                                        navController.navigate("search") {
                                            popUpTo("home")
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.BookOnline, "Bookings") },
                                    label = { Text("Bookings") },
                                    selected = currentRoute == "bookings",
                                    onClick = {
                                        navController.navigate("bookings") {
                                            popUpTo("home")
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, "Profile") },
                                    label = { Text("Profile") },
                                    selected = currentRoute == "profile",
                                    onClick = {
                                        navController.navigate("profile") {
                                            popUpTo("home")
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        VoyantNavigation(navController)
                    }
                }
            }
        }
    }
}