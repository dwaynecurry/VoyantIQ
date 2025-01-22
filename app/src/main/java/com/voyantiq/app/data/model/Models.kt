package com.voyantiq.app.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Enums
enum class TripStatus {
    PLANNING,
    UPCOMING,
    IN_PROGRESS,
    COMPLETED
}

enum class ActivityType {
    FLIGHT,
    HOTEL,
    RESTAURANT,
    SIGHTSEEING,
    EVENT,
    TRANSPORT,
    SHOPPING,
    ACTIVITY;

    fun getIcon(): ImageVector {
        return when (this) {
            FLIGHT -> Icons.Default.FlightTakeoff
            HOTEL -> Icons.Default.Hotel
            RESTAURANT -> Icons.Default.Restaurant
            SIGHTSEEING -> Icons.Default.Landscape
            EVENT -> Icons.Default.Event
            TRANSPORT -> Icons.Default.DirectionsCar
            SHOPPING -> Icons.Default.ShoppingBag
            ACTIVITY -> Icons.Default.SportsSoccer
        }
    }

    fun getTitle(): String {
        return when (this) {
            FLIGHT -> "Flight"
            HOTEL -> "Accommodation"
            RESTAURANT -> "Dining"
            SIGHTSEEING -> "Sightseeing"
            EVENT -> "Event"
            TRANSPORT -> "Transport"
            SHOPPING -> "Shopping"
            ACTIVITY -> "Activity"
        }
    }
}

enum class BookingStatus {
    CONFIRMED,
    PENDING,
    CANCELLED,
    COMPLETED,
    FAILED
}

enum class PaymentStatus {
    PAID,
    PENDING,
    REFUNDED,
    FAILED
}

enum class PaymentMethod {
    CREDIT_CARD,
    PAYPAL,
    BANK_TRANSFER,
    POINTS,
    OTHER
}

enum class NotificationType {
    BOOKING_CONFIRMATION,
    PAYMENT_REMINDER,
    TRIP_REMINDER,
    PRICE_ALERT,
    TRAVEL_UPDATE,
    SYSTEM_UPDATE
}

enum class NotificationPriority {
    HIGH,
    MEDIUM,
    LOW
}

// Data Classes
data class Trip(
    val id: String,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: TripStatus,
    val progress: Float,
    val budget: Double,
    val spent: Double = 0.0,
    val coverImageUrl: String? = null,
    val activities: List<TripActivity> = emptyList()
)

data class TripActivity(
    val id: String,
    val title: String,
    val type: ActivityType,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    val cost: Double,
    val isBooked: Boolean = false,
    val bookingReference: String? = null,
    val notes: String? = null
)

data class BookingHistory(
    val id: String,
    val tripId: String,
    val activityId: String,
    val bookingDate: LocalDateTime,
    val bookingStatus: BookingStatus,
    val paymentStatus: PaymentStatus,
    val confirmationNumber: String,
    val totalAmount: Double,
    val paymentMethod: PaymentMethod,
    val cancellationPolicy: String? = null,
    val refundAmount: Double? = null,
    val notes: String? = null
)

data class Notification(
    val id: String,
    val type: NotificationType,
    val priority: NotificationPriority,
    val title: String,
    val message: String,
    val timestamp: LocalDateTime,
    val isRead: Boolean = false,
    val relatedTripId: String? = null,
    val relatedBookingId: String? = null,
    val actionUrl: String? = null
)

data class BookingPreferences(
    val preferredPaymentMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    val autoConfirmBookings: Boolean = false,
    val priceAlerts: Boolean = true,
    val maxBudgetPerActivity: Double? = null,
    val preferredBookingTime: String? = null,
    val cancelNotificationPeriod: Int = 24, // hours
    val savedPaymentMethods: List<String> = emptyList(),
    val notificationPreferences: NotificationPreferences = NotificationPreferences()
)

data class NotificationPreferences(
    val enablePushNotifications: Boolean = true,
    val enableEmailNotifications: Boolean = true,
    val enablePriceAlerts: Boolean = true,
    val enableTripReminders: Boolean = true,
    val enableBookingUpdates: Boolean = true,
    val quietHoursStart: String? = null,
    val quietHoursEnd: String? = null,
    val notificationTypes: Set<NotificationType> = NotificationType.values().toSet()
)

