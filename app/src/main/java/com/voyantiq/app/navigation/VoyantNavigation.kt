package com.voyantiq.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.voyantiq.app.ui.screens.*

@Composable
fun VoyantNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen
        composable("splash") {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate("welcome") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Welcome Screen
        composable("welcome") {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate("signup")
                },
                onLoginClick = {
                    navController.navigate("login")
                },
                onDevBypassClick = {
                    // Navigate directly to home screen, bypassing authentication
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // Sign Up Screen
        composable("signup") {
            SignUpScreen(
                onSignUpComplete = {
                    navController.navigate("email_verification")
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onTermsClick = {
                    navController.navigate("terms")
                },
                onPrivacyClick = {
                    navController.navigate("privacy")
                }
            )
        }

        // Login Screen
        composable("login") {
            LoginScreen(
                onLoginComplete = {
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
                }
            )
        }

        // Email Verification Screen
        composable(
            route = "email_verification/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            EmailVerificationScreen(
                email = email,
                onVerificationComplete = {
                    navController.navigate("user_preferences") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onResendCode = {
                    // Handle resend code
                }
            )
        }

        // Forgot Password Screen
        composable("forgot_password") {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onResetSent = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Terms Screen
        composable("terms") {
            TermsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Privacy Screen
        composable("privacy") {
            PrivacyScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // User Preferences Screen
        composable("user_preferences") {
            ModernUserPreferencesScreen(
                onPreferencesSaved = {
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Home Screen
        composable("home") {
            HomeScreen(
                onNewTripClick = {
                    navController.navigate("trip_planning")
                },
                onTripClick = { tripId ->
                    navController.navigate("trip_details/$tripId")
                },
                onProfileClick = {
                    navController.navigate("profile")
                }
            )
        }

        // Trip Planning Screen
        composable("trip_planning") {
            TripPlanningScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNextClick = { tripDetails ->
                    navController.navigate("itinerary_generation")
                }
            )
        }

        // Trip Details Screen
        composable(
            route = "trip_details/{tripId}",
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")
            TripDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = {
                    // Navigate to edit trip screen
                }
            )
        }

        // Profile Screen
        composable("profile") {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditPreferencesClick = {
                    navController.navigate("user_preferences")
                },
                onLogoutClick = {
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Itinerary Generation Screen
        composable("itinerary_generation") {
            ItineraryGenerationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onConfirmClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}