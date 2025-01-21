package com.voyantiq.app.navigation

sealed class NavigationRoutes(val route: String) {
    // Existing authentication routes
    object Splash : NavigationRoutes("splash")
    object Welcome : NavigationRoutes("welcome")
    object Login : NavigationRoutes("login")
    object SignUp : NavigationRoutes("signup")

    // Main navigation routes
    object Home : NavigationRoutes("home")
    object Search : NavigationRoutes("search")
    object Bookings : NavigationRoutes("bookings")
    object Profile : NavigationRoutes("profile")

    // Trip related routes
    object TripPlanning : NavigationRoutes("trip_planning")
    object TripDetails : NavigationRoutes("trip_details/{tripId}") {
        fun createRoute(tripId: String) = "trip_details/$tripId"
    }
    object ItineraryGeneration : NavigationRoutes("itinerary_generation")

    // Profile related routes
    object Preferences : NavigationRoutes("preferences")
    object PaymentMethods : NavigationRoutes("payment_methods")
    object Settings : NavigationRoutes("settings")

    // Authentication related routes
    object EmailVerification : NavigationRoutes("email_verification/{email}") {
        fun createRoute(email: String) = "email_verification/$email"
    }
    object ForgotPassword : NavigationRoutes("forgot_password")
    object Terms : NavigationRoutes("terms")
    object Privacy : NavigationRoutes("privacy")

    // Search related routes
    object SearchResults : NavigationRoutes("search_results/{query}") {
        fun createRoute(query: String) = "search_results/$query"
    }
    object Destination : NavigationRoutes("destination/{id}") {
        fun createRoute(id: String) = "destination/$id"
    }

    // Booking related routes
    object BookingDetails : NavigationRoutes("booking_details/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_details/$bookingId"
    }
    object BookingHistory : NavigationRoutes("booking_history")
}