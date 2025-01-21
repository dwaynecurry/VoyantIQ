package com.voyantiq.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Color definitions
object VoyantColors {
    val Primary = Color(0xFF0072BC)        // Main brand blue
    val Secondary = Color(0xFF4DA3DD)      // Light blue
    val Tertiary = Color(0xFFE6F3FA)       // Pale blue
    val Success = Color(0xFF4CAF50)        // Green
    val Background = Color(0xFFFFFFFF)     // White background
    val Surface = Color(0xFFF5F5F5)        // Light gray surface
    val Error = Color(0xFFE53935)          // Error red
    val TextPrimary = Color(0xFF333333)    // Primary text
    val TextSecondary = Color(0xFF666666)  // Secondary text
    val Disabled = Color(0xFFCCCCCC)       // Disabled state
    val Divider = Color(0xFFEEEEEE)        // Divider color
    val PlaceholderText = Color(0xFF999999) // Placeholder text
}

// Remove any other color declarations and use VoyantColors directly
private val LightColorScheme = lightColorScheme(
    primary = VoyantColors.Primary,
    secondary = VoyantColors.Secondary,
    tertiary = VoyantColors.Tertiary,
    background = VoyantColors.Background,
    surface = VoyantColors.Surface,
    error = VoyantColors.Error,
    onPrimary = Color.White,
    onSecondary = VoyantColors.TextPrimary,
    onTertiary = VoyantColors.TextPrimary,
    onBackground = VoyantColors.TextPrimary,
    onSurface = VoyantColors.TextPrimary,
    onError = Color.White,
    surfaceVariant = VoyantColors.Surface,
    onSurfaceVariant = VoyantColors.TextSecondary,
    outline = VoyantColors.Divider
)

@Composable
fun VoyantTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}