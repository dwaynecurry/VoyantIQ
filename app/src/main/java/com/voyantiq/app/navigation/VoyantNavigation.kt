package com.voyantiq.app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.voyantiq.app.BuildConfig
import com.voyantiq.app.DevModeManager
import com.voyantiq.app.ui.screens.*

@Composable
fun VoyantNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Splash.route
    ) {
        // ACTIVE ROUTES

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
                    navController.navigate(NavigationRoutes.EmailVerification.createRoute("test@example.com")) {
                        popUpTo(NavigationRoutes.Welcome.route)
                    }
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
                onLogoutClick = {
                    DevModeManager.disableDevMode()
                    navController.navigate(NavigationRoutes.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onEditPreferencesClick = {
                    navController.navigate(NavigationRoutes.Preferences.route)
                },
                onPaymentMethodsClick = {
                    navController.navigate(NavigationRoutes.PaymentMethods.route)
                }
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

        composable(
            route = NavigationRoutes.TripDetails.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            TripDetailsScreen(
                tripId = tripId,
                onBackClick = {
                    navController.popBackStack()
                },
                onConfirmClick = {
                    navController.popBackStack()
                },
                onEditClick = {
                    // Navigate to edit screen when implemented
                }
            )
        }

        // PLANNED ROUTES (Commented out until implemented)
        /*
        // Trip Planning Flow
        composable(NavigationRoutes.TripPlanning.route) {
            TripPlanningScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onBasicDetailsComplete = { tripId ->
                    navController.navigate(NavigationRoutes.ItineraryGeneration.createRoute(tripId))
                }
            )
        }

        composable(
            route = NavigationRoutes.ItineraryGeneration.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) {
            // ItineraryGenerationScreen implementation pending
        }

        // Search and Discovery
        composable(NavigationRoutes.Search.route) {
            // SearchScreen implementation pending
        }

        composable(
            route = NavigationRoutes.SearchResults.route,
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) {
            // SearchResultsScreen implementation pending
        }

        composable(
            route = NavigationRoutes.DestinationDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            // DestinationDetailsScreen implementation pending
        }

        // Booking Flow
        composable(
            route = NavigationRoutes.BookingDetails.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) {
            // BookingDetailsScreen implementation pending
        }

        // User Profile and Settings
        composable(NavigationRoutes.Profile.route) {
            // ProfileScreen implementation pending
        }

        composable(NavigationRoutes.Settings.route) {
            // SettingsScreen implementation pending
        }

        // Social Features
        composable(NavigationRoutes.Community.route) {
            // CommunityScreen implementation pending
        }

        composable(
            route = NavigationRoutes.UserProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            // UserProfileScreen implementation pending
        }

        // Premium Features
        composable(NavigationRoutes.PremiumMembership.route) {
            // PremiumMembershipScreen implementation pending
        }

        composable(NavigationRoutes.RewardPoints.route) {
            // RewardPointsScreen implementation pending
        }

        composable(NavigationRoutes.VIPServices.route) {
            // VIPServicesScreen implementation pending
        }
        */
    }
}