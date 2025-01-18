package com.voyantiq.app.navigation

sealed class NavigationRoutes(val route: String) {
    object Splash : NavigationRoutes("splash")
    object Welcome : NavigationRoutes("welcome")
    object Login : NavigationRoutes("login")
    object SignUp : NavigationRoutes("signup")
    object Home : NavigationRoutes("home")
    object Profile : NavigationRoutes("profile")
    object TripPlanning : NavigationRoutes("trip_planning")
    object Preferences : NavigationRoutes("preferences")
    object Search : NavigationRoutes("search")
    object ItineraryGeneration : NavigationRoutes("itinerary_generation")
    object TripDetails : NavigationRoutes("trip_details/{tripId}") {
        fun createRoute(tripId: String) = "trip_details/$tripId"
    }
    object Notifications : NavigationRoutes("notifications")
    object Settings : NavigationRoutes("settings")
    object PaymentMethods : NavigationRoutes("payment_methods")
}