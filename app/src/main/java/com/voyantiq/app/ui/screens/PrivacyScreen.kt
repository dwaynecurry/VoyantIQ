package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrivacyScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SmallTopAppBar(
            title = { Text("Privacy Policy") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Text("←")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Last Updated: January 11, 2024",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrivacySection(
                title = "1. Information We Collect",
                content = """
                    • Account information (name, email, phone number)
                    • Address and payment details
                    • Travel preferences and history
                    • Device and usage information
                """.trimIndent()
            )

            PrivacySection(
                title = "2. How We Use Your Information",
                content = """
                    • To provide and improve our services
                    • To personalize your experience
                    • To process your transactions
                    • To communicate with you about our services
                """.trimIndent()
            )

            PrivacySection(
                title = "3. Information Sharing",
                content = """
                    • We do not sell your personal information
                    • We share information only with your consent
                    • We may share data with service providers
                    • We comply with legal requirements
                """.trimIndent()
            )

            // Add more sections as needed
        }
    }
}

@Composable
private fun PrivacySection(
    title: String,
    content: String
) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    Text(
        text = content,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}