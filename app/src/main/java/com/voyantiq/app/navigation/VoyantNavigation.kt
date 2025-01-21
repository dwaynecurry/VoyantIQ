package com.voyantiq.app.navigation

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
        // Authentication Flow
        composable(NavigationRoutes.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(NavigationRoutes.Welcome.route) {
                        popUpTo(NavigationRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // In your VoyantNavigation.kt, update the welcome screen navigation
        composable(NavigationRoutes.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(NavigationRoutes.SignUp.route)
                },
                onLoginClick = {
                    navController.navigate(NavigationRoutes.Login.route)
                },
                onDevBypassClick = {
                    navController.navigate(NavigationRoutes.Home.route) {
                        popUpTo(NavigationRoutes.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Navigation Screens
        composable(NavigationRoutes.Home.route) {
            HomeScreen(
                onTripClick = { tripId ->
                    navController.navigate(NavigationRoutes.TripDetails.createRoute(tripId))
                },
                onNewTripClick = {
                    navController.navigate(NavigationRoutes.TripPlanning.route)
                },
                onSearchClick = {
                    navController.navigate(NavigationRoutes.Search.route)
                },
                onProfileClick = {
                    navController.navigate(NavigationRoutes.Profile.route)
                }
            )
        }

        composable(NavigationRoutes.Search.route) {
            SearchScreen(
                onDestinationClick = { destinationId ->
                    navController.navigate(NavigationRoutes.Destination.createRoute(destinationId))
                }
            )
        }

        composable(NavigationRoutes.Bookings.route) {
            BookingsScreen(
                onBookingClick = { bookingId ->
                    navController.navigate(NavigationRoutes.BookingDetails.createRoute(bookingId))
                }
            )
        }

        composable(NavigationRoutes.Profile.route) {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditPreferencesClick = {
                    navController.navigate(NavigationRoutes.Preferences.route)
                },
                onPaymentMethodsClick = {
                    navController.navigate(NavigationRoutes.PaymentMethods.route)
                },
                onSettingsClick = {
                    navController.navigate(NavigationRoutes.Settings.route)
                },
                onLogoutClick = {
                    navController.navigate(NavigationRoutes.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Destination details
        composable(
            route = NavigationRoutes.Destination.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val destinationId = backStackEntry.arguments?.getString("id") ?: return@composable
            DestinationScreen(
                destinationId = destinationId,
                onBackClick = { navController.popBackStack() },
                onPlanTripClick = {
                    navController.navigate(NavigationRoutes.TripPlanning.route)
                }
            )
        }

        // Booking details
        composable(
            route = NavigationRoutes.BookingDetails.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: return@composable
            BookingDetailsScreen(
                bookingId = bookingId,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Keep your existing routes...
    }
}