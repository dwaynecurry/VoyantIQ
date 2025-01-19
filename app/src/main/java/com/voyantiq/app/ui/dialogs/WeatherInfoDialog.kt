package com.voyantiq.app.ui.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.ui.graphics.Color

enum class WeatherCondition(
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
) {
    SUNNY(
        description = "Sunny",
        icon = Icons.Default.WbSunny,
        color = Color(0xFFFFB300)
    ),
    PARTLY_CLOUDY(
        description = "Partly Cloudy",
        icon = Icons.Default.WbCloudy,
        color = Color(0xFF90A4AE)
    ),
    CLOUDY(
        description = "Cloudy",
        icon = Icons.Default.CloudQueue,
        color = Color(0xFF78909C)
    ),
    RAINY(
        description = "Rainy",
        icon = Icons.Default.Grain,
        color = Color(0xFF42A5F5)
    ),
    STORMY(
        description = "Stormy",
        icon = Icons.Default.FlashOn,
        color = Color(0xFF5C6BC0)
    )
}