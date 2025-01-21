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

        composable(NavigationRoutes.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(NavigationRoutes.SignUp.route)
                },
                onLoginClick = {
                    navController.navigate(NavigationRoutes.Login.route)
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
                    navController.navigate(NavigationRoutes.DestinationDetails.createRoute(destinationId))
                }
            )
        }

        composable(
            route = NavigationRoutes.DestinationDetails.route,
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

        composable(
            route = NavigationRoutes.TripDetails.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            TripDetailsScreen(
                tripId = tripId,
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    // Will implement edit functionality later
                }
            )
        }

        composable(NavigationRoutes.TripPlanning.route) {
            TripPlanningScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate(NavigationRoutes.ItineraryGeneration.route)
                }
            )
        }

        // Add other routes as needed...
    }
}