package com.voyantiq.app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.voyantiq.app.ui.screens.*

@Composable
fun VoyantNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Splash.route
    ) {
        // Splash and Welcome
        composable(NavigationRoutes.Splash.route) {
            SplashScreen {
                navController.navigate(NavigationRoutes.Welcome.route) {
                    popUpTo(NavigationRoutes.Splash.route) { inclusive = true }
                }
            }
        }

        composable(NavigationRoutes.Welcome.route) {
            WelcomeScreen(
                navController = navController,
                onGetStartedClick = {
                    navController.navigate(NavigationRoutes.SignUp.route)
                },
                onLoginClick = {
                    navController.navigate(NavigationRoutes.Login.route)
                }
            )
        }
        // Authentication
        composable(NavigationRoutes.Login.route) {
            LoginScreen(
                onLoginComplete = {
                    navController.navigate(NavigationRoutes.Home.route) {
                        popUpTo(NavigationRoutes.Welcome.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onForgotPasswordClick = {
                    navController.navigate(NavigationRoutes.ForgotPassword.route)
                },
                onSignUpClick = {
                    navController.navigate(NavigationRoutes.SignUp.route)
                }
            )
        }

        composable(NavigationRoutes.SignUp.route) {
            SignUpScreen(
                onSignUpComplete = {
                    navController.navigate(NavigationRoutes.EmailVerification.createRoute(it))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onTermsClick = {
                    navController.navigate(NavigationRoutes.Terms.route)
                },
                onPrivacyClick = {
                    navController.navigate(NavigationRoutes.Privacy.route)
                }
            )
        }

        composable(
            route = NavigationRoutes.EmailVerification.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            EmailVerificationScreen(
                email = backStackEntry.arguments?.getString("email") ?: "",
                onVerificationComplete = {
                    navController.navigate(NavigationRoutes.Home.route) {
                        popUpTo(NavigationRoutes.Welcome.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onResendCode = {
                    Log.d("EmailVerification", "Resending verification code")
                }
            )
        }
        composable(NavigationRoutes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onResetSent = {
                    navController.navigate(NavigationRoutes.Login.route) {
                        popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Main Screens
        composable(NavigationRoutes.Home.route) {
            HomeScreen(
                onNewTripClick = { /* Handle new trip */ },
                onTripClick = { /* Handle trip click */ },
                onEditPreferencesClick = { /* Handle edit preferences */ },
                onPaymentMethodsClick = { /* Handle payment methods */ },
                onLogoutClick = { /* Handle logout */ }
            )
        }

        composable(NavigationRoutes.Preferences.route) {
            PreferencesScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPreferencesSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavigationRoutes.PaymentMethods.route) {
            PaymentMethodsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAddPaymentMethod = {
                    // Handle adding payment method
                }
            )
        }
        composable(NavigationRoutes.Terms.route) {
            TermsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavigationRoutes.Privacy.route) {
            PrivacyScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Trip Details
        composable(
            route = NavigationRoutes.TripDetails.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")
            TripDetailsScreen(
                tripId = tripId ?: "",
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { navController.popBackStack() },
                onEditClick = {
                    // Navigate to edit screen when implemented
                }
            )
        }
        // New Trip Planning and Itinerary Generation
        composable(NavigationRoutes.TripPlanning.route) {
            TripPlanningScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate(NavigationRoutes.ItineraryGeneration.route) }
            )
        }

        composable(NavigationRoutes.ItineraryGeneration.route) {
            ItineraryGenerationScreen(
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { /* Handle confirm */ }
            )
        }
    }
}