// Utility Object
object DateTimeUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM d")
    private val yearFormatter = DateTimeFormatter.ofPattern("yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun formatDate(date: LocalDate): String = dateFormatter.format(date)

    fun formatDateRange(startDate: LocalDate, endDate: LocalDate): String =
        "${formatDate(startDate)} - ${formatDate(endDate)}, ${yearFormatter.format(endDate)}"

    fun formatTime(time: LocalDateTime): String = timeFormatter.format(time)

    fun formatTimeRange(start: LocalDateTime, end: LocalDateTime): String =
        "${formatTime(start)} - ${formatTime(end)}"
}

// Sample Data
object SampleData {
    private val now = LocalDate.now()

    // Existing sample trips
    val sampleTrips = listOf(
        Trip(
            id = "1",
            destination = "Paris, France",
            startDate = now.plusDays(15),
            endDate = now.plusDays(22),
            status = TripStatus.UPCOMING,
            progress = 0.8f,
            budget = 3500.0,
            spent = 2800.0,
            activities = listOf(
                TripActivity(
                    id = "1-1",
                    title = "Flight to Paris",
                    type = ActivityType.FLIGHT,
                    startTime = now.plusDays(15).atTime(10, 30),
                    endTime = now.plusDays(15).atTime(22, 45),
                    location = "CDG Airport",
                    cost = 850.0,
                    isBooked = true,
                    bookingReference = "AF1234"
                ),
                TripActivity(
                    id = "1-2",
                    title = "Hotel Le Grand",
                    type = ActivityType.HOTEL,
                    startTime = now.plusDays(15).atTime(15, 0),
                    endTime = now.plusDays(22).atTime(11, 0),
                    location = "1 Rue de la Paix",
                    cost = 1200.0,
                    isBooked = true,
                    bookingReference = "HLG789"
                )
            )
        )
        // Add more sample trips as needed
    )

    // Sample Booking History
    val bookingHistory = listOf(
        BookingHistory(
            id = "BK001",
            tripId = "1",
            activityId = "1-1",
            bookingDate = LocalDateTime.now().minusDays(30),
            bookingStatus = BookingStatus.CONFIRMED,
            paymentStatus = PaymentStatus.PAID,
            confirmationNumber = "AF1234-CONF",
            totalAmount = 850.0,
            paymentMethod = PaymentMethod.CREDIT_CARD,
            cancellationPolicy = "Refundable up to 24 hours before departure"
        )
        // Add more booking history entries as needed
    )

    // Sample Notifications
    val notifications = listOf(
        Notification(
            id = "N001",
            type = NotificationType.BOOKING_CONFIRMATION,
            priority = NotificationPriority.HIGH,
            title = "Booking Confirmed",
            message = "Your flight to Paris has been confirmed",
            timestamp = LocalDateTime.now().minusHours(2),
            relatedTripId = "1",
            relatedBookingId = "BK001"
        ),
        Notification(
            id = "N002",
            type = NotificationType.PRICE_ALERT,
            priority = NotificationPriority.MEDIUM,
            title = "Price Drop Alert",
            message = "Hotel prices in Paris have dropped by 20%",
            timestamp = LocalDateTime.now().minusHours(5)
        )
        // Add more notifications as needed
    )

    // Sample Booking Preferences
    val defaultBookingPreferences = BookingPreferences(
        preferredPaymentMethod = PaymentMethod.CREDIT_CARD,
        autoConfirmBookings = false,
        priceAlerts = true,
        maxBudgetPerActivity = 1000.0,
        preferredBookingTime = "10:00",
        cancelNotificationPeriod = 24,
        savedPaymentMethods = listOf(
            "Visa ending in 4242",
            "Mastercard ending in 5555",
            "PayPal account: user@example.com"
        ),
        notificationPreferences = NotificationPreferences(
            enablePushNotifications = true,
            enableEmailNotifications = true,
            enablePriceAlerts = true,
            enableTripReminders = true,
            enableBookingUpdates = true,
            quietHoursStart = "22:00",
            quietHoursEnd = "07:00"
        )
    )

    // Helper Functions
    fun getTripById(tripId: String): Trip? = sampleTrips.find { it.id == tripId }

    fun getActivitiesForTrip(tripId: String): List<TripActivity> =
        getTripById(tripId)?.activities ?: emptyList()

    fun getBookingsByTripId(tripId: String): List<BookingHistory> =
        bookingHistory.filter { it.tripId == tripId }

    fun getUnreadNotifications(): List<Notification> =
        notifications.filter { !it.isRead }

    fun getNotificationsByType(type: NotificationType): List<Notification> =
        notifications.filter { it.type == type }

    fun getBookingsByStatus(status: BookingStatus): List<BookingHistory> =
        bookingHistory.filter { it.bookingStatus == status }
}